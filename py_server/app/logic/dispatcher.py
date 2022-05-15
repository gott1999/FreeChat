# -*- coding: UTF-8 -*-
import threading

from proto import protocol_pb2 as protoc
from app.utils import serializer
from app.logger import log
from app.logic.user import users
from app.config.serve import DEFAULT_INET
from app.config.serve import DEFAULT_STREAM
from app.config.serve import DEFAULT_CONNECT_POOL_SIZE
from app.utils.imgHelper import saveImage


def addRelationship(np, address):
    if np.config["UID"] and np.pairs["des_uid"]:
        users.addRelationship(np.config["UID"], np.pairs["des_uid"])


def update(np, address):
    return users.updateUserBase(np.config["UID"], np.pairs, np.bits, address)


def tick(data: bytes, address: tuple):
    """
    dispatch the request
    :param address: tuple(ip, port)
    :param data: bytes
    :return: response protocol
    """
    res = users.get_standard_invalid_response()

    np = serializer.deserialize(data)

    # log
    log.Logger.log("ip=%s:%s" % address + " UID=%s" % np.config["UID"] + " connected!")

    # request
    if np.type == protoc.ConnectType.REQUEST:
        if "REQUIRE" in np.config:
            if np.config["REQUIRE"] == "LOGIN":
                res = requestLogin(np, address)
            elif np.config["REQUIRE"] == "REGISTER":
                res = requestRegister(np, address)
            elif np.config["REQUIRE"] == "LOGOUT":
                res = requestLogout(np)
            elif np.config["REQUIRE"] == "MESSAGE":
                res = requestMessage(np, address)
            elif np.config["REQUIRE"] == "CONTACTS":
                res = requestContacts(np, address)
            elif np.config["REQUIRE"] == "CONTACT":
                res = requestContact(np, address)
            elif np.config["REQUIRE"] == "IMAGE":
                res = getImg(np, address)
            elif np.config["REQUIRE"] == "UPDATE":
                res = update(np, address)
    elif np.type == protoc.ConnectType.TRANSIT:
        res = mentionMessage(np, address)
    else:
        if np.config["RESPONSE"] == "CONTACT":
            addRelationship(np, address)
        res = users.get_standard_valid_response()

    return serializer.serialize(res)


def requestLogin(np: protoc.Protocol, address: tuple):
    """
    tick login
    :param address:
    :param np: request protocol
    :return: response protocol
    """
    if "uname" in np.pairs and "upassword" in np.pairs:
        uname = np.pairs["uname"]
        upassword = np.pairs["upassword"]
    else:
        uname = None
        upassword = None
    return users.login(uname, upassword, address)


def requestRegister(np: protoc.Protocol, address: tuple):
    """
    tick register
    :param address:
    :param np: request protocol
    :return: response protocol
    """
    if "uname" in np.pairs and "upassword" in np.pairs:
        uname = np.pairs["uname"]
        upassword = np.pairs["upassword"]
    else:
        uname = None
        upassword = None
    return users.register(uname, upassword, address)


def requestLogout(np: protoc.Protocol):
    """
    log out
    :param np:
    :return:
    """
    if "UID" in np.config and np.config["UID"] != "VISITOR":
        uid = np.config["UID"]
    else:
        uid = None
    return users.logout(uid)


def requestMessage(np: protoc.Protocol, address: tuple):
    """
    tick user message
    :param np: request protocol
    :param address:
    :return: response protocol
    """
    if "UID" in np.config and np.config["UID"] != "VISITOR":
        uid = np.config["UID"]
    else:
        uid = None
    return users.getMessage(uid, address)


def requestContacts(np: protoc.Protocol, address: tuple):
    """
    tick contacts
    :param np:request protocol
    :param address:
    :return: response protocol
    """
    if "UID" in np.config and np.config["UID"] != "VISITOR":
        uid = np.config["UID"]
    else:
        uid = None
    return users.getContacts(uid, address)


def requestContact(np: protoc.Protocol, address: tuple):
    """
    tick contacts
    :param np:request protocol
    :param address:
    :return: response protocol
    """
    if "UID" in np.config and "des_uid" in np.pairs and np.config["UID"] != "VISITOR":
        uid = np.config["UID"]
        des = np.pairs["des_uid"]
    else:
        uid = None
        des = None
    return users.getContact(uid, des, address)


def mentionMessage(np: protoc.Protocol, address: tuple):
    """
    mention tar_uid message
    :param np:
    :param address:
    :return:
    """
    if "des_uid" in np.pairs and "UID" in np.config and np.config["UID"] != "VISITOR":
        users.tryUpdateIp(np.config["UID"], address[0])
        des_uid = np.pairs["des_uid"]
    else:
        des_uid = None

    if "data" in np.bits:
        log.Logger.log("uid=%s send message" % address[0])
        # add to pool
        users.addMessage(np.bits["data"])

    if len(np.bits) > 0:
        threading.Thread(target=saveBits, args=(np,)).start()
    if des_uid:
        mention(des_uid)

    return users.get_standard_valid_response()


def mention(tar_uid: str):
    """
    mention tar_uid to pull message
    :param tar_uid:
    :return:
    """
    if tar_uid and tar_uid != "":
        men = users.get_standard_valid_response()
        men.config["MENTION"] = "MESSAGE"
        host = users.tryGetIP(tar_uid)
        if host:
            log.Logger.log("tar=%s ip=host mention" % tar_uid)
            import socket
            skt = socket.socket(DEFAULT_INET, DEFAULT_STREAM)
            try:
                skt.settimeout(10)
                skt.connect((host, DEFAULT_CONNECT_POOL_SIZE))
                skt.send(serializer.serialize(men))
                skt.send(b'#')
            except Exception as e:
                log.Logger.error(e.__str__())
                users.logout(tar_uid)
            finally:
                skt.close()


def saveBits(np: protoc.Protocol):
    for key, value in np.bits.items():
        if key and value and key != "data":
            # 服务器不存储数据 用户自己解码
            saveImage(key, value)


def getImg(np: protoc.Protocol, address: tuple):
    log.Logger.log("ip=%s:%s pull image." % address)
    return users.getImg(np.pairs, address)
