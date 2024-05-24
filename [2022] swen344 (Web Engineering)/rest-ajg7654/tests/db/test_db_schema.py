import unittest
from src.db.freebie import *
from src.db.swen344_db_utils import connect
from tests.test_utils import *

class TestDBSchema(unittest.TestCase):

    def test_rebuild_tables(self):
        """Rebuild the tables"""
        rebuild_tables()
        assert_sql_count(self, "SELECT * FROM community", 0)
        assert_sql_count(self, "SELECT * FROM neighbor", 0)
        assert_sql_count(self, "SELECT * FROM wish", 0)
        assert_sql_count(self, "SELECT * FROM item", 0)
        assert_sql_count(self, "SELECT * FROM muted", 0)
        assert_sql_count(self, "SELECT * FROM nearby", 0)

    def test_rebuild_tables_is_idempotent(self):
        """Drop and rebuild the tables twice"""
        rebuild_tables()
        rebuild_tables()
        assert_sql_count(self, "SELECT * FROM community", 0)
        assert_sql_count(self, "SELECT * FROM neighbor", 0)
        assert_sql_count(self, "SELECT * FROM wish", 0)
        assert_sql_count(self, "SELECT * FROM item", 0)
        assert_sql_count(self, "SELECT * FROM muted", 0)
        assert_sql_count(self, "SELECT * FROM nearby", 0)

    def test_seed_data_works(self):
        """Attempt to insert the seed data"""
        rebuild_tables()
        insert_test_data()
        assert_sql_count(self, "SELECT * FROM community", 4)
        assert_sql_count(self, "SELECT * FROM neighbor", 7)
        assert_sql_count(self, "SELECT * FROM wish", 4)
        assert_sql_count(self, "SELECT * FROM item", 4)
        assert_sql_count(self, "SELECT * FROM muted", 0)
        assert_sql_count(self, "SELECT * FROM nearby", 1)