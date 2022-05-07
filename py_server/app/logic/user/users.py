# -*- coding: UTF-8 -*-

import time

from app.db import users
from app.logger import log
from app.utils import imgHelper
from app.utils import randomUtils
from proto import protocol_pb2 as protoc
from app.logic.message.message_pool import MessageManager


def login(uname: str, upassword: str, address: tuple):
    """
    login
    :param address:
    :param uname: name
    :param upassword: password
    :return: {}
    """
    res = get_standard_invalid_response()

    if uname and upassword and uname != "" and upassword != "":
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
                res.pairs["key"] = user_base["icon"]
                res.valid = True

                # set login pool
                MessageManager.ipPool[uid] = address[0]
        except Exception:
            log.Logger.error("ip=%s:%s by='logic' Login error" % address)
    return res


def logout(uid: str):
    if uid in MessageManager.ipPool:
        del MessageManager.ipPool[uid]
    return get_standard_valid_response()


def register(uname: str, upassword: str, address: tuple):
    """
    register and login
    :param address:
    :param uname: name
    :param upassword: password
    :return: {}
    """
    if uname and upassword and uname != "" and upassword != "":
        try:
            if users.register(uname, upassword):
                res = login(uname, upassword, address)
            else:
                res = get_standard_invalid_response()
        except Exception:
            log.Logger.error("ip=%s:%s by='logic' Register error" % address)
            res = get_standard_invalid_response()
    else:
        res = get_standard_invalid_response()
    return res


def getContacts(uid: str, address: tuple):
    """
    get contacts
    :param address:
    :param uid: src_uid
    :return: protoc.Protocol() -> pairs["uid"->"sub-protocol"]
    """
    res = get_standard_invalid_response()
    if uid and uid != "":
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
                res.bits[contact["uid"]] = t.SerializeToString()
            # set valid
            res.valid = True
        except Exception:
            log.Logger.error("ip=%s:%s by='logic' pull contact error" % address)
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
    if src_uid and des_uid and src_uid != "" and des_uid != "":
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
        except Exception:
            log.Logger.error("ip=%s:%s by='logic' pull contact error" % address)
    return res


def updateIcon(uid: str, icon: str, bits: bytes, address: tuple):
    """
    update icon
    :param address:
    :param bits: img bytes
    :param uid: uid
    :param icon: icon
    :return:
    """
    res = get_standard_invalid_response()
    if uid and icon and bits and uid != "" and icon != "":
        try:
            users.updateUserData("user_b_data", {"icon", icon}, uid)
            imgHelper.saveImage(icon, bits)
            res.valid = True
        except Exception:
            log.Logger.error("ip=%s:%s img=%s uid=%s by='logic' update icon error" % (address[0], address[1], icon, uid))
    return res


def updateDisplayName(uid: str, display_name: str, address: tuple):
    """
    update display name
    :param address:
    :param uid:
    :param display_name:
    :return:
    """
    res = get_standard_invalid_response()
    try:
        users.updateUserData("user_b_data", {"display_name", display_name}, uid)
        res.valid = True
    except Exception:
        log.Logger.error("ip=%s:%s uid=%s display_name=%s by='db' error update displayName" % (address[0], address[1], uid, display_name))


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
                bits = imgHelper.getImage(name)
                if bits:
                    res.bits[names] = bits
            except Exception:
                log.Logger.error("ip=%s:%s by='logic' get image error" % address)
        res.valid = True
    return res


def getMessage(uid: str, address: tuple):
    res = get_standard_invalid_response()
    res.config["datatype"] = "UserMessage"
    if uid and uid != "":
        # check whether you need push to client
        if MessageManager.manager.glance(uid) > 0:
            carrier = get_standard_valid_response()
            rt = MessageManager.manager.pop(uid)
            for i in range(0, len(rt)):
                carrier.pairs[str(i)] = MessageManager.serialize(rt[i])
            res.bits["data"] = carrier.SerializeToString()
        res.valid = True
    return res


def addMessage(bits: bytes):
    """
    deserialize and push to messagePool
    :param bits: bytes
    :return:
    """
    m = MessageManager.deserialize(bits)
    if m.des and m.des != "":
        MessageManager.manager.push(m.des, m)


def tryGetIP(uid: str):
    if uid in MessageManager.ipPool:
        return MessageManager.ipPool[uid]
    else:
        return None


def get_standard_invalid_response():
    """
    build a standard response. Include: millisecond_timestamp, ConnectType=RESPONSE, valid=False
    :return:
    """
    res = protoc.Protocol()
    res.millisecond_timestamp = time.time()
    res.type = protoc.ConnectType.RESPONSE
    res.valid = False
    return res


def get_standard_valid_response():
    """
    build a standard response. Include: millisecond_timestamp, ConnectType=RESPONSE, valid=True
    :return:
    """
    res = protoc.Protocol()
    res.millisecond_timestamp = time.time()
    res.type = protoc.ConnectType.RESPONSE
    res.valid = True
    return res
