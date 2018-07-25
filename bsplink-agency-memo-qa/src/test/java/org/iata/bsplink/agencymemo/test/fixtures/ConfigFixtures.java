package org.iata.bsplink.agencymemo.test.fixtures;

import java.util.Arrays;
import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.Config;

public class ConfigFixtures {

    /**
     * Returns a list of Configurations.
     */
    public static List<Config> getConfigs() {
        Config c1 = new Config();
        c1.setIsoCountryCode("AA");
        Config c2 = new Config();
        c2.setIsoCountryCode("BB");
        return Arrays.asList(c1, c2);
    }

}
