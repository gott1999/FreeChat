# -*- coding: UTF-8 -*-

import time

from app.db import users
from app.logger import log
from app.utils import imgHelper
from app.utils import randomUtils
from proto import protocol_pb2 as protoc
from app.logic.message.message_pool import MessageManager

ipPool = {}


def login(uname: str, upassword: str, address: tuple):
    """
    login
    :param address:
    :param uname: name
    :param upassword: password
    :return: {}
    """
    res = get_standard_invalid_response()

    if uname and upassword:
        try:
            rt = users.login(uname, upassword)
            if rt:
                # get base data
                uid = str(rt["uid"])
                user_base = users.getUserBase(uid)

                # set base data
                res.pairs["uid"] = uid
                res.pairs["displayName"] = user_base["displayName"]
                res.pairs["key"] = randomUtils.getStandard()
                if user_base["icon"]:
                    res.pairs["icon"] = user_base["icon"]
                res.valid = True

                # set uid to ip mapping
                tryUpdateIp(uid, address[0])
            else:
                res.pairs["ERROR"] = "Wrong password or uname"
        except Exception as e:
            res.pairs["ERROR"] = "Server Error"
            log.Logger.error("ip=%s:%s by='logic' Login error." % address + " %s" % str(e))
    return res


def logout(uid: str):
    if uid in ipPool:
        del ipPool[uid]
    return get_standard_valid_response()


def register(uname: str, upassword: str, address: tuple):
    """
    register and login
    :param address:
    :param uname: name
    :param upassword: password
    :return: {}
    """
    if uname and upassword:
        try:
            if users.register(uname, upassword):
                res = login(uname, upassword, address)
            else:
                res = get_standard_invalid_response()
                res.pairs["ERROR"] = "The user:%s is registered" % uname
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' Register error." % address + " %s" % str(e))
            res = get_standard_invalid_response()
            res.pairs["ERROR"] = "Server Error"
    else:
        res = get_standard_invalid_response()
        res.pairs["ERROR"] = "Server Error"
    return res


def getContacts(uid: str, address: tuple):
    """
    get contacts
    :param address:
    :param uid: src_uid
    :return: protoc.Protocol() -> pairs["uid"->"sub-protocol"]
    """
    res = get_standard_invalid_response()
    if uid:
        res.config["datatype"] = "Protocol"
        try:
            # get db
            contacts = users.getContacts(uid)
            # build Protocol
            for contact in contacts:
                # temp carrier
                t = protoc.Protocol()
                # for every returns
                for key, value in contact.items():
                    # keep it not None
                    if key and value:
                        t.pairs[key] = str(value)
                # set valid
                t.valid = True
                # add sub-Protocol
                res.bits[str(contact["uid"])] = t.SerializeToString()
            # set valid
            res.valid = True
            # set uid to ip mapping
            tryUpdateIp(uid, address[0])
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' pull contact error." % address + " %s" % str(e))
    return res


def getContact(src_uid: str, des_uid: str, address: tuple):
    """
    pull single contact for src_uid
    :param src_uid:
    :param des_uid:
    :param address:
    :return:
    """
    res = get_standard_invalid_response()
    if src_uid and des_uid:
        res.config["datatype"] = "Protocol"
        try:
            # get db
            contact = users.getContact(src_uid, des_uid)
            # temp carrier
            t = protoc.Protocol()
            # for every returns
            for key, value in contact.items():
                # keep it not None
                if key and value:
                    t.pairs[key] = str(value)
            # set valid
            t.valid = True
            # add sub-Protocol
            res.bits[contact["uid"]] = t.SerializeToString()
            # set valid
            res.valid = True

            # set uid to ip mapping
            tryUpdateIp(src_uid, address[0])
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' pull contact error." % address + " %s" % str(e))
    return res


def updateUserBase(uid: str, pairs: dict, bits: dict, address: tuple):
    """
    update icon
    :param address:
    :param bits: img bytes
    :param uid: uid
    :param pairs: pairs
    :return:
    """
    res = get_standard_invalid_response()
    if uid:
        if pairs:
            try:
                if "icon" in pairs:
                    users.updateIcon(pairs["icon"], uid)
                if "displayName" in pairs:
                    users.updateDisplayName(pairs["displayName"], uid)
                if "upassword" in pairs:
                    users.updateUserUpassword(pairs["upassword"], uid)
                res.valid = True
            except Exception as e:
                log.Logger.error(
                    "ip=%s:%s uid=%s by='logic' update user data error. %s" % (address[0], address[1], uid, str(e)))
        if bits:
            for key, value in bits.items():
                imgHelper.saveImage(key, value)
    return res


def getImg(names: dict, address: tuple):
    """
    get image
    :param address:
    :param names: img names
    :return: bin
    """
    res = get_standard_invalid_response()
    res.config["datatype"] = "IMAGE"
    if names:
        for name in names.keys():
            try:
                log.Logger.log("img=%s by='logic' try load image." % name)
                bits = imgHelper.getImage(name)
                if bits:
                    res.bits[name] = bits
            except Exception as e:
                log.Logger.error("ip=%s:%s by='logic' get image error." % address + " %s" % str(e))
        res.valid = True
    return res


def getMessage(uid: str, address: tuple):
    res = get_standard_invalid_response()
    res.config["datatype"] = "UserMessage"

    if uid:
        # check whether you need push to client
        if MessageManager.manager.glance(uid) > 0:
            rt = MessageManager.manager.pop(uid)
            for i in range(0, len(rt)):
                res.bits[str(i)] = MessageManager.serialize(rt[i])
        res.valid = True

        # set uid to ip mapping
        tryUpdateIp(uid, address[0])

    return res


def addMessage(bits: bytes):
    """
    deserialize and push to messagePool
    :param bits: bytes
    :return:
    """
    m = MessageManager.deserialize(bits)
    if m.des and m.des != "":
        from proto import user_message_pb2 as msg
        log.Logger.log("%s" % m.message)
        if m.type == msg.MessageType.DEL:
            users.delRelationShip(m.sec, m.des)
        else:
            if m.type == msg.MessageType.ADD:
                rt = users.getUid(m.des)
                if len(rt) > 0:
                    m.des = str(rt["uid"])
            MessageManager.manager.push(m.des, m)


def tryGetIP(uid: str):
    if uid in ipPool:
        return ipPool[uid]
    else:
        return None


def tryUpdateIp(uid: str, ip: str):
    """
    while user change ip, need to update ip
    :param uid:
    :param ip:
    :return:
    """
    ipPool[uid] = ip


def get_standard_invalid_response():
    """
    build a standard response. Include: millisecond_timestamp, ConnectType=RESPONSE, valid=False
    :return:
    """
    res = protoc.Protocol()
    res.millisecond_timestamp = get_millisecond_timestamp()
    res.type = protoc.ConnectType.RESPONSE
    res.valid = False
    return res


def get_standard_valid_response():
    """
    build a standard response. Include: millisecond_timestamp, ConnectType=RESPONSE, valid=True
    :return:
    """
    res = protoc.Protocol()
    res.millisecond_timestamp = get_millisecond_timestamp()
    res.type = protoc.ConnectType.RESPONSE
    res.valid = True
    return res


def get_millisecond_timestamp():
    return round(time.time() * 1000)


def addRelationship(src, des):
    if src and des:
        users.addRelationShip(src, des)
    return get_standard_valid_response()
