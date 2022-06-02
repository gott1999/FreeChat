# -*- coding: UTF-8 -*-

import time
import os
from app.config.serve import DEFAULT_LOG_PATH as PATH


class Logger:
    fileName = None

    @staticmethod
    def init():
        if not os.path.exists(PATH):
            os.mkdir(PATH)
        if Logger.fileName is None:
            t = time.strftime('%Y年%m月%d日 %H时%M分%S秒', time.localtime())
            Logger.fileName = './%s/%s.html' % (PATH, t)

    @staticmethod
    def write(msg, color="#FFFFFF"):
        if Logger.fileName is None:
            Logger.init()
        out = """<div style="background-color:%s;margin-top:20px;">%s</div>\n""" % (color, msg)
        out_file = open(Logger.fileName, 'a')
        out_file.write(out)
        out_file.close()

    @staticmethod
    def log(msg=''):
        s = '[LOG]: %s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s)

    @staticmethod
    def error(msg=''):
        s = '[ERROR]: %s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s, "#FF0000")

    @staticmethod
    def warn(msg=''):
        s = '[WARN]: %s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s, "#CCFF33")

    @staticmethod
    def getTime():
        return time.strftime("Date=%Y-%m-%d Time=%H:%M:%S", time.localtime())
