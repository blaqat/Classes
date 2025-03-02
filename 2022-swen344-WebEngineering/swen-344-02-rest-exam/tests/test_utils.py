import requests

URL = "http://localhost:5000/%s"

def print_question(test, number, question, path, ans=False):
    print("\nQ%s - %s:\n%s" % (number, question, (path if ans else get_rest_call(test, URL%path))))

def get_rest_call(test, url, params = {}, get_header = {},expected_code = 200):
    '''Implements a REST api using the GET verb'''
    response = requests.get(url, params, headers = get_header)
    test.assertEqual(expected_code, response.status_code,
                     f'Response code to {url} not {expected_code}')
    return response.json()

def post_rest_call(test, url, params = {}, post_header = {},expected_code = 200):
    '''Implements a REST api using the POST verb'''
    response = requests.post(url, params, headers = post_header)
    test.assertEqual(expected_code, response.status_code,
                     f'Response code to {url} not {expected_code}')
    return response.json()

def put_rest_call(test, url, params = {}, put_header = {},expected_code = 200):
    '''Implements a REST api using the PUT verb'''
    response = requests.put(url, params, headers = put_header)
    test.assertEqual(expected_code, response.status_code,
                     f'Response code to {url} not {expected_code}')
    return response.json()

def delete_rest_call(test, url,  expected_code = 200):
    '''Implements a REST api using the DELETE verb'''
    response = requests.delete(url)
    test.assertEqual(expected_code, response.status_code,
                     f'Response code to {url} not {expected_code}')
    return response.json()

