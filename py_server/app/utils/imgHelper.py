# -*- coding: UTF-8 -*-

import os
from app.logger import log

path = "./upload/img/%s"


def getImage(name: str):
    """
    get image
    :param name: str
    :return: bin
    """
    res = None
    if name and name != "":
        try:
            if os.path.exists(path % name):
                with open(path % name, 'rb') as r:
                    res = r.read()
                log.Logger.log("name=%s by='helper' loaded res.len=%s" % (name, len(res)))
        except Exception as e:
            log.Logger.error("name=%s by='helper' error get image" % name)
            raise e
    return res


def saveImage(name: str, img: bytes):
    """
    save Image
    :param name: img name
    :param img: bytes
    """
    if not os.path.exists(path % name):
        try:
            with open(path % name, 'wb') as w:
                w.write(img)
        except Exception as e:
            log.Logger.error("name=%s by='helper' error save image" % name)
            raise e
