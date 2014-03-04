'''
KTN-project 2013 / 2014
Python daemon thread class for listening for events on
a socket and notifying a listener of new messages or
if the connection breaks.

A python thread object is started by calling the start()
method on the class. in order to make the thread do any
useful work, you have to override the run() method from
the Thread superclass. NB! DO NOT call the run() method
directly, this will cause the thread to block and suspend the
entire calling process' stack until the run() is finished.
it is the start() method that is responsible for actually
executing the run() method in a new thread.
'''
from threading import Thread
import json


class ReceiveMessageWorker(Thread):

    def __init__(self, listener, connection):
        super(ReceiveMessageWorker, self).__init__()
        self.running = True
        self.daemeon = True
        self.listener = listener
        self.connection = connection

    def run(self):
        while self.running:
            data = self.connection.recv(1024)
            if not data:
                continue
            self.handle_data(data)

    def handle_data(self, data):
        self.listener.data_received(data)
