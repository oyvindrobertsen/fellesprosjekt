'''
Command Line Interface class
'''

import curses
import argparse
import random
from Client import Client
from curses import textpad


class ChatInterface(object):

    def __init__(self, host, port, username):
        self.host = host
        self.port = port
        self.username = username
        self.client = Client()

        self.stdscr = None
        curses.wrapper(self.main)

    def curses_setup(self):
        curses.curs_set(0)

        self.main_win = curses.newwin(60, 134, 0, 0)
        self.main_win.border('|', '|', '-', '-', '+', '+', '+', '+')
        self.main_win.refresh()
        
        self.text_win = curses.newwin(3, 134, 61, 0)
        #self.text_win.border('|', '|', '-', '-', '+', '+', '+', '+')
        self.input_box = textpad.Textbox(self.text_win)
        self.text_win.refresh()
        return

    def main(self, stdscr):
        self.stdscr = stdscr
        self.curses_setup()
        while True:
            self.input_box.edit()
            self.main_win.addstr(0,0, self.input_box.gather())
            self.main_win.refresh()

def get_args():
    parser = argparse.ArgumentParser(description='JSON based chat.',
                                     prog='ChatInterface')
    parser.add_argument('-u', '--user',
                        default='Anonymous' + str(int(random.random() * 1000)),
                        help='Your desire')
    parser.add_argument('--host', default='localhost',
                        help='Chat host server IP address')
    parser.add_argument('-p', '--port', default='9999',
                        help='Host port number')
    return parser.parse_args()


if __name__ == '__main__':
    args = get_args()
    chat_interface = ChatInterface(args.host, args.port, args.user)
