from flask_restful import Resource
from db import freebie
from db.swen344_db_utils import format_results

class Accounts(Resource):
    def get(self):
        results = format_results(
            freebie.get_accounts(),
            ('id', 'community_id', 'name', 'is_mod','birthdate'),
            'id'
        )
        return results