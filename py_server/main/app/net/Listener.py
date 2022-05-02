# -*- coding: UTF-8 -*-

import socket
import sys
import threading


DEFAULT_PORT = 25565
DEFAULT_CONNECT_POOL_SIZE = 5


class Server:
    @staticmethod
    def startServer(host=socket.gethostname(), port=DEFAULT_PORT, pool=DEFAULT_CONNECT_POOL_SIZE):
        log('Server start!')
        skt = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        skt.bind((host, port))
        skt.listen(pool)
        while True:
            conn, address = skt.accept()
            log('Client %s connect!' % conn)
            t = threading.Thread(target=tickRequest, args=(conn, address))
            t.start()


def encode(msg):
    return msg.encode('utf-8')


def decode(bits):
    return bits.decode('utf-8')


def log(msg):
    print('[LOG]' + msg)


def error(msg):
    print('[ERROR]' + msg)


def tickRequest(conn, address):
    try:
        data = conn.recv(1024)
        print(decode(data))
        conn.send(encode('hello world'))
    except ConnectionResetError as e:
        error('close connect %s' % e)
    finally:
        conn.close()


