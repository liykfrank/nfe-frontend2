package org.iata.bsplink.refund.fake;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    public static final String ISO_COUNTRY_CODE_AA = "AA";
    public static final String ISO_COUNTRY_CODE_BB = "BB";
    public static final String ISO_COUNTRY_CODE_CC = "CC";

    private String isoCountryCode;
    private String name;

    /**
    * Returns the list of countries.
    */
    public static List<Country> findAllCountries() {
        return Arrays.asList(
                getFakeCountry(ISO_COUNTRY_CODE_AA),
                getFakeCountry(ISO_COUNTRY_CODE_BB),
                getFakeCountry(ISO_COUNTRY_CODE_CC));
    }

    private static Country getFakeCountry(String isoCountryCode) {

        return new Country(isoCountryCode, "Country " + isoCountryCode);
    }
}
