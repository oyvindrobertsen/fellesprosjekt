'''
KTN-project 2013 / 2014
'''
import socket
import json
from MessageWorker import ReceiveMessageWorker

class Client(object):

    def __init__(self, listener):
        self.listener = listener
        self.connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.logged_in = False
        self.messages = []

    def start(self, host, port):
        self.connection.connect((host, port))
        self.rec_worker = ReceiveMessageWorker(self, self.connection)
        self.rec_worker.start()

    def message_received(self, message):
        parsed = json.loads(message)
        self.messages.append = parsed
        self.interface.print_messages(self.messages)

    def login_received(self, response):
        if 'messages' in response.keys():
            self.logged_in = True
            for m in response['messages']:
                self.messages.append(m['message'])
        elif 'error' in response.keys():
            self.messages.append(response['error'])

    def logout_received(self, response):
        self.logged_in = False
        self.connection_closed()

    def connection_closed(self):
        self.connection.close()
        self.rec_worker.running = False
        self.listener.running = False

    def send(self, data):
        self.connection.sendall(data)

    def force_disconnect(self):
        self.connection_closed() 

    def login(self, username):
        self.send(json.dumps({'request': 'login', 'username': username}))
    
    def logout(self):
        self.send(json.dumps({'request': 'logout'}))

    def send_message(self, message):
        self.send(json.dumps({'request': 'message', 'message': message}))

    def data_received(self, data):
        parsed = json.loads(data)
        if parsed['response'] == 'login':
            self.login_received(parsed)
        elif parsed['response'] == 'logout':
            self.logout_received(parsed)
        
