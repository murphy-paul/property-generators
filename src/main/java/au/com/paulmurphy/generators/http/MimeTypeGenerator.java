package au.com.paulmurphy.generators.http;

import com.google.common.collect.ImmutableList;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;
import net.java.quickcheck.generator.support.DefaultFrequencyGenerator;
import net.java.quickcheck.generator.support.StringGenerator;

import java.util.List;

import static net.java.quickcheck.generator.PrimitiveGenerators.fixedValues;
import static net.java.quickcheck.generator.CombinedGenerators.ensureValues;

/**
 * @author Paul Murphy
 */
public final class MimeTypeGenerator implements Generator<String> {

    public static final List<String> APPLICATION_MIME_TYPE = ImmutableList.<String>builder()
            .add("application/msword")                  //.doc
            .add("application/octet-stream")            // .bin, .exe
            .add("application/pdf")                     // .pdf
            .add("application/postscript")              // .ps, .ai, .eps
            .add("application/rtf")                     // .rtf
            .add("application/x-gtar")                  // .gtar
            .add("application/x-gzip")                  // .gz
            .add("application/x-java-archive")          // .jar
            .add("application/x-java-serialized-object")// .ser
            .add("application/x-java-vm")               // .class
            .add("application/x-tar")                   // .tar
            .add("application/zip")                     // .zip
            .build();

    public static final List<String> AUDIO_MIME_TYPE = ImmutableList.<String>builder()
            .add("audio/x-aiff")    // .aiff
            .add("audio/basic")     // .ua
            .add("audio/x-midi")    // .mid, .midi
            .add("audio/x-wav")     // .wav
            .build();

    public static final List<String> IMAGE_MIME_TYPE = ImmutableList.<String>builder()
            .add("image/bmp")       // .bmp
            .add("image/gif")       // .gif
            .add("image/jpeg")      // .jpeg, .jpg, .jpe
            .add("image/tiff")      // .tiff, .tif
            .add("image/x-xbitmap") // .xbm
            .build();

    public static final List<String> MULTIPART_MIME_TYPE = ImmutableList.<String>builder()
            .add("multipart/x-gzip")    // .gzip
            .add("multipart/x-zip")     // .zip
            .build();

    public static final List<String> TEXT_MIME_TYPE = ImmutableList.<String>builder()
            .add("text/html")       // .htm, .html
            .add("text/plain")      // .txt
            .add("text/richtext")   // .rtf, .rtx
            .build();

    public static final List<String> VIDEO_MIME_TYPE = ImmutableList.<String>builder()
            .add("video/mpeg")      // .mpg, .mpeg, .mpe
            .add("video/vnd.vivo")  // .viv, .vivo
            .add("video/quicktime") // .qt, .mov
            .add("video/x-msvideo") // .avi
            .build();

    public static final List<String> ALL_MIME_TYPES = ImmutableList.<String>builder()
            .addAll(APPLICATION_MIME_TYPE)
            .addAll(AUDIO_MIME_TYPE)
            .addAll(IMAGE_MIME_TYPE)
            .addAll(MULTIPART_MIME_TYPE)
            .addAll(TEXT_MIME_TYPE)
            .addAll(VIDEO_MIME_TYPE)
            .build();

    private final Generator<String> mimeTypeGenerator;

    private MimeTypeGenerator(final List<String> ensureValues, boolean includeRandom) {
        mimeTypeGenerator =
                includeRandom ? ensureValues(ensureValues, new RandomMimeTypeGenerator()) : fixedValues(ensureValues);
    }


    public static MimeTypeGenerator allMimeTypes() {
        return new MimeTypeGenerator(ALL_MIME_TYPES, true);
    }

    public static MimeTypeGenerator application() {
        return new MimeTypeGenerator(APPLICATION_MIME_TYPE, false);
    }

    public static MimeTypeGenerator audio() {
        return new MimeTypeGenerator(AUDIO_MIME_TYPE, false);
    }

    public static MimeTypeGenerator image() {
        return new MimeTypeGenerator(IMAGE_MIME_TYPE, false);
    }

    public static MimeTypeGenerator multipart() {
        return new MimeTypeGenerator(MULTIPART_MIME_TYPE, false);
    }

    public static MimeTypeGenerator text() {
        return new MimeTypeGenerator(TEXT_MIME_TYPE, false);
    }

    public static MimeTypeGenerator video() {
        return new MimeTypeGenerator(VIDEO_MIME_TYPE, false);
    }


    @Override
    public String next() {
        return mimeTypeGenerator.next();
    }

    private static class RandomMimeTypeGenerator implements Generator<String> {

        private final Generator<Character> allowedCharacters = PrimitiveGenerators.characters('a', 'z');
        private final Generator<Character> allowedSymbols = PrimitiveGenerators.characters('-', '.');

        //Text left of the forward slash (/)
        private StringGenerator leftPart;
        //Text right of the forward slash (/)
        private StringGenerator rightPart;
        private final Generator<Integer> leftPartLength = PrimitiveGenerators.integers(10, 20);
        private final Generator<Integer> rightPartLength = PrimitiveGenerators.integers(5, 15);

        RandomMimeTypeGenerator() {
            leftPart = new StringGenerator(leftPartLength, allowedCharacters);

            rightPart = new StringGenerator(
                    rightPartLength,
                    new DefaultFrequencyGenerator(allowedCharacters, 10).add(allowedSymbols, 1));

        }

        @Override
        public String next() {
            return leftPart.next() + "/" + rightPart.next();
        }
    }
}
