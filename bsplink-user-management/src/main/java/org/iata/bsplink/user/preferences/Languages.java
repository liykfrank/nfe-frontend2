package org.iata.bsplink.user.preferences;

import org.apache.commons.lang3.EnumUtils;

/**
 * The possible user's languages.
 *
 * <p>
 * The language codes are always in lower case letters.
 * See: https://es.wikipedia.org/wiki/ISO_639-1
 * </p>
 */
public enum Languages {

    DE,
    EN,
    ES,
    FR,
    IT;

    public static final Languages DEFAULT = EN;

    /**
     * Returns true if the language exists (should be in lower case characters).
     */
    public static boolean exists(String language) {

        return language.matches("[a-z]+")
                && EnumUtils.isValidEnum(Languages.class, language.toUpperCase());
    }

    @Override
    public String toString() {

        return name().toLowerCase();
    }

}
