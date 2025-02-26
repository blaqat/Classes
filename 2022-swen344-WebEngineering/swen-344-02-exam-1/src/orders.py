from src.swen344_db_utils import *

def rebuildTables():
    exec_sql_file('./orders_system.sql')

def get_agent_customers():
    # Gets a list of agents paired with their customers

    get = """
    SELECT AGENTS.AGENT_NAME, CUSTOMERS.CUST_NAME 
    FROM CUSTOMERS
    INNER JOIN AGENTS ON AGENTS.AGENT_CODE = CUSTOMERS.AGENT_CODE
    ORDER BY AGENTS.AGENT_NAME DESC
    """

    return exec_get_all(get, [])

def get_count_customers_who_ordered():
    # Gets the amount of customers who have ordered

    count = """
    SELECT count(CUSTOMERS.CUST_NAME)
    FROM ORDERS
    INNER JOIN CUSTOMERS ON ORDERS.CUST_CODE = CUSTOMERS.CUST_CODE    
    """

    return exec_get_one(count, [])

def get_customers_who_ordered():
    # Gets a list of customers who have ordered

    get = """
    SELECT CUSTOMERS.CUST_NAME
    FROM ORDERS
    INNER JOIN CUSTOMERS ON ORDERS.CUST_CODE = CUSTOMERS.CUST_CODE
    GROUP BY CUSTOMERS.CUST_NAME
    ORDER BY CUSTOMERS.CUST_NAME
    """

    return exec_get_all(get, [])

def get_average_money_spent():
    # Gets the average amount of money spent on orders
    
    get = """
    SELECT ROUND (avg(ORDERS.ORD_AMOUNT), 2) as Average_Paid
    FROM ORDERS 
    """

    return exec_get_one(get, [])

def get_agents_total_orders():
    # Gets a list of agents with their total amount of orders

    get = """
    SELECT AGENTS.AGENT_NAME, count(ORDERS) as total_orders
    FROM AGENTS
    INNER JOIN ORDERS ON AGENTS.AGENT_CODE = ORDERS.AGENT_CODE
    GROUP BY AGENTS.AGENT_NAME
    ORDER BY count(ORDERS) DESC
    """

    return exec_get_all(get, [])

def get_agents_total_sales():
    # Gets a list of agents with their total amount of money made

    get = """
    SELECT AGENTS.AGENT_NAME, sum(ORDERS.ORD_AMOUNT) as total_sales
    FROM AGENTS
    INNER JOIN ORDERS ON AGENTS.AGENT_CODE = ORDERS.AGENT_CODE
    GROUP BY AGENTS.AGENT_NAME
    ORDER BY sum(ORDERS.ORD_AMOUNT) DESC
    """

    return exec_get_all(get, [])

def get_customers_total_spent():
    # Gets a list of customers with their total amount spent

    get = """
    SELECT CUSTOMERS.CUST_NAME, sum(ORDERS.ORD_AMOUNT) as total_spent
    FROM CUSTOMERS
    INNER JOIN ORDERS ON CUSTOMERS.CUST_CODE = ORDERS.CUST_CODE
    GROUP BY CUSTOMERS.CUST_NAME
    ORDER BY sum(ORDERS.ORD_AMOUNT) DESC
    """

    return exec_get_all(get, [])

def get_country_total_orders():
    # Gets a list of countries with the count of orders

    get = """
    SELECT CUSTOMERS.CUST_COUNTRY, count(ORDERS) as total_orders
    FROM CUSTOMERS
    INNER JOIN ORDERS ON CUSTOMERS.CUST_CODE = ORDERS.CUST_CODE
    GROUP BY CUSTOMERS.CUST_COUNTRY
    ORDER BY sum(ORDERS.ORD_AMOUNT) DESC
    """

    return exec_get_all(get, [])

def get_agents_avg_commission():
    # Gets the average commission between all agents

    get = """
    SELECT ROUND (avg(AGENTS.COMMISSION * ORDERS.ORD_AMOUNT), 2) as average_commission
    FROM AGENTS
    INNER JOIN ORDERS ON AGENTS.AGENT_CODE = ORDERS.AGENT_CODE
    """

    return exec_get_one(get, [])