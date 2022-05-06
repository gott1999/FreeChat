# -*- coding: UTF-8 -*-
import time

from app.db import users
from app.logger import log
from app.utils import imgHelper
from app.utils import randomUtils
from proto import protocol_pb2 as protoc


def login(uname: str, upassword: str, address: tuple):
    """
    login
    :param address:
    :param uname: name
    :param upassword: password
    :return: {}
    """
    res = get_standard_invalid_response()
    res.pairs["method"] = "LOGIN"

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

                # try set icon
                if user_base["icon"]:
                    img = imgHelper.getImage("./upload/icon/%s" % user_base["icon"])
                    if img:
                        res.pairs["key"] = user_base["icon"]
                        res.bits["icon"] = img

                res.valid = True
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' Login error" % address)
            raise e
    return res


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
                res = login(uname, upassword)
            else:
                res = get_standard_invalid_response()
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' Register error" % address)
            res = get_standard_invalid_response()
    else:
        res = get_standard_invalid_response()
    res.pairs["method"] = "REGISTER"
    return res


def getContact(uid: str, address: tuple):
    """
    get contacts
    :param address:
    :param uid: src_uid
    :return: protoc.Protocol() -> pairs["uid"->"sub-protocol"]
    """
    res = get_standard_invalid_response()
    if uid:
        res.protocol_type = protoc.ProtocolType.NESTED
        try:
            # get db
            contacts = users.getContact(uid)
            # build Protocol
            for i in contacts:
                # temp carrier
                t = protoc.Protocol()
                # for every returns
                for key, value in i.items():
                    # keep it not None
                    if key and value:
                        t.pairs[key] = str(value)
                        # if it has icon, put to data
                        if key == "icon":
                            img = imgHelper.getImage("./upload/icon/%s" % value)
                            if img:
                                res.bits[key] = img
                # set valid
                t.valid = True
                # set sub-Protocol
                res.pairs[t.pairs["uid"]] = str(t.SerializeToString(), "utf-8")
            # set valid
            res.valid = True
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' pull contact error" % address)
    return res


def getIconByUid(uid: str, address: tuple):
    """
    get icon
    :param address:
    :param uid:
    :return: bin
    """
    res = get_standard_invalid_response()

    if uid:
        # set uid
        res.pairs["uid"] = uid

        # get icon name from db
        icon = None
        try:
            icon = users.getIcon(uid)
        except Exception as e:
            log.Logger.error("ip=%s:%s by='db' pull icon error" % address)

        # get icon from disk
        if icon:
            # set icon name
            res.pairs["icon"] = icon
            # get img
            img = imgHelper.getImage("./upload/icon/%s" % icon)
            if img:
                # set img
                res.bits[icon] = img
                # set valid
                res.valid = True
    return res


def getImg(img: str, address: tuple):
    """
    get image
    :param address:
    :param img: img name
    :return: bin
    """
    res = get_standard_invalid_response()
    if img:
        try:
            img = imgHelper.getImage("./upload/img/" + img)
            if img:
                res.bits[img] = img
                res.valid = True
        except Exception as e:
            log.Logger.error("ip=%s:%s by='logic' get image error" % address)
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
    if uid and icon and bits:
        try:
            users.updateUserData("user_b_data", {"icon", icon}, uid)
            imgHelper.saveImage("./upload/icon", icon, bits)
            res.valid = True
        except Exception as e:
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
    except Exception as e:
        log.Logger.error("ip=%s:%s uid=%s display_name=%s by='db' error update displayName" % (address[0], address[1], uid, display_name))


def get_standard_invalid_response():
    """
    build a standard response
    including: millisecond_timestamp, ConnectType=RESPONSE,valid=False
    :return:
    """
    res = protoc.Protocol()
    res.millisecond_timestamp = time.time()
    res.type = protoc.ConnectType.RESPONSE
    res.valid = False
    return res
