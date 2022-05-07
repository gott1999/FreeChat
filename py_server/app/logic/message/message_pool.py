# -*- coding: UTF-8 -*-

from proto import user_message_pb2 as msg


class MessagePool:

    def __init__(self):
        self.messagePool = {str: []}

    def pushImg(self, src: str, des: str, img: str, millisecond_timestamp: int):
        """
        push img
        :param src: src uid
        :param des: des uid
        :param img: img name
        :param millisecond_timestamp:
        :return:
        """
        m = MessagePool.buildDefault(src, des, img, millisecond_timestamp)
        m.type = msg.MessageType.IMAGE
        self.push(m, des)

    def pushText(self, src: str, des: str, text: str, millisecond_timestamp: int):
        """
        push text
        :param src: src uid
        :param des: des uid
        :param text: text
        :param millisecond_timestamp:
        :return:
        """
        m = MessagePool.buildDefault(src, des, text, millisecond_timestamp)
        m.type = msg.MessageType.TEXT
        self.push(m, des)

    def push(self, des: str, m: msg.UserMessage):
        if des in self.messagePool:
            self.messagePool[des].append(m)
        else:
            self.messagePool[des] = [m]


    def pop(self, des: str):
        if des in self.messagePool:
            return self.messagePool.pop(des)
        else:
            return None

    def glance(self, des: str):
        if des in self.messagePool:
            return len(self.messagePool[des])
        else:
            return 0

    @staticmethod
    def buildDefault(src: str, des: str, text: str, millisecond_timestamp: int):
        message = msg.UserMessage()
        message.src = src
        message.des = des
        message.message = text
        message.millisecond_timestamp = millisecond_timestamp
        return message


class MessageManager:

    manager = MessagePool()

    ipPool = {str: str}

    @staticmethod
    def serialize(m: msg.UserMessage):
        return m.SerializeToString()

    @staticmethod
    def deserialize(b: bytes):
        t = msg.UserMessage()
        try:
            t.ParseFromString(b)
        except Exception as e:
            print(str(e))
        return t
