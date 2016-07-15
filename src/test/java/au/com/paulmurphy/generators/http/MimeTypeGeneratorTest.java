package au.com.paulmurphy.generators.http;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import net.java.quickcheck.characteristic.AbstractCharacteristic;
import net.java.quickcheck.generator.iterable.Iterables;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static net.java.quickcheck.QuickCheck.forAll;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

/**
 * @author Paul Murphy
 */
public class MimeTypeGeneratorTest {

    private static final String MIME_TYPE_PATTERN = "^[a-z]*/[a-z-\\.]*";

    @Test
    public void shouldProduceMimeTypesWithCorrectFormat() {
        forAll(MimeTypeGenerator.allMimeTypes(), new AbstractCharacteristic<String>() {
            @Override
            protected void doSpecify(String mimeType) throws Throwable {
                assertTrue(mimeType.matches(MIME_TYPE_PATTERN));
            }
        });
    }

    @Test
    public void shouldContainRandomValues() {
        List<String> randomValues = FluentIterable.from(Iterables.toIterable(MimeTypeGenerator.allMimeTypes()))
                .filter(mimeType -> !MimeTypeGenerator.ALL_MIME_TYPES.contains(mimeType))
                .toList();

        assertThat(randomValues.size(), is(greaterThan(0)));
    }

    @Test
    public void shouldContainAllFixedValues() {
        contains(Iterables.toIterable(MimeTypeGenerator.allMimeTypes()), MimeTypeGenerator.ALL_MIME_TYPES);
    }

    @Test
    public void shouldContainAllApplicationValues() {
        contains(Iterables.toIterable(MimeTypeGenerator.application()), MimeTypeGenerator.APPLICATION_MIME_TYPE);
    }

    @Test
    public void shouldContainAllAudioValues() {
        contains(Iterables.toIterable(MimeTypeGenerator.audio()), MimeTypeGenerator.AUDIO_MIME_TYPE);
    }

    @Test
    public void shouldContainAllImageValues() {
        contains(Iterables.toIterable(MimeTypeGenerator.image()), MimeTypeGenerator.IMAGE_MIME_TYPE);
    }

    @Test
    public void shouldContainAllMultiPartValues() {
        contains(Iterables.toIterable(MimeTypeGenerator.multipart()), MimeTypeGenerator.MULTIPART_MIME_TYPE);
    }

    @Test
    public void shouldContainAllTextValues() {
        contains(Iterables.toIterable(MimeTypeGenerator.text()), MimeTypeGenerator.TEXT_MIME_TYPE);
    }

    @Test
    public void shouldContainAllFixedVideoMimeTypes() {
        contains(Iterables.toIterable(MimeTypeGenerator.video()), MimeTypeGenerator.VIDEO_MIME_TYPE);
    }

    private void contains(Iterable<String> iterable, List<String> fixedValues) {
        Set<String> underTest = ImmutableSet.copyOf(iterable);

        //Iterator over the fixed values, and ensure the generator contains those values.
        for (String type : fixedValues) {
            assertTrue(underTest.contains(type));
        }

    }

}