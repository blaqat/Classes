import datetime
from email.mime import base
from platform import release
from src.swen344_db_utils import *

def rebuildTables():
    exec_sql_file('db-ajg7654/src/init_schema.sql')

def mute_neighbor(user, neighbor):
    """
    Marks a neighbor as muted to user
    :param user -- The name of the user
    :param neighbor -- The name of the neighbor the user wants to mark as muted to
    """
    [user_id] = simple_get("neighbor", "id", "name", user)
    [neighbor_id] = simple_get("neighbor", "id", "name", neighbor)

    command = """
    INSERT INTO muted(neighbor_a, neighbor_b)
    VALUES(%s, %s)
    ON CONFLICT (neighbor_a, neighbor_b) DO NOTHING
    """

    simple_run(command, (user_id, neighbor_id))

def get_muted(user):
    """
    Gets a list of users muted by user 
    :param user -- Name of the user

    :return: a list of muted neighbors
    """
    [user_id] = simple_get("neighbor", "id", "name", user)
    return simple_get("muted", "neighbor_b", "neighbor_a", user_id, ret=0)

def check_muted(neighbor_a, neighbor_b):
    """
    Checks if a neighbor muted another
    :param community_a -- The first neighbor
    :param community_b -- The second neighbor
    
    :return: a boolean of whether or not the neighbor_b is muted by neighbor_a
    """
    muted = get_muted(neighbor_a)
    [neighbor_b_id] = simple_get("neighbor", "id", "name", neighbor_b)
    in_muted = False
    for m in muted:
        if neighbor_b_id in m:
            in_muted = True
            break

    return in_muted

def mark_nearby(user, community):
    """
    Marks a community as nearby to the community of a user
    :param user -- The name of the neighbor marking the community
    :param community_b -- The name of the community the neighbor wants to mark as near to
    """
    assert isa_mod(user), "This user (%s) does not have the ability to mark communities" % user
    community_a = simple_get("neighbor", "community_id", "name", user)
    community_b = simple_get("community", "id", "name", community)
    if is_nearby(community_a, community_b): return

    simple_create("nearby", ret_key="community_a", community_a=community_a, community_b=community_b)
    simple_create("nearby", ret_key="community_a", community_a=community_b, community_b=community_a)

def is_nearby(community_a, community_b):
    """
    Checks if two communities are nearby
    :param community_a -- The first community
    :param community_b -- The second community
    
    :return: a boolean of whether or not the communities are nearby
    """
    get = """
    SELECT community_a FROM nearby WHERE (community_a=%s AND community_b=%s) OR (community_a=%s AND community_b=%s)
    """
    got = simple_run(get, (community_a, community_b, community_b, community_a), 0)

    return len(got) > 0
    
def isa_mod(user):
    """
    Checks if a user is a moderator 

    Keyword arguments:
    :param user -- The name of the neighbor

    :return: a boolean whether or not a user is moderator
    """
    [mod] = simple_get('neighbor', 'mod', 'name', user)
    return mod == True

def create_account(name, community=None, birthday=None):
    """
    Creates an account with the given name

    Keyword arguments:
    :param name -- The name of the neighbor
    :param community -- The name of the community the new account is joining
    :param birthday -- The string of the birthay of the new account in YYYY-MM-DD format 

    :return: The ID of the new created account
    """
    
    simple_append('neighbor', community_id=None, name=name, mod='0', birthday=None)
    return modify_account(name, community=community, birthday=birthday)   

def modify_account(current_name, name=None, community=None, birthday=None):
    """
    Edits the information for a user

    Keyword arguments:
    :param current_name -- The name of the neighbor
    :param name -- The name the user is switching to
    :param community -- The name of the community the new account is switching to
    :param birthday -- The string of the birthay of the new account in YYYY-MM-DD format 

    :return: The ID of the changed user
    """
    nid = simple_get('neighbor', 'id', 'name', current_name)
    cid = community and simple_get('community', 'id', 'name', community) or None
    birthday = birthday and datetime.datetime.strptime(birthday, '%Y-%m-%d') or None

    return simple_update('neighbor', 'id = %s' % nid, name=name, community_id=cid, birthday=birthday)

