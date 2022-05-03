# -*- coding: UTF-8 -*-

import time
import os

path = 'log'


class Logger:
    file = None

    @staticmethod
    def init():
        if not os.path.exists(path):
            os.mkdir(path)
        t = time.strftime('%Y-%m-%d-%H-%M-%S', time.localtime())
        Logger.file = open('./%s/%s.txt' % (path, t), 'w')

    @staticmethod
    def close():
        Logger.file.close()
        Logger.file = None

    @staticmethod
    def write(msg):
        if Logger.file is None:
            Logger.init()
        if not msg.endswith('\n'):
            msg += '\n'
        Logger.file.write(msg)

    @staticmethod
    def log(msg=''):
        s = '[LOG]: %s' % msg
        print(s)
        Logger.write(s)

    @staticmethod
    def error(msg=''):
        s = '[WARN]: %s' % msg
        print(s)
        Logger.write(s)

    @staticmethod
    def warn(msg=''):
        s = '[WARN]: %s' % msg
        print(s)
        Logger.write(s)
