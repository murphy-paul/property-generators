package au.com.paulmurphy.generators.http;

import net.java.quickcheck.Generator;

import static net.java.quickcheck.generator.PrimitiveGenerators.characters;
import static net.java.quickcheck.generator.PrimitiveGenerators.integers;
import static net.java.quickcheck.generator.PrimitiveGenerators.strings;

/**
 * A {@link Generator} which produces variable length paths.
 *
 * @author Paul Murphy
 */
public class PathGenerator implements Generator<String> {

    private static final char LO = 'a';
    private static final char HI = 'z';

    private static final int NAME_MIN_SIZE = 3;
    private static final int NAME_MAX_SIZE = 25;

    private static final int PATH_MIN_DEPTH = 1;
    private static final int PATH_MAX_DEPTH = 5;

    private Generator<Integer> pathDepth;

    private Generator<String> stringGenerator;

    private boolean trailingPathSeparator;

    /**
     * Default constructor
     */
    public PathGenerator() {
        this(integers(PATH_MIN_DEPTH, PATH_MAX_DEPTH),
                strings(integers(NAME_MIN_SIZE, NAME_MAX_SIZE), characters(LO, HI)));
    }

    /**
     * Construct an instance which will use the provided path depth generator to determine the depth of the path.
     *
     * @param pathDepth the generator which will determine the path depth.
     */
    public PathGenerator(Generator<Integer> pathDepth) {
        this(pathDepth, strings(integers(NAME_MIN_SIZE, NAME_MAX_SIZE), characters(LO, HI)));
    }


    /**
     * The generated paths will have a trailing path separator.
     *
     * @param trailingPathSeparator true if a trailing path separator is desired. false by default.
     * @return instance of the {@code PathGenerator}.
     */
    public PathGenerator trailingPathSeparator(final boolean trailingPathSeparator) {
        this.trailingPathSeparator = trailingPathSeparator;
        return this;
    }

    /**
     * Construct an instance which will use the provided generators to construct the path.
     *
     * @param pathDepth the generator which will determine the path depth.
     * @param stringGenerator the generator which will construct each part name.
     */
    public PathGenerator(Generator<Integer> pathDepth, Generator<String> stringGenerator) {
        this.pathDepth = pathDepth;
        this.stringGenerator = stringGenerator;
    }

    @Override
    public String next() {
        int length = pathDepth.next();
        StringBuilder builder = new StringBuilder(length + 1);
        for (int i = 0; i < length; i++) {
            builder.append("/");
            builder.append(stringGenerator.next());
        }
        if (trailingPathSeparator) {
            builder.append("/");
        }
        return builder.toString();

    }
}
