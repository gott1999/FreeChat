# -*- coding: UTF-8 -*-
import os
import socket
import threading

from app.logger import log
from app.logic import dispatcher

from app.config.serve import DEFAULT_INET
from app.config.serve import DEFAULT_BUFFER
from app.config.serve import DEFAULT_STREAM
from app.config.serve import DEFAULT_PORT
from app.config.serve import DEFAULT_CONNECT_POOL_SIZE
from app.config.serve import DEFAULT_UPLOAD_PATH
from app.config.serve import DEFAULT_IMAGE_UPLOAD_PATH


class Server:

    isListen = True

    @staticmethod
    def initFolders():
        """
        create folders for use
        :return:
        """
        if not os.path.exists(DEFAULT_UPLOAD_PATH):
            os.mkdir(DEFAULT_UPLOAD_PATH)
        if not os.path.exists(DEFAULT_IMAGE_UPLOAD_PATH):
            os.mkdir(DEFAULT_IMAGE_UPLOAD_PATH)

    @staticmethod
    def startServer(port=DEFAULT_PORT, pool=DEFAULT_CONNECT_POOL_SIZE):
        """
        start Server
        :param port: port
        :param pool: connect pool size
        :return: None
        """
        Server.initFolders()
        log.Logger.log("Ivp4 start server on port: %s" % port)
        skt = socket.socket(DEFAULT_INET, DEFAULT_STREAM)
        skt.bind(('', port))
        skt.listen(pool)
        while Server.isListen:
            conn, address = skt.accept()
            threading.Thread(target=Server.handle, args=(conn, address)).start()
        log.Logger.log("Ivp4 server closed")

    @staticmethod
    def handle(conn: socket, address):
        """
        handle request from 'conn'
        :param conn: connect
        :param address: address
        :return: None
        """
        try:
            total_data = Server.receive(conn, address)
            res = dispatcher.tick(total_data, address)
            conn.send(res)
            conn.send(b'#')
        except Exception as e:
            log.Logger.error("ip=%s:%s by='handle' handle error" % address)
            log.Logger.error(str(e))
        finally:
            conn.close()

    @staticmethod
    def receive(conn: socket, address):
        total_data = bytes()
        while True:
            try:
                sub_data = conn.recv(DEFAULT_BUFFER)
                if len(sub_data) > 0:
                    if sub_data[-1:] == b'#':
                        total_data += sub_data[:-1]
                        break
                    else:
                        total_data += sub_data
                else:
                    break
            except Exception as e:
                log.Logger.error("ip=%s:%s by='receiver' Socket receive data error" % address)
                raise e
        return total_data
