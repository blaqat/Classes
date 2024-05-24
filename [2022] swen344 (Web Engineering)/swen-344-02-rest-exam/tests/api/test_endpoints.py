import unittest

from numpy import True_
from tests.test_utils import *

class TestEndpointExample(unittest.TestCase):

    def test_example_endpoint(self):
        actual = get_rest_call(self, URL % "")
        assert(len(actual) == 15)

    def test_questions(self):
        # 1. Edit Jegbert's bleat (13)
        put_rest_call(self, URL % "bleats/13", {'content':"game bro is a joke and we both know it."})
        print_question(self, 1, "Edit Jegbert's bleat", "bleats?id=13")

        # 2. Get SansUndertale's user profile info
        print_question(self, 2, "Get SansUndertale's info", "users/SansUndertale")
        
        # 3. Edit SansUndertale's bleat (3)
        put_rest_call(self, URL % "bleats/3", {'content':"ok, i will send someone over to fix it.", 'replace':False})
        print_question(self, 3, "Edit SansUndertale's bleat", "bleats?id=3")

        # 4. List all bleats by Jegbert
        print_question(self, 4, "List all bleats by Jegbert", "bleats/Jegbert")

        # 5. List all bleats by SansUndertale
        print_question(self, 5, "List all bleats by SansUndertale", "bleats/SansUndertale")

        # 6. Delete Boba's bleat with ID 8 and Jeffie's with ID 9
        delete_rest_call(self, URL % "bleats?id=8")
        delete_rest_call(self, URL % "bleats?id=9")
        print_question(self, 6, "Deleting bleat id 8 and 9", "", True)

        # 7. List all bleats by Boba and Jeffie
        jeffie_bleats = get_rest_call(self, URL % "bleats/Jeffie")
        boba_bleats = get_rest_call(self, URL % "bleats/Boba")
        print_question(self, 7, "List all bleats by Boba and Jeffie", "Bobas:" + str(boba_bleats) + "\n Jeffies:" + str(jeffie_bleats), True)

        # 8. Have SarahZ post a new bleat
        post_rest_call(self, URL % "bleats", {'user_id':1, 'content':"No burg is complete without cheese"})
        print_question(self, 8, "Ading SarahZ bleat", "", True)

        # 9. List all bleats by SarahZ
        print_question(self, 9, "List all bleats by SarahZ", "bleats/SarahZ")


