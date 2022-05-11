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
            Logger.fileName = './%s/%s.txt' % (PATH, t)

    @staticmethod
    def write(msg):
        if Logger.fileName is None:
            Logger.init()
        if not msg.endswith('\n'):
            msg += '\n'
        file = open(Logger.fileName, 'a')
        file.write(msg)
        file.close()

    @staticmethod
    def log(msg=''):
        s = '[LOG]: %s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s)

    @staticmethod
    def error(msg=''):
        s = '[ERROR]: %s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s)

    @staticmethod
    def warn(msg=''):
        s = '[WARN]: %s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s)

    @staticmethod
    def getTime():
        return time.strftime("Date=%Y-%m-%d Time=%H:%M:%S", time.localtime())
