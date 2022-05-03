# -*- coding: UTF-8 -*-

import socket
import threading

from app.logger import log

DEFAULT_PORT = 25565
DEFAULT_CONNECT_POOL_SIZE = 5


class Server:

    @staticmethod
    def startServer(host=socket.gethostname(), port=DEFAULT_PORT, pool=DEFAULT_CONNECT_POOL_SIZE):
        log.Logger.log('start server on %s:%s' % (host, port))
        skt = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        skt.bind((host, port))
        skt.listen(pool)
        while True:
            conn, address = skt.accept()
            print(conn)
            threading.Thread(target=handleRequest, args=(conn, address)).start()


def handleInput(s):
    if s == 'exit':
        log.Logger.log('closing server')
        log.Logger.close()
        return False
    return True


def handleRequest(conn, address):
    from proto import netprotoc_pb2 as protoc

    data = conn.recv()
    np = protoc.Protocol()
    np.ParseFromString(data)
    print(np)
