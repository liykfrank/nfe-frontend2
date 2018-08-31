package org.iata.bsplink.user.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class TestUtils {

    /**
     * Gets the json from the provided file as a string.
     *
     * @param fileName the file name
     * 
     * @return the json contained in the file
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String getJson(String fileName) throws IOException {
        String aux;
        Resource resource = new ClassPathResource(fileName);

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            aux = buffer.lines().collect(Collectors.joining("\n"));
            return aux;
        } finally {
            if (buffer != null) {
                buffer.close();
            }
        }
    }
}
