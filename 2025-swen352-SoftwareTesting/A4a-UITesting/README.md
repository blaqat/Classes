# SWEN352-Activity-4a

 The Java code for Activity #4a: UI Testing using Selenium.

## Running the Tests

The infrastructure in this project supports multiple browsers and multiple
screen modes using the `browserType` and `screenMode` Java system properties.

These are specified using Maven profiles like this:

```shell
./mvnw clean test -P chrome
```

Here is the complete set of Maven profiles to specify the browser type:
* `chrome` -- the Google Chrome browser (*default*)
* `firefox` -- the Mozilla Firefox browser
* `edge` -- the Microsoft Edge browser
* `safari` -- the Apple Safari browser

Here are the two Maven profiles to specify the screen mode:
* `desktop` -- the Desktop screen mode (*default*)
* `phone` -- the Phone screen mode

You can also use the `debug` Maven profile to tell Maven to pause during
startup so you can engage your IDE in a Java debugging session.

These profiles can be combined like this:

```shell
./mvnw clean test -P edge,phone,debug
```

This will run the tests in a debug session against the MS Edge browser configured
into a Phone mode.


## Sample Test

There are two sample UI tests provided for guidance.  Both tests test the RIT
Home page.

* `RitRatingsTest` -- tests the exists of the rankings in the middle of the page,
   for example, that RIT is 5th for being "Among Top Schools for Co-op or Internship Programs"
* `RitExploreStudiesInComputingTest` -- tests the links to RIT's _Areas of Study_, this test
   validates the existence of the _Computing and Information Sciences_ link and follows that link

These two test class are in the `edu.rit.swen253.test.sample` package.  These use
Page Object and View Objects classes in the `edu.rit.swen253.page.sample` package.
