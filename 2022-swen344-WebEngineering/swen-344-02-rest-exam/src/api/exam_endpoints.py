from flask_restful import Resource, request
from db.swen344_db_utils import *

class ExampleEndpoint(Resource):
    def get(self):
        return exec_get_all('SELECT * FROM BLEATS')


class Users(Resource):
    def get(self, username=None):
        results = None

        if username is not None:
            results = exec_get_all('SELECT * FROM BLEAT_USERS WHERE USER_NAME = %s', [username])
        else:
            results = exec_get_all('SELECT * FROM BLEAT_USERS')
 
        return format_results(results, 
            ("id", "username", "email", "favorite_thing")
        )


class Bleats(Resource):
    def get(self, id=None, name=None):
        id = request.args.get("id") or id
        results = None

        if id is not None:
            results = exec_get_all('SELECT * FROM BLEATS WHERE ID = %s LIMIT 1', [id])
        elif name is not None:
            results = exec_get_all("""
            SELECT * FROM BLEATS 
            LEFT JOIN BLEAT_USERS ON BLEATS.USER_ID = BLEAT_USERS.ID
            WHERE BLEAT_USERS.USER_NAME = %s""", [name])
        else:
            results = exec_get_all("SELECT * FROM BLEATS")

        return format_results(results, 
            ("id", "user_id", "content")
        )

    def post(self):
        user_id = request.form['user_id']
        content = request.form['content']
        params = [user_id, content]

        if all(params):
            exec_commit("""
            INSERT INTO BLEATS ( USER_ID, BLEAT_CONTENT ) VALUES ( %s, %s );
            """, params)

    def put(self, id):
        content = request.form['content']
        replace = request.form.get('replace')

        if id is not None and content is not None:
            if replace == 'False':
                content = exec_get_one("SELECT BLEAT_CONTENT FROM BLEATS WHERE ID = %s", [id])[0] + content

            exec_commit("""
            UPDATE BLEATS 
            SET BLEAT_CONTENT = %s 
            WHERE ID = %s
            """, [content, id])


    def delete(self, id=None):
        id = request.args.get("id") or id
        if id is not None:
            exec_commit("""
            DELETE FROM BLEATS WHERE ID = %s
            """, [id])
