package org.iata.bsplink.user.validation;

public class CountryValidationMessages {

    public static final String ISOC_DUPLICATED_MESSAGE =
            "ISO Country Code to be assigned only once.";
    public static final String ISOC_EMPTY_MESSAGE = "The ISO Country Code cannot be empty.";
    public static final String ISOC_FORMAT_MESSAGE = "ISO Country Code format is incorrect.";

    private CountryValidationMessages() {
        // only has static content
    }

}
