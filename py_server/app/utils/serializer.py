# -*- coding: UTF-8 -*-
from proto import protocol_pb2 as protoc


def serialize(t: protoc.Protocol):
    return t.SerializeToString() + b'#'


def deserialize(t: bytes):
    t = protoc.Protocol()
    if t[-1:] == b'#':
        t.ParseFromString(t[:-1])
    else:
        t.ParseFromString(t)
    return t
