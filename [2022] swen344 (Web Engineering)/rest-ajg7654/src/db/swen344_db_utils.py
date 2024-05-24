import psycopg2
import yaml
import os

def connect():
    config = {}
    yml_path = os.path.join(os.path.dirname(__file__), '../../config/db.yml')
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

def exec_get_len(table_name):
    conn = connect()
    cur = conn.cursor()
    cur.execute("SELECT count(*) FROM %s" % table_name)
    ret = cur.fetchone()[0]
    conn.commit()
    conn.close()
    return ret    

def exec_get(table_name, ret_key, k, v, ret=1):
    conn = connect()
    cur = conn.cursor()
    cur.execute("SELECT %s FROM %s WHERE %s" % (ret_key, table_name, k) + " = %s", [v])
    ret = (ret=='one' or ret == 1 and cur.fetchone or cur.fetchall)()
    conn.commit()
    conn.close()
    return ret

def exec_delete(table_name, k, v):
    conn = connect()
    cur = conn.cursor()
    cur.execute("DELETE FROM %s WHERE %s" % (table_name, k) + " = %s", [v])
    conn.commit()
    conn.close()

def exec_create(table_name, ret_key="id", **kwargs):
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

def exec_append(table_name, **kwargs):
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

def exec_update(table_name, where, **kwargs):
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

def exec_sql(command, args, ret=None):
    conn = connect()
    cur = conn.cursor()
    cur.execute(command, args)
    retur = None
    if(ret is not None):
        retur = (ret=='one' or ret==1 and cur.fetchone or cur.fetchall)()
    
    conn.commit()
    conn.close()

    return retur

def convert_type(o):
    if not (isinstance(o, (str, bool, float, int, list, tuple, dict)) or o is None):
        o = str(o)
    elif isinstance(o, (list, tuple)):
        o = [convert_type(v) for v in o]
    elif isinstance(o, dict):
        for k in o:
            o[k] = convert_type(o[k])
    return o

def format_results(results, keys, id_key):
    res = dict()
    c = 0
    i = None
    for r in results:
        r = [convert_type(v) for v in r]
        dict_r = dict(zip(keys, r))
        
        if id_key:
            i = dict_r.pop(id_key)
        res[id_key and i or c] = dict_r
        c+=1
    return res