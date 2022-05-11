# -*- coding: UTF-8 -*-
from app.logger import log
from proto import protocol_pb2 as protoc


def serialize(t: protoc.Protocol):
    """
    serialize
    :param t:
    :return:
    """
    return t.SerializeToString()


def deserialize(t: bytes):
    """
    deserialize
    :param t:
    :return:
    """
    res = protoc.Protocol()
    try:
        res.ParseFromString(t)
    except Exception as e:
        log.Logger.error("by='serializer' deserialize error %s" % e.__str__())
    return res
