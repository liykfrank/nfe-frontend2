package org.iata.bsplink.user.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LanguagesTest {

    @Test
    public void testExists() {

        for (Languages language : Languages.values()) {

            assertTrue(Languages.exists(language.toString()));
        }
    }

    @Test
    public void testToStringReturnsValueInLowerCaseLetters() {

        assertEquals("es", Languages.ES.toString());
    }

}
