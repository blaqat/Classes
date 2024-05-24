from flask import Flask
from flask_restful import Resource, Api
from api import *

app = Flask(__name__)
api = Api(app)

api.add_resource(home.Home, '/')
api.add_resource(accounts.Accounts, '/accounts')
api.add_resource(communities.Communities, '/communities')
api.add_resource(feed.Feed, '/feed')

if __name__ == '__main__':
    app.run(debug=True)