package au.com.paulmurphy.generators.http;

import net.java.quickcheck.characteristic.AbstractCharacteristic;
import org.junit.Test;

import static org.junit.Assert.*;
import static net.java.quickcheck.QuickCheck.forAll;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * @author Paul Murphy
 */
public class StatusCodeGeneratorTest {

    @Test
    public void shouldNotGenerateValuesLowerThan100() {
        forAll(new StatusCodeGenerator(), new AbstractCharacteristic<Integer>() {
            @Override
            protected void doSpecify(Integer integer) throws Throwable {
                assertThat("Status code below lower limit", integer, is(greaterThanOrEqualTo(100)));
            }
        });
    }

    @Test
    public void shouldNotGenerateValuesHigherThan511() {
        forAll(new StatusCodeGenerator(), new AbstractCharacteristic<Integer>() {
            @Override
            protected void doSpecify(Integer integer) throws Throwable {
                assertThat("Status code above upper limit", integer, is(lessThanOrEqualTo(511)));
            }
        });
    }

}