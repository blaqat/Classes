from cgi import test
import unittest
import datetime
from numpy import testing
from src.freebiefinder import *
from src.swen344_db_utils import connect
from datetime import date



def insert_test_data_db1():
    rebuildTables()
    exec_sql_file('db-ajg7654/tests/insert_schema.sql')

def insert_test_data_db2():
    insert_test_data_db1()
    create_account("Bobbie", community="Rocinante")

def insert_test_data_db3():
    insert_test_data_db2()
    make_wish("Crisjen", "tea", date_posted="2022-02-06")
    make_item("Jules-Pierre", "Razorback", date_posted="2022-02-06", released=True)
    make_item("Jules-Pierre", "Guanshiyin", "2022-02-08", date_posted="2022-02-06")
    mute_neighbor("Crisjen", "Jules-Pierre")
    make_wish("Bobbie", "RocketShip", date_posted="2022-02-07")
    mark_nearby("Crisjen", "Rocinante")

def check_tables_built(self, cur):
    cur.execute('SELECT * FROM community')
    self.assertEqual([], cur.fetchall(), "no rows in community")
    cur.execute('SELECT * FROM neighbor')
    self.assertEqual([], cur.fetchall(), "no rows in neighbor")
    cur.execute('SELECT * FROM wish')
    self.assertEqual([], cur.fetchall(), "no rows in wish")
    cur.execute('SELECT * FROM comment')
    self.assertEqual([], cur.fetchall(), "no rows in comment")
    cur.execute('SELECT * FROM nearby')
    self.assertEqual([], cur.fetchall(), "no rows in nearby")
    cur.execute('SELECT * FROM muted')
    self.assertEqual([], cur.fetchall(), "no rows in muted")
    

