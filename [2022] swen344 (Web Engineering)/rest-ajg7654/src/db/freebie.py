import datetime
from email.mime import base
from platform import release
from .swen344_db_utils import *


def rebuild_tables():
    exec_sql_file('src/db/schema.sql')

def get_muted_list(user_id):
    return exec_get("muted", "neighbor_b", "neighbor_a", user_id, ret=0)

def check_user_muted_by(uid_a, uid_b):
    muted = get_muted_list(uid_a)
    in_muted = False

    for m in muted:
        if uid_b in m:
            in_muted = True
            break

    return in_muted
    
def get_accounts():
    return exec_sql("SELECT * FROM neighbor", [], 'all')

def get_neighbor_analytics(user_id=None):
    anal = """
    SELECT 
        neighbor.name as username,
        count(muted.neighbor_b) as num_muted,
        STRING_AGG(na.name::character varying, ',') as muted_csv,
        CASE 
            WHEN count(wish.id)=0 
                THEN NULL
            ELSE 
                count(item.id)/count(wish.id)*1.0
            END as wish_to_post,
        count(wish.fulfilled_date) as unfullfiled
    FROM neighbor
    LEFT JOIN muted ON muted.neighbor_b=neighbor.id
    LEFT JOIN neighbor na ON na.id = muted.neighbor_a
    LEFT JOIN item ON neighbor.id = item.neighbor_id
    LEFT JOIN wish ON wish.neighbor_id = neighbor.id
    """ + user_id and "WHERE neighbor.id = %s" + """
    GROUP BY neighbor.name
    ORDER BY num_muted DESC
    """

    return exec_sql(anal, user_id and [user_id] or [])

def get_user_feeds(user_id=None):
    feeds = []
    user_ids = []
    id_count = exec_get_len('neighbor')

    if user_id is None:
        for i in range(1, id_count+1):
            user_ids.append(i)
    else:
        user_ids.append(int(user_id))

    for i in user_ids:
        if not (0 < i < id_count):
            return feeds
        else:
            feeds.append(
                [i, exec_get('neighbor', 'name', 'id', i)[0], get_user_feed(user_id=i)]
            )
        
    return feeds


def get_user_feed(user_id):
    date = "2022-02-07"
    [community_id, is_mod] = exec_get('neighbor', 'community_id, mod', 'id', user_id)
    

    #gets the lists of wishes that will show up for a user
    feed_command = """
    SELECT item.name, neighbor.name, wish.expiration_date FROM wish
    INNER JOIN item ON wish.id=item.wish_id
    JOIN neighbor ON neighbor.id=wish.neighbor_id
        WHERE
    (item.released=TRUE OR item.released IS NULL) AND (
    ((neighbor.id=%s) OR (%s=TRUE) OR
    (expiration_date >= date(%s) OR expiration_date IS NULL)) AND
    (date(%s) BETWEEN fulfilled_date AND fulfilled_date + INTERVAL '24 HOURS' OR fulfilled_date IS NULL)
    ) AND (
	neighbor.community_id=%s)"""

    
    base_arguments = [user_id, is_mod, date, date, community_id]

    feed = exec_sql(feed_command, base_arguments, 0)
    final_feed = []
    
    for v in feed:
        if(not check_user_muted_by(user_id, v[1])):
            final_feed.append(v)
    
    return final_feed

def get_community_analytics():
    anal = """
    SELECT 
        community.name as community,
        count(item.id) as num_posts,
        count(wish.id) as num_wishes, 
        count(neighbor.id) as num_members, 
        count(nearby.community_a) as nearby_communities,
        CASE 
            WHEN count(wish.id)=0 
                THEN NULL
            ELSE 
                count(item.id)/count(wish.id)*1.0
            END as wish_to_post,
        CASE 
            WHEN count(item.id)=0 
                THEN NULL
            ELSE 
                date_trunc('year',age(min(item.post_date)))
            END as age_years
    FROM community 
    LEFT JOIN neighbor ON neighbor.community_id = community.id
    LEFT JOIN item ON neighbor.id = item.neighbor_id
    LEFT JOIN wish ON wish.neighbor_id = neighbor.id
    LEFT JOIN nearby ON community.id=nearby.community_a
    GROUP BY community.name    
    """

    return exec_sql(anal, [], 'all')