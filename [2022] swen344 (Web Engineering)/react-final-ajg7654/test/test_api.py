import unittest
import json
from .rest_utils import *


class TestExample(unittest.TestCase):
    def edit_test(self):
        put_rest_call(self, 'http://localhost:5000/courses/9', 
        {'course_title':"Physics 100", 'course_number':"294", 'course_details':"Physucks"}
        )
        result = get_rest_call(self, 'http://localhost:5000/courses/9').get('0').get('num')
        self.assertEqual("294", result, "Course number should have been changed from 400 to 294")

    def delete_test(self):
        delete_rest_call(self, 'http://localhost:5000/courses/9')
        result2 = get_rest_call(self, 'http://localhost:5000/courses/9')
        self.assertEqual(result2, dict(), "Should have been deleted")
        
    def test_api(self):
        self.edit_test()
        self.delete_test()