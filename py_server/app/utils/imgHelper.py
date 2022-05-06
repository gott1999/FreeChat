# -*- coding: UTF-8 -*-

import os
from app.logger import log


def getImage(path: str):
    """
    get image
    :param path: path
    :return: bin
    """
    res = None
    try:
        if os.path.exists(path):
            with open(path, 'rb') as r:
                res = r.read()
    except Exception as e:
        log.Logger.error("path=%s by='helper' error get image" % path)
    return res


def saveImage(path: str, name: str, img: bytes):
    """
    save Image
    :param path: str
    :param name: img name
    :param img: bytes
    """
    try:
        if not os.path.exists(path):
            os.mkdir(path)
        with open("%s/%s" % (path, name), 'wb') as w:
            w.write(img)
    except Exception as e:
        log.Logger.error("path=%s by='helper' error save image" % path)
        raise e
