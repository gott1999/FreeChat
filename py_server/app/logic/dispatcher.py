# -*- coding: UTF-8 -*-

from proto import protocol_pb2 as protoc
from app.utils import serializer
from app.logger import log
from app.logic import users


def tick(data, address):
    """
    dispatch the request
    :param address: tuple(ip, port)
    :param data: bytes
    :return: response protocol
    """
    np = serializer.deserialize(data)

    log.Logger.log("ip=%s:%s" % address + " method=" + np.pairs["method"])

    # request
    if np.type == protoc.ConnectType.REQUEST:
        if np.pairs["method"] == "LOGIN":
            res = requestLogin(np, address)
        elif np.pairs["method"] == "REGISTER":
            res = requestRegister(np, address)
        elif np.pairs["method"] == "MESSAGE":
            res = requestMessage(np, address)
        elif np.pairs["method"] == "CONTACT":
            res = requestContact(np, address)
        else:
            res = users.get_standard_invalid_response()
    # transit
    elif np.type == protoc.ConnectType.TRANSIT:
        res = users.get_standard_invalid_response()
    # response
    else:
        res = users.get_standard_invalid_response()

    return serializer.serialize(res)


def requestLogin(np: protoc.Protocol, address: tuple):
    """
    tick login
    :param address:
    :param np: request protocol
    :return: protoc.Protocol
    """
    return users.login(np.pairs["uname"], np.pairs["upassword"], address)


def requestRegister(np: protoc.Protocol, address: tuple):
    """
    tick register
    :param address:
    :param np: request protocol
    :return: None
    """
    return users.register(np.pairs["uname"], np.pairs["upassword"], address)


def requestMessage(np: protoc.Protocol, address: tuple):

    pass


def requestContact(np: protoc.Protocol, address: tuple):
    """
    tick contacts
    :param np:
    :param address:
    :return:
    """
    return users.getContact(np.pairs["uid"], address)

