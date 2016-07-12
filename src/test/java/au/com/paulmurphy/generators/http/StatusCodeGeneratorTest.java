package au.com.paulmurphy.generators.http;

import net.java.quickcheck.Generator;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.CombinedGenerators;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static net.java.quickcheck.QuickCheck.forAll;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import static au.com.paulmurphy.generators.http.StatusCodeGenerator.INFORMATIONAL_CODES;
import static au.com.paulmurphy.generators.http.StatusCodeGenerator.SUCCESS_CODES;
import static au.com.paulmurphy.generators.http.StatusCodeGenerator.REDIRECT_CODES;
import static au.com.paulmurphy.generators.http.StatusCodeGenerator.CLIENT_ERROR_CODES;
import static au.com.paulmurphy.generators.http.StatusCodeGenerator.SERVER_CODES;

/**
 * @author Paul Murphy
 */
public class StatusCodeGeneratorTest {

    @Test
    public void allCodesShouldNotGenerateValuesLowerThan100() {
        forAll(StatusCodeGenerator.allCodes(), new AbstractCharacteristic<Integer>() {
            @Override
            protected void doSpecify(Integer integer) throws Throwable {
                assertThat("Status code below lower limit", integer, is(greaterThanOrEqualTo(100)));
            }
        });
    }

    @Test
    public void allCodesShouldNotGenerateValuesHigherThan511() {
        forAll(StatusCodeGenerator.allCodes(), new AbstractCharacteristic<Integer>() {
            @Override
            protected void doSpecify(Integer integer) throws Throwable {
                assertThat("Status code above upper limit", integer, is(lessThanOrEqualTo(511)));
            }
        });
    }

    @Test
    public void successCodesShouldGenerateValuesWithinRange() {
        forAll(StatusCodeGenerator.successCodes(), new AbstractCharacteristic<Integer>() {
            @Override
            protected void doSpecify(Integer integer) throws Throwable {
                assertThat("Status code below lower limit", integer, is(greaterThanOrEqualTo(200)));
                assertThat("Status code above upper limit", integer, is(lessThanOrEqualTo(300)));
            }
        });
    }

    @Test
    public void shouldNotIncludeExcludeValues() {
        Generator<List<Integer>> codesGenerate = CombinedGenerators.ensureValues(
                INFORMATIONAL_CODES, SUCCESS_CODES, REDIRECT_CODES, CLIENT_ERROR_CODES, SERVER_CODES );

        forAll(codesGenerate, new AbstractCharacteristic<List<Integer>>() {
            @Override
            protected void doSpecify(List<Integer> integers) throws Throwable {
                StatusCodeGenerator underTest = StatusCodeGenerator.exclude(integers);

                for(Integer i : Iterables.toIterable(underTest)) {
                    assertFalse("Contains excluded value", integers.contains(i));
                }
            }
        });
    }

}