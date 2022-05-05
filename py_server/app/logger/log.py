# -*- coding: UTF-8 -*-

import time
import os

path = 'log'


class Logger:
    fileName = None

    @staticmethod
    def init():
        if not os.path.exists(path):
            os.mkdir(path)
        if Logger.fileName is None:
            t = time.strftime('%Y年%m月%d日 %H时%M分%S秒', time.localtime())
            Logger.fileName = './%s/%s.txt' % (path, t)

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
        s = '[LOG]: time=%s %s' % (Logger.getTime(), msg)
        print(s)
        Logger.write(s)

    @staticmethod
    def error(msg=''):
        s = '[WARN]: time=%s %s' % (Logger.getTime(), msg)
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
