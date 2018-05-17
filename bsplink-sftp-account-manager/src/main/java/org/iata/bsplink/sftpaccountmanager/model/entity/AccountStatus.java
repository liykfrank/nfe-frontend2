package org.iata.bsplink.sftpaccountmanager.model.entity;

public enum AccountStatus {

    /**
     * Account is active and can be used.
     */
    ENABLED,

    /**
     * Account has been disabled manually.
     */
    DISABLED,

    /**
     * Account has been expired by inactivity.
     */
    EXPIRED
}
