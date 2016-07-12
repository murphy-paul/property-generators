package au.com.paulmurphy.generators.http;

import com.google.common.collect.ImmutableList;
import net.java.quickcheck.Generator;

import java.util.List;
import static net.java.quickcheck.generator.CombinedGenerators.excludeValues;
import static net.java.quickcheck.generator.PrimitiveGenerators.fixedValues;

/**
 * A {@link Generator} for Http Status Codes.
 *
 * @author Paul Murphy
 */
public final class StatusCodeGenerator implements Generator<Integer> {

    public static final List<Integer> INFORMATIONAL_CODES =
            ImmutableList.of(100, 101, 102);
    public static final List<Integer> SUCCESS_CODES =
            ImmutableList.of(200, 201, 202, 203, 204, 205, 206, 207, 208, 226);
    public static final List<Integer> REDIRECT_CODES =
            ImmutableList.of(301, 302, 303, 304, 305, 306, 307, 308);
    public static final List<Integer> CLIENT_ERROR_CODES =
            ImmutableList.of(400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416,
                    417, 418, 421, 422, 423, 424, 426, 428, 429, 431, 451);
    public static final List<Integer> SERVER_CODES =
            ImmutableList.of(500, 501, 502, 503, 504, 505, 506, 507, 508, 510, 511);
    public static final List<Integer> ALL_CODES = ImmutableList.<Integer>builder()
            .addAll(INFORMATIONAL_CODES)
            .addAll(SUCCESS_CODES)
            .addAll(REDIRECT_CODES)
            .addAll(CLIENT_ERROR_CODES)
            .addAll(CLIENT_ERROR_CODES)
            .addAll(SERVER_CODES).build();

    private final Generator<Integer> generator;

    private StatusCodeGenerator(final Generator<Integer> generator) {
        this.generator = generator;
    }

    /**
     * Produces a {@link StatusCodeGenerator} which contains all Http Status Codes.
     *
     * @return StatusCodeGenerator of all Http Status Codes
     */
    public static StatusCodeGenerator allCodes() {
        return new StatusCodeGenerator(fixedValues(ALL_CODES));
    }

    /**
     * Produces a {@code StatusCodeGenerator} with Http 2XX status codes.
     *
     * @return StatusCodeGenerator with 2XX status codes
     */
    public static StatusCodeGenerator successCodes() {
        return new StatusCodeGenerator(fixedValues(SUCCESS_CODES));
    }

    /**
     * Produces a {@code StatusCodeGenerator} excluding the provided {@code Integer} values.
     *
     * @param exclude List<Integer> values to exclude.
     * @return StatusCodeGenerator excluding the provided values.
     */
    public static StatusCodeGenerator exclude(List<Integer> exclude) {
        return new StatusCodeGenerator(excludeValues(fixedValues(ALL_CODES), exclude));
    }

    @Override
    public Integer next() {
        return generator.next();
    }

}
