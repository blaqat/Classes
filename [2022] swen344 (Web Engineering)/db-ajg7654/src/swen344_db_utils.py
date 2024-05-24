from distutils.core import run_setup
import psycopg2
import yaml
import os

def connect():
    config = {}
    yml_path = os.path.join(os.path.dirname(__file__), '../config/db.yml')
    with open(yml_path, 'r') as file:
        config = yaml.load(file, Loader=yaml.FullLoader)
    return psycopg2.connect(dbname=config['database'],
                            user=config['user'],
                            password=config['password'],
                            host=config['host'],
                            port=config['port'])

def exec_sql_file(path):
    full_path = os.path.join(os.path.dirname(__file__), f'../../{path}')
    conn = connect()
    cur = conn.cursor()
    with open(full_path, 'r') as file:
        cur.execute(file.read())
    conn.commit()
    conn.close()

def exec_get_one(sql, args={}):
    conn = connect()
    cur = conn.cursor()
    cur.execute(sql, args)
    one = cur.fetchone()
    conn.close()
    return one

def exec_get_all(sql, args={}):
    conn = connect()
    cur = conn.cursor()
    cur.execute(sql, args)
    # https://www.psycopg.org/docs/cursor.html#cursor.fetchall
    list_of_tuples = cur.fetchall()
    conn.close()
    return list_of_tuples

def exec_commit(sql, args={}):
    conn = connect()
    cur = conn.cursor()
    result = cur.execute(sql, args)
    conn.commit()
    conn.close()
    return result

def simple_len(table_name):
    """
    Gets the count(*) from a table

    Keyword arguments:
    :param table_name -- The name of the table

    :return: The length of the table
    """
    conn = connect()
    cur = conn.cursor()
    cur.execute("SELECT count(*) FROM %s" % table_name)
    ret = cur.fetchone()[0]
    conn.commit()
    conn.close()
    return ret    

def simple_get(table_name, ret_key, k, v, ret=1):
    """
    Gets the value from a property in a table

    Keyword arguments:
    :param table_name -- The name of the table
    :param ret_key -- The key of the property that will be returned
    :param k -- The key of the property (WHERE statement)
    :param v -- The value of the key of the property
        WHERE k = v

    :return: The value from the property (ret_key) of the table
    """
    conn = connect()
    cur = conn.cursor()
    #print("SELECT %s FROM %s WHERE %s" % (ret_key, table_name, k) + " = %s", v)
    cur.execute("SELECT %s FROM %s WHERE %s" % (ret_key, table_name, k) + " = %s", [v])
    ret = (ret==1 and cur.fetchone or cur.fetchall)()
    conn.commit()
    conn.close()

    return ret

def simple_delete(table_name, k, v):
    """
    Deletes an item from a table

    Keyword arguments:
    :param table_name -- The name of the table
    :param k -- The key of the property (WHERE statement)
    :param v -- The value of the key that will be deleted from the table (WHERE statement)
    WHERE k = v
    """
    conn = connect()
    cur = conn.cursor()
    #print("DELETE FROM %s WHERE %s" % (table_name, k) + " = %s", v)
    cur.execute("DELETE FROM %s WHERE %s" % (table_name, k) + " = %s", [v])
    conn.commit()
    conn.close()

def simple_create(table_name, ret_key="id", **kwargs):
    """
    Creates an item in a table

    Keyword arguments:
    :param table_name -- The name of the table
    :param **kwargs -- The properties of the created item in key,value format

    :return: The ID of the new item
    """
    conn = connect()
    cur = conn.cursor()

    insert = """
        INSERT INTO %s""" % table_name + """(%s)""" % ", ".join(kwargs.keys()) + """
        VALUES("""  + ("%s, " * len(kwargs))[:-2] + ")" + """
        RETURNING %s
        """

    cur.execute(insert, [*list(kwargs.values()), ret_key])
    ret = cur.fetchone()

    conn.commit()
    conn.close()

    return ret

def simple_append(table_name, **kwargs):
    """
    Creates an item in a table

    Keyword arguments:
    :param table_name -- The name of the table
    :param **kwargs -- The properties of the created item in key,value format

    :return: The ID of the new item
    """
    conn = connect()
    cur = conn.cursor()
    cur.execute("SELECT count(*) FROM %s" % table_name)
    size = cur.fetchone()[0]

    insert = """
        INSERT INTO %s""" % table_name + """(id, %s)""" % ", ".join(kwargs.keys()) + """
        VALUES(%s, """  + ("%s, " * len(kwargs))[:-2] + ")" + """
        RETURNING id
        """

    cur.execute(insert, [size+1, *list(kwargs.values())])
    ret = cur.fetchone()

    conn.commit()
    conn.close()

    return ret

def simple_update(table_name, where, **kwargs):
    """
    Updates an item

    Keyword arguments:
    :param table_name -- The name of the table
    :param where -- the WHERE statement
    :param **kwargs -- The parameters that are being updated in key,value format 

    :return: The ID of the new updated item
    """
    conn = connect()
    cur = conn.cursor()

    update = """
        UPDATE %s SET
    """ % table_name

    for key, val in kwargs.items():
        update += ((key + " = %s" + ",") if val is not None else "")

    update = update[:-1] + """ WHERE %s
        RETURNING id
    """ % where

    cur.execute(update, [val for val in kwargs.values() if val is not None])
    updated_id = cur.fetchone()[0]

    conn.commit()
    conn.close()

    return updated_id

def simple_run(command, args, ret=None):
    """
    Runs a command
    """
    conn = connect()
    cur = conn.cursor()
    cur.execute(command, args)
    retur = None
    if(ret is not None):
        retur = (ret==1 and cur.fetchone or cur.fetchall)()
    
    conn.commit()
    conn.close()

    return retur