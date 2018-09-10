package org.iata.bsplink.user.preferences;

import java.time.ZoneId;
import java.util.Set;

public class TimeZones {

    public static final String DEFAULT = "GMT";

    private TimeZones() {
        // only have static methods
    }

    public static Set<String> timeZones() {

        return ZoneId.getAvailableZoneIds();
    }

    public static boolean exists(String timeZone) {

        return timeZones().contains(timeZone);
    }

}
