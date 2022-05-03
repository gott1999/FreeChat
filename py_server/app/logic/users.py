# -*- coding: UTF-8 -*-

from app.db import users
from app.logger import log


def login(uname, upassword):
    if not uname and not upassword:
        return None
    res = users.login(uname, upassword)
    if len(res) < 1:
        return None

    return res


def register(uname, upassword):
    if not uname and not upassword:
        return 0
    res = 0

    try:
        res = users.register(uname, upassword)
    except Exception as e:
        res = 0
        log.Logger.error(e)

    if res < 1:
        return 0
    else:
        return 1


def getContact(uid=''):
    pass


def getIcon(uid=''):
    pass

