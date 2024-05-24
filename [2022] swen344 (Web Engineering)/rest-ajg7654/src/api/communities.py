from flask_restful import Resource
from db import freebie
from db.swen344_db_utils import format_results

class Communities(Resource):
    def get(self):
        
        results = format_results(
            freebie.get_community_analytics(),
            ("name", "num_posts", "num_wishes", "num_members", "nearby", "items", "age"),
            "name")
        
        return results