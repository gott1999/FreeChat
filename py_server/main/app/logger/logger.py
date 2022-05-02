# -*- coding: UTF-8 -*-

class Logger:
    @staticmethod
    def log(msg=''):
        print('[LOG]: %s' % msg)

    @staticmethod
    def error(msg=''):
        print('[ERROR]: %s' % msg)

    @staticmethod
    def warn(msg=''):
        print('[WARN]: %s' % msg)

