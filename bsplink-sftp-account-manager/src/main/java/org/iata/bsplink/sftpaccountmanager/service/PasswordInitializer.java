package org.iata.bsplink.sftpaccountmanager.service;

/**
 * Represents a service which returns a initial random password.
 */
public interface PasswordInitializer {

    public String generateNewPasswordHash();
}
