from flask_restful import Resource
from flask_restful import request
from flask_restful import reqparse
import json
from .swen_344_db_utils import *

parser = reqparse.RequestParser()
parser.add_argument('calories')
parser.add_argument('tot_fat')
parser.add_argument('sat_fat')
parser.add_argument('tran_fat')
parser.add_argument('protein')
parser.add_argument('carbs')
parser.add_argument('category_name')
parser.add_argument('food_name')
parser.add_argument('new_goal')

class Goal(Resource):
    def get(self, id=None):
        return exec_get_all("SELECT * FROM goals")

    def put(self, id):
        args=parser.parse_args();
        exec_commit(
            """
            UPDATE goals
            SET goal = %s
            WHERE id = %s
            """, 
            [args['new_goal'], id]
        )

class Foods(Resource):
    def get(self, id=None):
    # NOTE: No need to replicate code; use the util function!
       result = exec_get_all(
           """
           SELECT foods.id, foods.name, categories.name, calories, tot_fat, sat_fat, tran_fat, protein, carbs FROM foods
           LEFT JOIN categories ON categories.id = foods.category_id
           """
       )
       return result

    def put(self, id):
        args = parser.parse_args()
        exec_commit(
            """
            UPDATE foods
            SET calories='%s',tot_fat='%s',sat_fat='%s',tran_fat='%s',protein='%s',carbs='%s'
            WHERE id = %s
            """,
            [int(float(args['calories'])),float(args['tot_fat']),float(args['sat_fat']),float(args['tran_fat']),float(args['protein']),float(args['carbs']),id]
        )

    def delete(self, id):
        exec_commit(
            'DELETE FROM foods WHERE id = %s',
            [id]
        )

    def post(self, id=None):
        args = parser.parse_args()
        category_id = exec_get_one("SELECT id FROM categories WHERE name = %s", [args['category_name']])[0]

        exec_commit(
            """
            INSERT INTO foods(name, category_id, calories, tot_fat, sat_fat, tran_fat, protein, carbs) 
            VALUES(%s, %s, %s, %s, %s, %s, %s, %s);
            """,
            [args['food_name'], category_id, int(float(args['calories'])),float(args['tot_fat']),float(args['sat_fat']),float(args['tran_fat']),float(args['protein']),float(args['carbs'])]
        )