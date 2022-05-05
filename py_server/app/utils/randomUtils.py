# -*- coding: UTF-8 -*-

import math
import random

str_pool = "qwertyuiopasdfghjklzxcvbnm1234567890"


def getStandard():
    return getRandom(16)


def getRandom(length):
    res = ''
    for i in range(0, length):
        s = str_pool[random.randint(0, len(str_pool) - 1)]
        up = random.random()
        if up < 0.5:
            res += s.upper()
        else:
            res += s
    return res