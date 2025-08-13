import os
import unittest
from library import patron


class TestPatron(unittest.TestCase):
    def setUp(self):
        self.pat = patron.Patron("fname", "lname", "20", "1234")

    def test_patron_invalid_name(self):
        with self.assertRaises(patron.InvalidNameException):
            patron.Patron("123", "123", "20", "1234")

    def test_patron_valid_name(self):
        pat = patron.Patron("fname", "lname", "20", "1234")
        self.assertIsInstance(pat, patron.Patron)

    def test_add_borrowed_book_already_borrowed(self):
        self.pat.add_borrowed_book("book")
        self.pat.add_borrowed_book("book")
        self.assertEqual(self.pat.get_borrowed_books(), ["book"])

    def test_add_borrowed_book(self):
        self.pat.add_borrowed_book("book")
        self.assertEqual(self.pat.get_borrowed_books(), ["book"])

    def test_return_borrowed_book(self):
        self.pat.add_borrowed_book("book")
        self.pat.return_borrowed_book("book")
        self.assertEqual(self.pat.get_borrowed_books(), [])

    # MARK: Getters Tests
    def test_get_borrowed_books(self):
        self.pat.borrowed_books = ["book", "anotherBook"]
        self.assertEqual(self.pat.get_borrowed_books(), ["book", "anotherBook"])

    def test_patron_neq(self):
        pat = patron.Patron("differentGuy", "lname", "20", "1234")
        self.assertNotEqual(self.pat, pat)

    def test_patron_eq(self):
        pat = patron.Patron("fname", "lname", "20", "1234")
        self.assertEqual(self.pat, pat)

    def test_get_first_name(self):
        self.assertEqual(self.pat.get_fname(), "fname")

    def test_get_last_name(self):
        self.assertEqual(self.pat.get_lname(), "lname")

    def test_get_age(self):
        self.assertEqual(self.pat.get_age(), "20")

    def test_get_member_id(self):
        self.assertEqual(self.pat.get_memberID(), "1234")
