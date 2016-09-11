package au.com.paulmurphy.generators.http;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.java.quickcheck.Generator;
import net.java.quickcheck.collection.Pair;
import net.java.quickcheck.generator.CombinedGenerators;
import net.java.quickcheck.generator.PrimitiveGenerators;

import java.util.List;

/**
 * Produces Key Value {@link Pair} representations for Http Headers. The Key names will produce both actual header
 * key and random key names. Some keys may also produce valid values plus random data.
 *
 * @author Paul Murphy
 */
public class HeaderGenerator implements Generator<Pair<String, String>> {

    public static final List<String> HEADER_NAMES = ImmutableList.<String>builder()
            .add("Accept-Charset").add("Accept").add("Accept-Charset").add("Accept-Encoding")
            .add("Accept-Language").add("Accept-Datetime").add("Authorization").add("Cache-Control")
            .add("Connection").add("Cookie").add("Content-Length").add("Content-MD5").add("Content-Type")
            .add("Date").add("Expect").add("Forwarded").add("From").add("Host").add("If-Match")
            .add("If-Modified-Since").add("If-None-Match").add("If-Range").add("If-Unmodified-Since")
            .add("Max-Forwards").add("Origin").add("Pragma").add("Proxy-Authorization").add("Range")
            .add("Referer [sic]").add("TE").add("User-Agent").add("Upgrade").add("Via").add("Warning")
            .build();
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 20;

    private GeneratorProvider generatorProvider = new GeneratorProvider();

    private Generator<String> keyGenerator =
            CombinedGenerators.ensureValues(HEADER_NAMES, PrimitiveGenerators.letterStrings(MIN_LENGTH, MAX_LENGTH));


    @Override
    public Pair<String, String> next() {
        final String headerName = keyGenerator.next();
        final Generator<String> valueGenerator = generatorProvider.provide(headerName);
        return new Pair(headerName, valueGenerator.next());
    }


    static class GeneratorProvider {

        private MimeTypeGenerator mimeTypeGenerator = MimeTypeGenerator.allMimeTypes();
        private Generator<String> valueGenerator = PrimitiveGenerators.letterStrings(MIN_LENGTH, MAX_LENGTH);

        Generator<String> provide(final String headerName) {
            Preconditions.checkArgument(headerName != null, "headerName is required");

            if ("Accept".equals(headerName) || "Content-Type".equals(headerName)) {
                return mimeTypeGenerator;
            } else {
                return valueGenerator;
            }
        }
    }


}
