package au.com.paulmurphy.generators.http;

import com.google.common.base.Splitter;
import net.java.quickcheck.Generator;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.collection.Pair;
import org.junit.Test;

import static com.google.common.collect.Iterables.size;
import static net.java.quickcheck.QuickCheck.forAll;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * @author Paul Murphy
 */
public class PathGeneratorTest {

    private static final int PATH_MIN_DEPTH = 1;
    private static final int PATH_MAX_DEPTH = 5;

    private Generator<Pair<Integer, Integer>> rangeGenerator = new Generator<Pair<Integer, Integer>>() {
        final int sensibleLimit = 10;
        @Override
        public Pair<Integer, Integer> next() {
            final int minDepth = integers(0, sensibleLimit).next();
            final int maxDepth = integers(minDepth, sensibleLimit).next();

            return new Pair(minDepth, maxDepth);
        }
    };


    @Test
    public void shouldGeneratedPathWithDefaultDepth(){
        verifyPathDepth(new PathGenerator(), PATH_MIN_DEPTH, PATH_MAX_DEPTH);
    }

    @Test
    public void shouldGeneratedPathWithinSpecifiedDepth(){
        forAll(rangeGenerator, new AbstractCharacteristic<Pair<Integer, Integer>>() {
            @Override
            protected void doSpecify(Pair<Integer, Integer> depth) throws Throwable {
                PathGenerator pathGenerator = new PathGenerator(integers(depth.getFirst(), depth.getSecond()));
                verifyPathDepth(pathGenerator, depth.getFirst(), depth.getSecond());

            }
        });
    }

    @Test
    public void shouldHaveTrailingPathSeparator() {
        forAll(new PathGenerator().trailingPathSeparator(true), new AbstractCharacteristic<String>() {
            @Override
            protected void doSpecify(String path) throws Throwable {
                assertThat("Trailing path separator", path.charAt(path.length() - 1), is(equalTo('/')));
            }
        });
    }


    private void verifyPathDepth(final PathGenerator pathGenerator, final int minDepth, final int maxDepth) {
        forAll(pathGenerator, new AbstractCharacteristic<String>() {
            @Override
            protected void doSpecify(String generatedPath) throws Throwable {
                assertThat(
                        "Path depth",
                        getPathDepth(generatedPath),
                        allOf(greaterThanOrEqualTo(minDepth), lessThanOrEqualTo(maxDepth)));
            }
        });
    }

    private int getPathDepth(final String path) {
        return size(Splitter.on("/").omitEmptyStrings().split(path));
    }


}