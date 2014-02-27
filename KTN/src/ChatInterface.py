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

        self.NR_OF_MESSAGES_TO_DISPLAY = 5

        self.stdscr = None
        curses.wrapper(self.main)

    def main(self, stdscr):
        self.stdscr = stdscr
        curses.echo()
        self.client.login(self.username)
        time.sleep(2) #delay, in case of poor connection

        while self.running:
            if self.client.logged_in:
                self.stdscr.clear()
                self.stdscr.addstr(0, 0, 'Logged in!')
                self.stdscr.refresh()
                self.stdscr.getstr()
            else:
                self.stdscr.clear()
                self.stdscr.addstr(0, 0, '--> Please enter user name to log in:')
                self.stdscr.move(1,0)
                username = self.stdscr.getstr()
                self.client.login(username)
                time.sleep(2)
                



    def print_messages(self, messages):
        for i in xrange(len(messages)):
            self.stdscr.addstr(i, 0, messages[i])
        self.stdscr.refresh()
        self.stdscr.getch()

def get_args():
    parser = argparse.ArgumentParser(description='JSON based chat.',
                                     prog='ChatInterface')
    parser.add_argument('-u', '--user',
                        default='Anonymous' + str(int(random.random() * 1000)),
                        help='Your desired username')
    parser.add_argument('--host', default='localhost',
                        help='Chat host server IP address')
    parser.add_argument('-p', '--port', default='9999',
                        help='Host port number')
    return parser.parse_args()


if __name__ == '__main__':
    args = get_args()
    chat_interface = ChatInterface(args.host, args.port, args.user)
