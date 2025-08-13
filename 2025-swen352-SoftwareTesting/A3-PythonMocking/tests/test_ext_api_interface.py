import os
import json
import requests
import unittest
from pathlib import Path
from unittest.mock import patch
from library import ext_api_interface


TESTS_DATA_DIR = Path(os.path.join(os.path.dirname(__file__), "../tests_data"))


class TestExtApiInterface(unittest.TestCase):
    def setUp(self):
        self.api = ext_api_interface.Books_API()
        self.book = "Learning Python"
        self.author = "David Ascher"
        with open(TESTS_DATA_DIR / "ebooks.txt", "r") as f:
            self.books_data = json.loads(f.read())
        with open(TESTS_DATA_DIR / "json_data.txt", "r") as f:
            self.json_data = json.loads(f.read())
        with open(TESTS_DATA_DIR / "david_ascher_data.txt", "r") as f:
            self.author_data = json.loads(f.read())
        with open(TESTS_DATA_DIR / "book_info.txt", "r") as f:
            self.book_info = json.loads(f.read())

    @patch("requests.get")
    def test_make_request_success(self, mock_get):
        mock_get.return_value.status_code = 200
        mock_get.return_value.json.return_value = dict()
        self.assertEqual(self.api.make_request("url"), dict())
        # Should have tested that requests.get and response.json were called correctly

    @patch("requests.get")
    def test_make_request_connection_error(self, mock_get):
        mock_get.side_effect = requests.exceptions.ConnectionError
        self.assertEqual(self.api.make_request("url"), None)

    @patch("requests.get")
    def test_make_request_failure(self, mock_get):
        mock_get.return_value.status_code = 100
        self.assertEqual(self.api.make_request("url"), None)

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_is_book_available(self, mock_request):
        mock_request.return_value = self.json_data
        self.assertTrue(self.api.is_book_available(self.book))

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_is_book_not_available(self, mock_request):
        mock_request.return_value = None
        self.assertFalse(self.api.is_book_available(self.book))

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_books_by_author(self, mock_request):
        mock_request.return_value = self.author_data
        self.assertIn(self.book, self.api.books_by_author(self.author))

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_books_by_author_req_failure(self, mock_request):
        mock_request.return_value = None
        self.assertEqual(self.api.books_by_author(self.author), [])

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_get_book_info_req_failure(self, mock_request):
        mock_request.return_value = None
        self.assertEqual(self.api.get_book_info(self.author), [])

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_get_book_info(self, mock_request):
        mock_request.return_value = self.json_data
        self.assertEqual(self.api.get_book_info(self.book), self.book_info)

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_get_ebooks_req_failure(self, mock_request):
        mock_request.return_value = None
        self.assertEqual(self.api.get_ebooks(self.book), [])

    @patch("library.ext_api_interface.Books_API.make_request")
    def test_get_ebooks(self, mock_request):
        mock_request.return_value = self.json_data
        self.assertEqual(self.api.get_ebooks(self.book), self.books_data)
