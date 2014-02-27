'''
KTN-project 2013 / 2014
Very simple server implementation that should serve as a basis
for implementing the chat server
'''
import SocketServer
import json

'''
The RequestHandler class for our server.

It is instantiated once per connection to the server, and must
override the handle() method to implement communication to the
client.
'''
curr_users = {}
message_log= []


class CLientHandler(SocketServer.BaseRequestHandler):

    def handle(self):
        # Get a reference to the socket object
        self.connection = self.request
        # Get the remote ip adress of the socket
        self.ip = self.client_address[0]
        # Get the remote port number of the socket
        self.port = self.client_address[1]
        print 'Client connected @' + self.ip + ':' + str(self.port)
        # Wait for data from the client
        while True:
            data = self.connection.recv(1024).strip()

            if not data:
                continue

            parsed = json.loads(data)
            if parsed['request'] == 'login':
                curr_users[parsed['username']] = self.request
                self.respond_login(parsed)
            elif parsed['request'] == 'logout':
                self.respond_logout(parsed)
            elif parsed['request'] == 'message':
                self.respond_message(parsed)

    def respond_login(self, data_dict):
        print "Responded to login request, username: " + data_dict['username']  
        self.connection.sendall(json.dumps({'response': 'login',
                                            'username': data_dict['username'],
                                            'messages': message_log}))

    def resond_logout(self, data_dict):
        pass
        

'''
This will make all Request handlers being called in its own thread.
Very important, otherwise only one client will be served at a time
'''
class ThreadedTCPServer(SocketServer.ThreadingMixIn, SocketServer.TCPServer):
    pass

if __name__ == "__main__":
    HOST = 'localhost'
    PORT = 9999

    # Create the server, binding to localhost on port 9999
    server = ThreadedTCPServer((HOST, PORT), CLientHandler)

    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()


