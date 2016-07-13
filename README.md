# property-generators
[![Build Status](https://travis-ci.org/murphy-paul/property-generators.svg?branch=master)](https://travis-ci.org/murphy-paul/property-generators)

A project containing common property generators for use with the Java [QuickCheck ](https://bitbucket.org/blob79/quickcheck) Library.

Usage

```java
    private void shouldBeFalseForAllNon200OKResponseCodes() {
        forAll(StatusCodeGenerator.allCodes(), new AbstractCharacteristic<Integer>() {
            @Override
            protected void doSpecify(Integer statusCode) throws Throwable {
                guard(statusCode != 200);

                assertFalse(success(statusCode));
            }
        });
    }


    private boolean success(Integer statusCode) {
        return statusCode == 200;
    }
```

