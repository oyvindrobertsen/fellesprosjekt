'''
Command Line Interface class
'''

import curses
import argparse
import random
import time
from Client import Client


class ChatInterface(object):

    def __init__(self, host, port, username):
        '''
        Sets up and initializes
        '''
        self.host = host
        self.port = port
        self.username = username
        self.running = True
        self.client = Client(self)
        self.client.start(self.host, int(self.port))

        self.NR_OF_MESSAGES_TO_DISPLAY = 15

        self.stdscr = None
        curses.wrapper(self.main)

    def main(self, stdscr):
        self.stdscr = stdscr
        curses.echo()
        self.client.login(self.username)
        time.sleep(1) #delay, in case of poor connection

        while self.running:
            if self.client.logged_in:
                self.stdscr.clear()
                self.display_messages_and_prompt()
            else:
                self.stdscr.clear()
                for i in xrange(len(self.client.messages)):
                    self.stdscr.addstr(i, 0, self.client.messages[i])
                self.stdscr.addstr(self.NR_OF_MESSAGES_TO_DISPLAY, 0, '--> Please enter user name to log in:')
                self.stdscr.move(self.NR_OF_MESSAGES_TO_DISPLAY + 1, 0)
                username = self.stdscr.getstr()
                self.client.login(username)
                time.sleep(2)

    def display_messages_and_prompt(self):
        if self.client.messages:
            for i in xrange(len(self.client.messages)):
                self.stdscr.addstr(i, 0, self.client.messages[i])
        self.stdscr.addstr(self.NR_OF_MESSAGES_TO_DISPLAY, 0, '--> Enter a message:')
        self.stdscr.move(self.NR_OF_MESSAGES_TO_DISPLAY + 1, 0)
        self.stdscr.refresh()
        message = self.stdscr.getstr()
        self.parse(message)

    def parse(self, message):
        if message == 'logout':
            self.client.logout()
        else:
            self.client.send_message('test')

def get_args():
    parser = argparse.ArgumentParser(description='JSON based chat.',
                                     prog='ChatInterface')
    parser.add_argument('-u', '--user',
                        default='',
                        help='Your desired username')
    parser.add_argument('--host', default='localhost',
                        help='Chat host server IP address')
    parser.add_argument('-p', '--port', default='9999',
                        help='Host port number')
    return parser.parse_args()


if __name__ == '__main__':
    args = get_args()
    chat_interface = ChatInterface(args.host, args.port, args.user)
