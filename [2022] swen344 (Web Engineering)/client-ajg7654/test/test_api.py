import unittest
import json
from .rest_utils import *

class TestExample(unittest.TestCase):
    def test_api(self):
        result = get_rest_call(self, 'http://localhost:5000/foods')
        print(result)
