from flask import Flask
from flask_restful import Resource, Api


from api.swen_344_db_utils import *
from api.nutrikit import *

app = Flask(__name__) #create Flask instance

api = Api(app) #api router

api.add_resource(Foods,'/foods', '/foods/<int:id>')
api.add_resource(Goal,'/goals', '/goals/<int:id>')

if __name__ == '__main__':
    print("Loading db");
    exec_sql_file('nutrikit.sql');
    print("Starting flask");
    app.run(debug=True), #starts Flask



    