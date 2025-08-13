# SWEN352-Activity-1

 The Java code for Activity #1: Unit Testing (JUnit and JaCoCo)

## Running the Tests

The infrastructure in this project supports both unit test (Surefire) reports
and code coverage (JaCoCo) reports.

### Unit Test Report

Use the following command to run all the unit tests and generate the Surefire Report:

```shell
mvn clean test surefire-report:report
```

The report is an HTML file that is stored in the `target/site/` directory.  Point your
browser to the `surefire-report.html` file to see the results for all executed tests.

### Code Coverage Report

Use the following command to run all the unit tests and generate the JaCoCo Report:

```shell
mvn clean test surefire-report:report jacoco:report
```

The report is an HTML file that is stored in the `target/site/jacoco` directory.  Point your
browser to the `index.html` file to see the results for all executed tests.


## Sample Test

There is a sample test in the `CoffeeMakerTest` class in the `src/test/` directory.
This test class demonstrates the basic structure of a JUnit test

* `setUp` -- used to instantiate your component-under-test (aka the `CuT`)
* `tearDown` -- used to clear the `CuT` instance variable
* `tautology` -- this is a simple assertion that is always true
