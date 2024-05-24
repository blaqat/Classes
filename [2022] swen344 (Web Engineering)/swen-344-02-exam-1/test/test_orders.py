import unittest
from src.orders import *
from src.swen344_db_utils import connect



def print_question(number, question, answer):
    print("Q%s:\n%s:\n%s\n" % (number, question, answer))

class TestExam(unittest.TestCase):
    def setUp(self):
        """Setup"""
        rebuildTables()
        print("Setup done")

    def test_tables(self):
        """Check existence of the tables"""
        result = exec_get_all('SELECT * FROM agents')
        self.assertNotEqual([], result, "no rows in agents table")
        result = exec_get_all('SELECT * FROM orders')
        self.assertNotEqual([], result, "no rows in orders table")
        print("test_tables done")

    def test_questions(self):
        # Question 1:
        question = "List of each agent and customers they have supported"
        answer = ""
        agent_customer_list = get_agent_customers()

        for agent_customer in agent_customer_list:
            answer += 'agent %s, customer %s\n' % agent_customer

        print_question(1, question, answer)

        # Question 2:
        question = "How many unique customers have placed orders"
        answer = get_count_customers_who_ordered()
        print_question(2, question, answer)

        # Question 3:
        question = "Average value of all orders placed"
        answer = '$%s' % get_average_money_spent()
        print_question(3, question, answer)

        # Question 4:
        question = "Agent that supported the most orders and how many"
        answer_template = 'agent %s, orders %s'

        agent_list = get_agents_total_orders()
        first_agent = agent_list[0]
        answer = answer_template % first_agent

        print_question(4, question, answer)

        # Question 5:
        question = "Agent that supported the least orders and how many"
        
        last_agent = agent_list[-1]
        answer = answer_template % last_agent

        print_question(5, question, answer)

        # Question 6:
        question = "Agent that had highest sales and total amount"
        answer_template = 'agent %s, sales $%s'

        agent_list = get_agents_total_sales()
        first_agent = agent_list[0]
        answer = answer_template % first_agent

        print_question(6, question, answer)

        # Question 7:
        question = "Agent that had lowest sales and total amount"
        
        last_agent = agent_list[-1]
        answer = answer_template % last_agent

        print_question(7, question, answer)

        # Question 8:
        question = "Customer that has spent the most money and how much"
        answer_template = 'customer %s, total $%s'

        cust_list = get_customers_total_spent()
        first_customer = cust_list[0]
        answer = answer_template % first_customer

        print_question(8, question, answer)

        # Question 9:
        question = "Country with most orders and how many"
        answer_template = 'country %s, orders %s'

        country_list = get_country_total_orders()
        first_country = country_list[0]
        answer = answer_template % first_country

        print_question(9, question, answer)

        # Question 10:
        question = "The average commision paid out to agents"
        answer = '$%s' % get_agents_avg_commission()
        print_question(10, question, answer)