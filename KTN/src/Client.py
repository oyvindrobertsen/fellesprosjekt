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
        with self.listener.lock:
            self.messages.append(message['message'])
        self.listener.display_messages()

    def login_received(self, response):
        with self.listener.lock:
            if 'messages' in response.keys():
                self.logged_in = True
                for message in response['messages']:
                    self.messages.append(message)
            elif 'error' in response.keys():
                self.messages.append(response['error'])
        self.listener.display_messages()

    def logout_received(self, response):
        with self.listener.lock:
            self.logged_in = False
            self.connection_closed()
        self.listener.display_messages()

    def connection_closed(self):
        self.connection.close()
        self.rec_worker.running = False
        self.listener.running = False

    def send(self, data):
        self.connection.sendall(data)

    def send_message(self, message):
        self.send(json.dumps({'request': 'message', 'message': message}))

    def force_disconnect(self):
        self.connection_closed() 

    def login(self, username):
        self.send(json.dumps({'request': 'login', 'username': username}))
    
    def logout(self):
        self.send(json.dumps({'request': 'logout'}))

    def data_received(self, data):
        parsed = json.loads(data)
        if parsed['response'] == 'login':
            self.login_received(parsed)
        elif parsed['response'] == 'logout':
            self.logout_received(parsed)
        elif parsed['response'] == 'message':
            self.message_received(parsed)
