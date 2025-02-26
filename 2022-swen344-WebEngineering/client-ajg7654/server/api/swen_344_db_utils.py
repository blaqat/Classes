import psycopg2
import yaml
import os

def connect():
    config = {}
    yml_path = os.path.join(os.path.dirname(__file__), 'db.yml')
    with open(yml_path, 'r') as file:
        config = yaml.load(file, Loader=yaml.FullLoader)

    return psycopg2.connect(dbname=config['database'],
                            user=config['user'],
                            password=config['password'],
                            host=config['host'],
                            port=config['port'])

def exec_sql_file(path):
    full_path = os.path.join(os.path.dirname(__file__), f'{path}')
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
    #print("exec_commit:\n" + sql+"\n")
    conn = connect()
    cur = conn.cursor()
    result = cur.execute(sql, args)
    conn.commit()
    conn.close()
    return result

def convert_type(o):
    if not (isinstance(o, (str, bool, float, int, list, tuple, dict)) or o is None):
        o = str(o)
    elif isinstance(o, (list, tuple)):
        o = [convert_type(v) for v in o]
    elif isinstance(o, dict):
        for k in o:
            o[k] = convert_type(o[k])
    return o

def format_results(results, keys, id_key=None):
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