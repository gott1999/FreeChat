# -*- coding: UTF-8 -*-
import os
import socket
import threading
import time

from app.logger import log
from app.logic import users
from app.utils import randomUtils
from proto import protocol_pb2 as protoc

DEFAULT_PORT = 25565
DEFAULT_CONNECT_POOL_SIZE = 5


class Server:
    @staticmethod
    def initFolders():
        """
        create folders for use
        :return:
        """
        if not os.path.exists('upload'):
            os.mkdir('upload')
        if not os.path.exists('upload/icon'):
            os.mkdir('upload/icon')
        if not os.path.exists('upload/img'):
            os.mkdir('upload/img')

    @staticmethod
    def startServer(port=DEFAULT_PORT, pool=DEFAULT_CONNECT_POOL_SIZE):
        """
        start Server
        :param port: port
        :param pool: connect pool size
        :return: None
        """
        Server.initFolders()
        log.Logger.log("start server on port: %s" % port)
        skt = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        skt.bind(('', port))
        skt.listen(pool)
        while True:
            conn, address = skt.accept()
            threading.Thread(target=Server.handleRequest, args=(conn, address)).start()

    @staticmethod
    def handleRequest(conn: socket, address):
        """
        handle request from 'conn'
        :param conn: connect
        :param address: address
        :return: None
        """
        try:
            data = conn.recv(1024)
            np = protoc.Protocol()
            np.ParseFromString(data)
            log.Logger.log("Client=%s:%s" % address + " method=" + np.pairs["method"])
            rt = tick(np)
            conn.send(rt.SerializeToString())
        except Exception as e:
            log.Logger.error(e.__str__())
        finally:
            conn.close()


def tick(np: protoc.ConnectType):
    """
    dispatch the request
    :param np: request protocol
    :return: response protocol
    """
    res = protoc.Protocol()
    res.type = protoc.ConnectType.RESPONSE
    res.valid = True

    if np.pairs["method"] == "LOGIN":
        res.pairs["method"] = "LOGIN"
        tickLogin(np, res)
    elif np.pairs["method"] == "REGISTER":
        res.pairs["method"] = "REGISTER"
        tickRegister(np, res)
    else:
        res.valid = False
    res.millisecond_timestamp = round(time.time() * 1000)
    return res


def tickLogin(np, res):
    """
    tick login
    :param np: request protocol
    :param res: response protocol
    :return: None
    """
    rt = users.login(np.pairs["uname"], np.pairs["upassword"])
    if rt is None:
        res.valid = False
    else:
        res.pairs["uid"] = str(rt["uid"])
        res.pairs["displayName"] = rt["displayName"]
        if rt["icon"]:
            res.pairs["icon"] = rt["icon"]
            res.bits["icon"] = ""
        res.pairs["key"] = randomUtils.getStandard()


def tickRegister(np, res):
    """
    tick register
    :param np: request protocol
    :param res: response protocol
    :return: None
    """
    rt = users.register(np.pairs["uname"], np.pairs["upassword"])
    if rt is None:
        res.valid = False
    else:
        res.pairs["uid"] = str(rt["uid"])
        res.pairs["displayName"] = rt["displayName"]
        if rt["icon"]:
            res.pairs["icon"] = rt["icon"]
            res.bits["icon"] = ""
        res.pairs["key"] = randomUtils.getStandard()
