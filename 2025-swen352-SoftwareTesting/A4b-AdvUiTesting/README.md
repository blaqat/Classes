# SWEN352-Activity-4b

 The Java code for Activity #4b: UI Test Planning and Design.

## Running the Tests

The infrastructure in this project supports multiple browsers and multiple
screen modes using the `browserType` and `screenMode` Java system properties.

These are specified using Maven profiles like this:

```shell
mvn clean test -P chrome
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
mvn clean test -P edge,phone,debug
```

This will run the tests in a debug session against the MS Edge browser configured
into a Phone mode.


## Sample Test

There is one sample UI tests provided for guidance.  This test, `NavigateToRitMapsTest`, exercises
the _Maps to RIT_ feature  of the [TigerCenter webapp](https://tigercenter.rit.edu).  Specifically, it tests the _Maps at RIT_
button to navigate to a new tab that displays the `maps.rit.edu` web page.

This test class is in the `edu.rit.swen253.test.maps` package.  It uses a Page Object class
in the `edu.rit.swen253.page.tiger` package.
