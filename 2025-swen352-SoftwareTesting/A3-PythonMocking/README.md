# SWEN-352-PYTHON-MOCK
## Running Tests

You can run your unit tests from the root directory of the project with the following command:
```bash
python -m unittest discover
```

### Test Organization
- Unittest python files should all be placed in the `tests` directory
- Helper unit test data should be placed in the `tests_data` directory
- Make sure all of your unit test files start with "test_*"; otherwise python will not detect them with this command

### Test Coverage

Making quality unit tests is important but the focus of this activity is on the usage of mocks, so feel free to stop writing tests once you hit 95% coverage and do not worry about the number of tests you have.

You can run the following commands to measure your tests coverage (in the root directory of the project):

```bash
coverage run --source=library/ -m unittest discover
```
```bash
coverage html
# or
coverage report
```


If you ran `coverage html`, a directory with the html report will be created, simply open `index.html` in your browser to view the results.
