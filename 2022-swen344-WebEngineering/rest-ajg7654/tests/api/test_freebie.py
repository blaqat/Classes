import unittest
from tests.test_utils import *
from src.db.freebie import rebuild_tables

class TestFreebie(unittest.TestCase):

    def setUp(self):
        rebuild_tables()
        insert_test_data()

    def test_communities(self):
        expected = ["Ceres", "Luna", "Rocinante", "Earth"]
        actual = get_rest_call(self, 'http://localhost:5000/communities')
        self.assertEqual(expected, list(actual.keys()))

    def test_accounts(self):
        actual = len(get_rest_call(self, 'http://localhost:5000/accounts'))
        expected = 7
        self.assertEqual(expected, actual)

    def test_feed(self):
        actual = list(get_rest_call(self, 'http://localhost:5000/feed').keys())
        expected = ['Holden','Naomi','Alex','Miller','Alexis','Crisjen']
        self.assertEqual(expected, actual)

        actual = list(get_rest_call(self, 'http://localhost:5000/feed?user_id=1').keys())
        expected = ['Holden']
        self.assertEqual(expected, actual)
       