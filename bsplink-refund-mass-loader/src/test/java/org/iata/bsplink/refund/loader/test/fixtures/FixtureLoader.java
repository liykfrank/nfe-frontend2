package org.iata.bsplink.refund.loader.test.fixtures;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FixtureLoader {

    private static final String BASE_FILE_FIXTURES_DIR = "fixtures/files/";
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
}
