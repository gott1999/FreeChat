# -*- coding: UTF-8 -*-
from app.net.Listener import Server
import app.db.UrchatDb as db
import app.net.proto.Net_pb2 as net_pb2

rq = net_pb2.Request()
rq.method = net_pb2.Request.ConnectMethod.POST
rq.ip = '127.0.0.1'
rq.host = 66666
rq.request = 'login'
rq.type = net_pb2.Request.ConnectType.TEXT
b = rq.SerializeToString()
print(b)

rq2 = net_pb2.Request()
rq2.ParseFromString(b)
print(rq2.method)

#print(len(db.login('xiang','123')))


#Server.startServer('127.0.0.1')