class TestFreebieFinder(unittest.TestCase):

    def test_build_tables(self):
         # Build the tables
        conn = connect()
        cur = conn.cursor()
        rebuildTables()
        check_tables_built(self, cur)
        conn.close()

    def test_rebuild_tables_is_idempotent(self):
        #Drop and rebuild the tables twice
        rebuildTables()
        rebuildTables()
        conn = connect()
        cur = conn.cursor()
        check_tables_built(self, cur)
        conn.close()

    def test_db1_1(self):
        # Naomi checks her feed on 1/2/2022
        insert_test_data_db1()
        want_feed = [('spaceship', 'Alex', None), ('protomolecule', 'Holden', datetime.date(2022, 1, 24)), ('water', 'Naomi', datetime.date(2022, 1, 25))]
        real_feed = get_feed("Naomi", "2022-01-02")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_2(self):
        # Naomi checks her feed on 1/26/2022
        insert_test_data_db1()
        want_feed = [('spaceship', 'Alex', None),  ('water', 'Naomi', datetime.date(2022, 1, 25))]
        real_feed = get_feed("Naomi", "2022-01-26")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_3(self):
        # Holden checks his feed on 1/2/2022
        insert_test_data_db1()
        want_feed = [('spaceship', 'Alex', None), ('protomolecule', 'Holden', datetime.date(2022, 1, 24)), ('water', 'Naomi', datetime.date(2022, 1, 25))]
        real_feed = get_feed("Holden", "2022-01-02")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_4(self):
        # Holden checks his feed on 1/2/2022
        insert_test_data_db1()
        want_feed = [('spaceship', 'Alex', None), ('protomolecule', 'Holden', datetime.date(2022, 1, 24)), ('water', 'Naomi', datetime.date(2022, 1, 25))]
        real_feed = get_feed("Holden", "2022-01-26")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_5(self):
        # Miller checks his feed
        insert_test_data_db1()
        want_feed = [('whiskey', 'Miller', None)]
        real_feed = get_feed("Miller", "2022-01-26")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_make_wish(self):
        # Test if person can see wish after making it
        insert_test_data_db1()
        make_wish("Miller", "lava lamp", "2022-1-26")
        want_feed = [('whiskey', 'Miller', None), ('lava lamp', 'Miller', datetime.date(2022, 1, 26))]
        real_feed = get_feed("Miller", "2022-01-26")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_no_expiration_wish(self):
        # Test is null expiration wish works
        insert_test_data_db1()
        make_wish("Miller", "lava lamp")
        want_feed = [('whiskey', 'Miller', None), ('lava lamp', 'Miller', None)]
        real_feed = get_feed("Miller", "2022-01-26")
        testing.assert_array_equal(real_feed, want_feed)

    def test_db1_mod_see_mod(self):
        # Test if moderator can see other expired moderator posts
        insert_test_data_db1()
        make_wish('Alexis', 'radio', '2022-1-05')
        want_feed = [('whiskey', 'Miller', None), ('radio', 'Alexis', datetime.date(2022, 1, 5))]
        real_feed = get_feed("Miller", "2022-01-26")
        testing.assert_array_equal(real_feed, want_feed)

    
    def test_db2_1(self):
        insert_test_data_db1()
        create_account("Bobbie", community="Rocinante")

        real_result = simple_get('neighbor', 'id, name, community_id', 'name', 'Bobbie')
        want_result = (8, 'Bobbie', 1)

        testing.assert_array_equal(real_result, want_result)

    def test_db2_2(self):
        insert_test_data_db2()
        bobby_id = elevate_account("Holden", "Bobbie")

        want_result = ('Bobbie', 1, True)
        real_result = simple_get('neighbor', 'name, community_id, mod', 'id', bobby_id)

        testing.assert_array_equal(real_result, want_result)


    def test_db2_3(self):
        modify_account('Bobbie', community="Ceres")
        testing.assert_array_equal((2), simple_get('neighbor', 'community_id', 'name', 'Bobbie'))

        modify_account('Bobbie', birthday="2000-1-20", community="Rocinante")
        testing.assert_array_equal((1, date(2000, 1, 20)), simple_get('neighbor', 'community_id, birthday', 'name', 'Bobbie'))

        modify_account('Bobbie', name="Roberta")
        testing.assert_array_equal((8), simple_get('neighbor', 'id', 'name', 'Roberta'))

    def test_db2_4(self):
        insert_test_data_db2()
        make_wish("Bobbie", "Goliath")

        add_comment("Alex", "Goliath", "That guy was huge")
        testing.assert_array_equal([(1, 3, 5, 'That guy was huge')], get_comments('Goliath'))

        fullfill_wish("Bobbie", "Goliath", "2022-1-30")
        self.assertEqual(date(2022, 1, 30), simple_get('wish', 'fulfilled_date', 'id', simple_get('item', 'wish_id', 'name', 'Goliath')[0])[0])

        want = [('spaceship', 'Alex', None), ('Goliath', 'Bobbie', None)]
        got = get_feed("Bobbie", "2022-1-31")
        testing.assert_array_equal(got, want)

        want.pop()
        got = get_feed("Bobbie", "2022-2-1")
        testing.assert_array_equal(got, want)

    def test_db2_5(self):
        insert_test_data_db1()
        
        old_result = simple_get('wish','id', 'id', simple_get('item', 'wish_id', 'name', 'protomolecule'))[0]
        delete_post("Miller", 'protomolecule')
        new_result = simple_get('wish','id', 'id', simple_get('item', 'wish_id', 'name', 'protomolecule'))
        
        testing.assert_array_equal(3, old_result)
        self.assertEqual(None, new_result)

    def test_db2_6(self):
        insert_test_data_db1()

        wish_id = make_wish('Holden', 'Canterbury')
        add_comment('Alex', 'Canterbury', "I hate whatever this is")
        delete_post('Miller', 'Canterbury')
        
        get_deleted_post = simple_get('wish', 'id', 'id', wish_id)
        get_deleted_comm = simple_get('comment', 'id', 'wish_id', wish_id)
        self.assertEqual(get_deleted_post, get_deleted_comm)

    def test_db2_invalid_user(self):
        """
        Tests if uncreated user can make a post
        """
        insert_test_data_db1()

        wish_id = make_wish('Holden', 'Canterbury')
        try:
            add_comment('Amos', 'Canterbury', "I hate whatever this is")
        except AssertionError as e:
            pass
        else:
            raise AssertionError("This user shoud not be ble to make a commment as they don't exist")


    def test_db2_invalid_delete(self):
        """
        Tests if a non mod can delete a post
        """
        insert_test_data_db1()

        make_wish('Holden', 'Canterbury')
        add_comment('Alex', 'Canterbury', "I hate whatever this is")
        try:
            delete_post('Alex', 'Canterbury')
        except AssertionError as e:
            pass
        else:
            raise AssertionError("This user shouldn't have been able to delete a message as they are not a mod.")


    def test_db3_1(self):
        insert_test_data_db2()
        make_wish("Crisjen", "tea")
        testing.assert_array_equal(get_feed("Jules-Pierre", "2022-02-07", checkNearby=True), [('tea', 'Crisjen', None)])

    def test_db3_2(self):
        insert_test_data_db2()
        make_item("Jules-Pierre", "Razorback")
        testing.assert_array_equal(get_feed("Crisjen", "2022-02-07", True), tuple())
        update_item("Jules-Pierre", "Razorback", released=True)
        testing.assert_array_equal(get_feed("Crisjen", "2022-02-07", True), [('Razorback', 'Jules-Pierre', None)])
        

    def test_db3_3(self):
        insert_test_data_db2()
        make_item("Jules-Pierre", "Guanshiyin", "2022-02-08")
        testing.assert_array_equal(get_feed("Crisjen", "2022-02-07", True), tuple())
        testing.assert_array_equal(get_feed("Crisjen", "2022-02-08", True), [('Guanshiyin', 'Jules-Pierre', None)])
        

    def test_db3_4(self):
        mute_neighbor("Crisjen", "Jules-Pierre")
        assert get_feed("Crisjen", "2022-02-08", True)==[], "Expected Empty Array"
        

    def test_db3_5(self):
        insert_test_data_db2()
        make_wish("Bobbie", "RocketShip")
        assert get_feed("Crisjen", "2022-02-08", True) == [], "Expected empty array"
        mark_nearby("Crisjen", "Rocinante")
        has_bobby = False

        for item in get_feed("Crisjen", "2022-02-08", True):
            if item == ('RocketShip', 'Bobbie', None): has_bobby = True

        assert has_bobby==True, "Expected True"

    def test_db4_community_dashboard(self):
        """
        Reports how many posts wishes members cmmunities post ratio and age of a community
        """
        insert_test_data_db3()
        dashboard = get_community_analytics()
        want = [
            ('Ceres', 1, 1, 2, 0, 1.0, datetime.timedelta(0)), 
            ('Luna', 2, 0, 2, 0, None, datetime.timedelta(0)), 
            ('Rocinante', 4, 4, 4, 4, 1.0, datetime.timedelta(0)), 
            ('Earth', 2, 2, 2, 2, 1.0, datetime.timedelta(0))
            ]
        testing.assert_array_equal(dashboard, want)

    def test_db4_moderation_dashboard(self):
        """
        Reports how many times have been muted, who has muted them, post ratio, and unfulfilled posts for a neighbor
        """
        insert_test_data_db3()
        mute_neighbor("Alex", "Bobbie")
        dashboard = get_user_analytics("Holden")
        want = [
            ('Bobbie', 1, 'Alex', (1.0), 0), 
            ('Alex', 0, None, (1.0), 0), 
            ('Holden', 0, None, (1.0), 0), 
            ('Naomi', 0, None, (1.0), 0)
            ]
        testing.assert_array_equal(dashboard, want)

    def test_db4_moderation_dashboard_non_mod(self):
        """
        Same as previous except when a non mod does it
        """
        insert_test_data_db3()
        try:
            dashboard = get_user_analytics("Alex")
            #print(dashboard)
        except AssertionError as e:
            pass
        else:
            raise AssertionError("This user shouldn't have been able to check user dashboard as they are not a mod.")
