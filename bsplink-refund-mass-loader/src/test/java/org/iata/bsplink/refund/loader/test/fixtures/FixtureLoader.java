package org.iata.bsplink.refund.loader.test.fixtures;

import static org.apache.commons.io.FileUtils.deleteQuietly;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FixtureLoader {

    private static final String TMP_DIRECTORY_NAME = "./test_tmp";
    public static final String BASE_FILE_FIXTURES_DIR = "fixtures/files/";
    private static final String BASE_RECORDS_FIXTURES_DIR = "fixtures/records/";

    public static Resource getFileFixture(String name) {

        return new ClassPathResource(BASE_FILE_FIXTURES_DIR + name);
    }

    public static Resource getRecordFixture(String name) {

        return new ClassPathResource(BASE_RECORDS_FIXTURES_DIR + name);
    }

    public static String readFileFixtureToString(String name) {

        return readFileToString(getFileFixture(name));
    }

    public static String readRecordFixtureToString(String name) {

        return readFileToString(getRecordFixture(name));
    }

    private static String readFileToString(Resource resource) {

        try {

            return FileUtils.readFileToString(resource.getFile(), StandardCharsets.ISO_8859_1);

        } catch (Exception exception) {

            throw new RuntimeException(exception);
        }
    }

    /**
     * Creates an empty temporary directory to be used in tests.
     *
     * <p>
     * If the directory exists it's removed and created again.
     * </p>
     */
    public static File createTmpDirectory() {

        File tmpDirectory = new File(TMP_DIRECTORY_NAME);

        deleteQuietly(tmpDirectory);
        tmpDirectory.mkdirs();

        return tmpDirectory;
    }
}
