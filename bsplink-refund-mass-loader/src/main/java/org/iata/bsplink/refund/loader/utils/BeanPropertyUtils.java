package org.iata.bsplink.refund.loader.utils;

import org.springframework.beans.PropertyAccessorFactory;

/**
 * Shortcuts to access the Spring bean utilities.
 */
public class BeanPropertyUtils {

    private BeanPropertyUtils() {
        // only contains static methods
    }

    public static void setProperty(Object object, String fieldName, String value) {

        PropertyAccessorFactory.forBeanPropertyAccess(object).setPropertyValue(fieldName, value);
    }

    public static Object getProperty(Object object, String fieldName) {

        return PropertyAccessorFactory.forBeanPropertyAccess(object).getPropertyValue(fieldName);
    }
}
