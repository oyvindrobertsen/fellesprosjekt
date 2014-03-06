'''
KTN-project 2013 / 2014
Very simple server implementation that should serve as a basis
for implementing the chat server
'''
import SocketServer
import json
import re

'''
The RequestHandler class for our server.

It is instantiated once per connection to the server, and must
override the handle() method to implement communication to the
client.
'''
curr_users = {}
message_log= []
log_size = 15


class ClientHandler(SocketServer.BaseRequestHandler):

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
                if parsed['username'] in curr_users.keys() and parsed['username'] != None:
                    self.respond_login_taken(parsed)
                elif not re.match(r'^[a-zA-Z\_\d]{1,20}$', parsed['username']):
                    self.respond_login_invalid(parsed)
                else:
                    curr_users[parsed['username']] = self.request
                    self.username = parsed['username']
                    self.respond_login(parsed)
            elif parsed['request'] == 'logout':
                curr_users[self.username] = None
                self.respond_logout(parsed)
                break
            elif parsed['request'] == 'message':
                self.add_to_backlog(parsed)
                self.respond_message(parsed)
        self.connection.close()

    def respond_login(self, data_dict):
        print "Responded to successfull login request, username: " + data_dict['username']
        message_log.append({'response': 'message', 'message': data_dict['username'] + ' logged in.'})
        self.connection.sendall(json.dumps({'response': 'login',
                                            'username': self.username,
                                            'messages': message_log}))

    def respond_login_taken(self, data_dict):
        print self.ip + " tried to log in with an already taken username: "\
                + data_dict['username']
        self.connection.sendall(json.dumps({'response': 'login',
                                            'error': 'Name already taken!',
                                            'username': data_dict['username']}))

    def respond_login_invalid(self, data_dict):
        print self.ip + " tried to log in with an invalid username :"\
                + data_dict['username']
        self.connection.sendall(json.dumps({'response': 'login',
                                            'error': 'Invalid username!',
                                            'username': data_dict['username']}))

    def respond_logout(self, data_dict):
        print self.username + " logged out."
        self.connection.sendall(json.dumps({'response': 'logout', 'username': self.username}))

    def respond_message(self, data_dict):
        print self.username + '> ' + data_dict['message']
        message_log.append({'response': 'message', 'message': self.username + '>' + data_dict['message']})
        self.connection.sendall(json.dumps({'response': 'message', 'message': data_dict['message']}))

    def add_to_backlog(self, message):
        if len(message_log) < log_size:
            message_log.append(message)
            return
        message_log.pop(0)
        message_log.append(message)


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
    server = ThreadedTCPServer((HOST, PORT), ClientHandler)

    # Activate the server; this will keep running until you
    # interrupt the program with Ctrl-C
    server.serve_forever()


