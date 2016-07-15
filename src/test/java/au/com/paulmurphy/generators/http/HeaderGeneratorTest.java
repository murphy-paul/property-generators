package au.com.paulmurphy.generators.http;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import net.java.quickcheck.Generator;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.collection.Pair;
import net.java.quickcheck.generator.PrimitiveGenerators;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static net.java.quickcheck.QuickCheck.forAll;
import static net.java.quickcheck.QuickCheck.guard;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Paul Murphy
 */
public class HeaderGeneratorTest {

    private static final String ACCEPT_HEADER = "Accept";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String EMPTY = "";

    @Test
    public void shouldContainAllPreConfiguredHeaderNames() {

        Set<String> headerNames = FluentIterable
                .from(Iterables.toIterable(new HeaderGenerator()))
                .transform(header -> header.getFirst()).toSet();

        for (String expected : headerNames) {
            assertTrue(headerNames.contains(expected));
        }
    }

    @Test
    public void shouldNotProduceEmptyHeaderNames() {
        forAll(new HeaderGenerator(), new AbstractCharacteristic<Pair<String, String>>() {
            @Override
            protected void doSpecify(Pair<String, String> stringStringPair) throws Throwable {
                assertThat(stringStringPair.getFirst(), is(not(equalTo(EMPTY))));
            }
        });
    }

    @Test
    public void shouldNotProduceEmptyHeaderValues() {
        forAll(new HeaderGenerator(), new AbstractCharacteristic<Pair<String, String>>() {
            @Override
            protected void doSpecify(Pair<String, String> stringStringPair) throws Throwable {
                assertThat(stringStringPair.getSecond(), is(not(equalTo(EMPTY))));
            }
        });
    }

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

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenHeaderNameIsNull() {
        HeaderGenerator.GeneratorProvider provider = new HeaderGenerator.GeneratorProvider();

        try {
            provider.provide(null);

            fail("excepted an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //Success
        }

    }

    @Test
    public void shouldReturnMimeTypeGenerator() {
        HeaderGenerator.GeneratorProvider provider = new HeaderGenerator.GeneratorProvider();

        assertThat(provider.provide("Accept"), is(instanceOf(MimeTypeGenerator.class)));
        assertThat(provider.provide("Content-Type"), is(instanceOf(MimeTypeGenerator.class)));
    }

    @Test
    public void shouldNotReturnMimeTypeGenerator() {
        Generator<String> headerNames = PrimitiveGenerators.fixedValues(HeaderGenerator.HEADER_NAMES);
        HeaderGenerator.GeneratorProvider provider = new HeaderGenerator.GeneratorProvider();

        forAll(headerNames, new AbstractCharacteristic<String>() {
            @Override
            protected void doSpecify(String headerName) throws Throwable {
                guard(headerName != "Accept");
                guard(headerName != "Content-Type");

                assertThat(provider.provide(headerName), is(not(instanceOf(MimeTypeGenerator.class))));
            }
        });
    }


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


}