from flask_restful import Resource
from flask_restful import reqparse
import json
from .swen_344_db_utils import *

parser = reqparse.RequestParser()
parser.add_argument('course_number')
parser.add_argument('course_title')
parser.add_argument('course_details')

class Courses(Resource):
    def get(self, id=None):
        results = exec_get_all(
            """
            SELECT courses.id, department.name, c_title, c_number, c_details, college.name FROM courses
            LEFT JOIN department ON department.id = courses.dept_id
            LEFT JOIN college ON department.college_id = college.id
            """ 
            + ( "WHERE courses.id = %s\n" if id is not None else 'ORDER BY courses.id' )
        , [id])

        return format_results(results, 
            ("id", "department", "title", "num", "details", "college")
        )

    def put(self, id):
        args = parser.parse_args()
        exec_commit(
            """
            UPDATE courses
            SET c_number = %s, c_title = %s, c_details = %s
            WHERE ID = %s
            """, 
            [args["course_number"], args["course_title"], args["course_details"], id]
        )

    def delete(self, id):
        exec_commit(
            'DELETE FROM courses WHERE id = %s',
            [id]
        )