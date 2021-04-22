import sys
from bonjour import CodeMaker

class PyMaker(CodeMaker):
    def __init__(self):
        self.desc = "jython 0.1"

    def version(self):
        return self.desc
