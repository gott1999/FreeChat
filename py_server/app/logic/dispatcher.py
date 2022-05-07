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


def tick(data, address):
    """
    dispatch the request
    :param address: tuple(ip, port)
    :param data: bytes
    :return: response protocol
    """
    np = serializer.deserialize(data)

    log.Logger.log("ip=%s:%s" % address + " method=" + np.config["method"])

    # request
    if np.type == protoc.ConnectType.REQUEST:
        if "REQUIRE" in np.config:
            if np.config["REQUIRE"] == "LOGIN":
                res = requestLogin(np, address)
            elif np.config["REQUIRE"] == "REGISTER":
                res = requestRegister(np, address)
            elif np.config["REQUIRE"] == "MESSAGE":
                res = requestMessage(np, address)
            elif np.config["REQUIRE"] == "CONTACTS":
                res = requestContacts(np, address)
            elif np.config["REQUIRE"] == "CONTACT":
                res = requestContact(np, address)
            elif np.config["REQUIRE"] == "IMAGE":
                res = getImg(np, address)
            else:
                res = users.get_standard_invalid_response()
        else:
            res = users.get_standard_invalid_response()
    # transit
    elif np.type == protoc.ConnectType.TRANSIT:
        res = mentionMessage(np, address)
    else:
        res = users.get_standard_invalid_response()

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


def requestMessage(np: protoc.Protocol, address: tuple):
    """
    tick user message
    :param np: request protocol
    :param address:
    :return: response protocol
    """
    if "uid" in np.pairs:
        uid = np.pairs["uid"]
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
    if "uid" in np.pairs:
        uid = np.pairs["uid"]
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
    if "src_uid" in np.pairs and "des_uid":
        uid = np.pairs["uid"]
        des = np.pairs["uid"]
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
    if "tar_uid" in np.pairs:
        des_uid = np.pairs["tar_uid"]
    else:
        des_uid = None
    if "data" in np.pairs and np.bits["data"]:
        # add to pool
        users.addMessage(np.bits["data"])
    if len(np.bits) > 0:
        threading.Thread(target=saveBits, args=np.bits)
    threading.Thread(target=mention, args=des_uid).start()
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
            import socket
            skt = socket.socket(DEFAULT_INET, DEFAULT_STREAM)
            try:
                skt.settimeout(10)
                skt.connect((host, DEFAULT_CONNECT_POOL_SIZE))
                skt.send(serializer.serialize(men))
            except Exception:
                users.logout(tar_uid)
            finally:
                skt.close()


def saveBits(bits: dict):
    for key, value in bits.items():
        if key and value:
            saveImage(key, value)


def getImg(np: protoc.Protocol, address: tuple):
    return users.getImg(np.pairs, address)

