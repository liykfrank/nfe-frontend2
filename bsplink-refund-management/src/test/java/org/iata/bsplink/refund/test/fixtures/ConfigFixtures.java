package org.iata.bsplink.refund.test.fixtures;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.iata.bsplink.refund.model.entity.Config;

public class ConfigFixtures {

    /**
     * Returns a list of Configurations.
     */
    public static List<Config> getConfigs() {

        return getCountries().stream().map(countryName -> new Config(countryName))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of countries.
     */
    public static List<String> getCountries() {

        return Arrays.asList("AA", "BB");
    }

}