def delete_account(name):
    """
    Deactivates an account

    Keyword arguments:
    :param name -- The name of the neighbor
    """
    nid = simple_get('neighbor', 'id', 'name', name)
    simple_update('neighbor', 'id=%s'%nid, active=False)

def elevate_account(user, neighbor_name):
    """
    Gives a neighbor moderator

    Keyword arguments:
    :param user -- The name of the user
    :param neighbor_name -- The name the user is giving moderator to

    :return: The ID of the changed user
    """
    assert isa_mod(user), "This user (%s) does not have the ability to elevate accounts" % user

    nid = simple_get('neighbor', 'id', 'name', neighbor_name)
    assert nid is not None, "This user (%s) does not exist!" % user
    changed_id = simple_update('neighbor', 'id=%s'%nid, mod='1')
    
    return changed_id

def add_comment(user, post_name, message):
    """
    Creates a comment on a wish

    Keyword arguments:
    :param user -- The name of the user
    :param post_name -- The name of the wish the comment is being made on
    :param message -- The textual content of the message

    :return: The ID of the created comment
    """

    nid = simple_get('neighbor', 'id', 'name', user)
    assert nid is not None, "This user (%s) does not exist!" % user
    wid = get_post(post_name)
    assert wid is not None, "This wish (%s) does not exist!" % post_name

    return simple_append('comment', neighbor_id=nid, wish_id=wid, message=message)

def get_comments(post_name):
    """
    Gets a list of comments made on a wish in chronological order

    Keyword arguments:
    :param post_name -- The name of the wish the comments are being retrieved from
    
    :return: List of comments
    """
    conn = connect()
    cur = conn.cursor()
    wid = get_post(post_name)
    assert wid is not None, "This wish (%s) does not exist!" % post_name

    cur.execute("SELECT * FROM comment WHERE wish_id = %s", [wid])
    comment = cur.fetchall()
    conn.close()
    return sorted(comment, key=lambda comment: comment[0])

def delete_comment(user, comment_id):
    """
    Deactivates a comment that was made on a post

    Keyword arguments:
    :param user -- The name of the user
    :param comment_id -- The id of the comment that is being deactivated
    
    :return: The ID of the edited comment
    """

    assert isa_mod(user), "This user (%s) does not have the ability to delete comments" % user
    return simple_update('comment', 'id%s'%comment_id, active=False)

def get_post(post_name):

    get = """
    SELECT wish.id FROM wish
    INNER JOIN item on wish.id = item.wish_id
    WHERE item.name = %s
    """

    return simple_run(get, [post_name], ret=1)

def delete_post(user, post_name):
    """
    Deletes a wish and all the comments tied to it

    Keyword arguments:
    :param user -- The name of the user
    :param post_name -- The name of the wish the comment is being made on
    
    """
    assert isa_mod(user), "This user (%s) does not have the ability to delete posts" % user
    
    wid = get_post(post_name)
    assert wid is not None, "This wish (%s) does not exist!" % post_name
    comments = get_comments(post_name)

    simple_delete('wish', 'id', wid)
    simple_run('UPDATE item SET wish_id=NULL WHERE wish_id=%s', [wid])
    for comment in comments:
        simple_delete('comment', 'id', comment[0])

def make_item(user, item_name, date_visible=None, wish_id=None, released=False, date_posted=None):
    """
    Creates an iteam

    Keyword arguments:
    :param user -- The name of the user
    :param item_name -- The name of the item being created
    :param date_vsible -- The date the item will become a wish
    :param wish_id -- The wish the item is connected to
    :released -- If the item has been released as a post
    :date_posted -- The date the item was posted
    """
    [neighbor_id] = simple_get('neighbor', 'id', 'name', user)
    item_id = simple_len("item")

    make = """
    INSERT INTO item(id, name, post_date, date_visible, neighbor_id, wish_id, released) VALUES 
    (%s, %s, %s, %s, %s, %s, %s)
    ON CONFLICT (name, wish_id) DO NOTHING
    RETURNING id
    """
    return simple_run(make, (item_id + 1, item_name, date_posted, date_visible, neighbor_id, wish_id, released), ret=1)

