from flask import Flask
from flask_restful import Resource, Api
from api.exam_endpoints import *
from db.init_db import insert_test_data

app = Flask(__name__)
api = Api(app)

api.add_resource(ExampleEndpoint, '/')
api.add_resource(Bleats, '/bleats', '/bleats/<string:name>', '/bleats/<int:id>')
api.add_resource(Users, '/users', '/users/<string:username>')

if __name__ == '__main__':
    insert_test_data()
    app.run(debug=True)
