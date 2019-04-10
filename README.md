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


The HeaderGenerator provides key values pairs which represent http headers. Example use case below:


```java
    /**
     *
     * Test method to demonstrate a use case for the Generator.
     *
     * Accepts a List<Pair<String, String>> which represents Http Headers, and returns a modified
     * List of header values.
     *
     * This method should <b>NOT</b> replace any existing header.
     *
     * @param headers
     */
    private List<Pair<String, String>> setHeaders(List<Pair<String, String>> headers) {

        ImmutableList.Builder<Pair<String, String>> builder = ImmutableList.builder();
        builder.addAll(headers);

        ImmutableList<String> existingHeaders =
                FluentIterable.from(headers)
                        .transform(header -> header.getFirst())
                        .toList();

        if (!existingHeaders.contains(ACCEPT_HEADER)) {
            builder.add(new Pair<>(ACCEPT_HEADER, APPLICATION_JSON));
        }

        if (!existingHeaders.contains(CONTENT_TYPE)) {
            builder.add(new Pair(CONTENT_TYPE, APPLICATION_JSON));
        }

        return builder.build();
    }
```

A test for such a method could use the generator

```java
    @Test
    public void shouldNotReplaceExistingHeaders() {
        forAll(new HeaderGenerator(), new AbstractCharacteristic<Pair<String, String>>() {
            @Override
            protected void doSpecify(Pair<String, String> header) throws Throwable {
                List<Pair<String, String>> current = ImmutableList.of(header);

                List<Pair<String, String>> underTest = setHeaders(current);

                assertThat(underTest, hasItems(header));
            }
        });
    }

```
