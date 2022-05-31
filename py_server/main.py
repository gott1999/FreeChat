# -*- coding: UTF-8 -*-

from app.network.server import Server
import threading

t = threading.Thread(target=Server.startServer)

while True:
	if input() == "exit":
		break

Server.Stop()

