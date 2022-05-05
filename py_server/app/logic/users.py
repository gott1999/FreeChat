# -*- coding: UTF-8 -*-
import os

from app.db import users
from proto import protocol_pb2 as protoc


def login(uname: str, upassword: str):
    """
    login
    :param uname: name
    :param upassword: password
    :return: {}
    """
    if uname and upassword:
        rt = users.login(uname, upassword)
        if len(rt) < 1:
            return None
        uid = rt[0]["uid"]
        user_base = users.getUserBase(uid)
        return user_base[0]
    else:
        return None


def register(uname: str, upassword: str):
    """
    register and login
    :param uname: name
    :param upassword: password
    :return: {}
    """
    if uname and upassword:
        res = users.register(uname, upassword)
        if res < 1:
            return None
        else:
            return login(uname, upassword)
    else:
        return None


def getContact(uid: str):
    """
    get contacts
    :param uid: src_uid
    :return: [{}, {}...]
    """
    contacts = users.getContact(uid)
    res = {}
    for it in contacts:
        carry = protoc.Protocol()
        res.valid = True

    return res


def getIcon(icon: str):
    """
    get icon
    :param icon: icon name
    :return: bin
    """
    getImage("./upload/icon/" + icon)


def getImg(img: str):
    """
    get image
    :param img: img name
    :return: bin
    """
    getImage("./upload/img/" + img)


def getImage(path: str):
    """
    get image
    :param path: path
    :return: bin
    """
    res = None
    if os.path.exists(path):
        with open(path, 'rb') as r:
            res = r.read()
    return res
