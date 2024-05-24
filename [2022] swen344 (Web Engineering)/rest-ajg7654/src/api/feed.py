from flask_restful import Resource, request
from db import freebie
from db.swen344_db_utils import format_results

class Feed(Resource):
    
    def get(self):
        user_id = request.args.get("user_id")
        results = format_results(
            freebie.get_user_feeds(user_id=user_id),
            ("user_id", "user_name", "feed"),
            'user_name')
        return results