def make_wish(user, item_name, expiration_date=None, date_posted=None):
    """
    Creates a wish on a communities board

    Keyword arguments:
    :param user -- The name of the user
    :param item_name -- The name of the item on the wish
    :param expiration_date -- The date in YYYY-MM-DD format that the wish will expire on
    
    :return: The ID of the created wish
    """
    item_id = simple_get('item', 'id', 'name', item_name)
    wish_count = simple_len("wish")

    if(not item_id):
        item_id = make_item(user, item_name, date_posted=date_posted, wish_id=wish_count+1, released=True)
    else:
        update_item(user, item_name, wish_id = wish_count+1)

    expiration_date = expiration_date and datetime.datetime.strptime(expiration_date, '%Y-%m-%d') or None
    [neighbor_id] = simple_get('neighbor', 'id', 'name', user)

    return simple_append('wish', neighbor_id=neighbor_id, expiration_date=expiration_date, fulfilled_date=None)

def update_item(user, post_name, released=None, **kwargs):
    [uid, wid] = simple_get('item', 'neighbor_id, wish_id', 'name', post_name)
    assert simple_get('neighbor', 'id', 'name', user)[0]==uid, "This user (%s) did not make the item."%user

    if released==True and wid is None:
        make_wish(user, post_name)

    return simple_update('item', "name='%s'"%post_name, released=released, **kwargs)

def fullfill_wish(user, post_name, date):
    """
    Marks a wish as completed

    Keyword arguments:
    :param user -- The name of the user
    :param post_name -- The name of wish that is being fulfilled
    :param date -- The date that the wish is completed on (today)
    
    :return: The ID of the completed wish
    """
    post_id = get_post(post_name)
    assert simple_get('neighbor', 'name', 'id', simple_get('wish', 'neighbor_id', 'id', post_id)[0])[0] == user, "This neighbor (%s) didn't make this wish" % user
    date = datetime.datetime.strptime(date, '%Y-%m-%d')
    return simple_update('wish', "id='%s'" % post_id, fulfilled_date=date)

def get_feed(user, date, checkNearby=False):
    """
    Gets a list of wishes that a user should be able to see on their board

    Keyword arguments:
    :param user -- The name of the user
    :param date -- The date that the board is being viewed on (today)
    
    :return: A list of wishes
    """
    [user_id, community_id, is_mod] = simple_get('neighbor', 'id, community_id, mod', 'name', user)
    
    #gets the list of items that need to be created
    item_check = """
    SELECT item.id, item.name, neighbor.name FROM item 
    INNER JOIN neighbor ON item.neighbor_id=neighbor.id
    WHERE date_visible <= date(%s) AND released=FALSE 
    """

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
	neighbor.community_id=%s""" + (checkNearby and """ OR 
		EXISTS(SELECT * FROM nearby 
			   WHERE (nearby.community_a = neighbor.community_id AND nearby.community_b = %s 
					  OR nearby.community_a = %s AND nearby.community_b = neighbor.community_id)
			 )
	)
    """ or ")")

    new_wishes = simple_run(item_check, (date,), 0)
    for wish in (new_wishes if new_wishes else []):
        update_item(wish[2], wish[1], released=True)
    
    base_arguments = [user_id, is_mod, date, date, community_id]
    

    if checkNearby:
        base_arguments.append(community_id)
        base_arguments.append(community_id)

    feed = simple_run(feed_command, base_arguments, 0)
    final_feed = []
    
    for v in feed:
        if(not check_muted(user, v[1])):
            final_feed.append(v)
    
    return final_feed

def get_community_analytics():

    #This function gets all of the necessary community analytics (number of posts, wishes, members, wishes/items, how old it is)
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

    return simple_run(anal, [], 'all')

def get_user_analytics(user):

    [is_mod, community_id] = simple_get("neighbor", "mod, community_id", "name", user)

    assert is_mod, "This user (%s) does not have the ability to view user analytics" % user

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
    WHERE neighbor.community_id = %s
    GROUP BY neighbor.name
    ORDER BY num_muted DESC
    """

    return simple_run(anal, [community_id], 'all')