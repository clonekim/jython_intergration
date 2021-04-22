# -*- coding: utf-8 -*-

from bonjour import CodeMaker


class Maker(CodeMaker):
    def __init__(self):
        self.desc = u"Maker 한글 0.1"

    def version(self):
        return self.desc
