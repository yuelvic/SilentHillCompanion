from SimpleWebSocketServer import SimpleWebSocketServer, WebSocket

clients = []
class SimpleEcho(WebSocket):

    def handleMessage(self):
        # echo message back to clients
        for client in clients:
            if client != self:
                client.sendMessage(self.data)
        print(self.data)

    def handleConnected(self):
        clients.append(self)
        print(self.address, 'connected')

    def handleClose(self):
        print(self.address, 'closed')

server = SimpleWebSocketServer('', 8000, SimpleEcho)
server.serveforever()