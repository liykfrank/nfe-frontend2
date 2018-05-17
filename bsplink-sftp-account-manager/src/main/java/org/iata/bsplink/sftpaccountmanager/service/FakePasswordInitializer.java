package org.iata.bsplink.sftpaccountmanager.service;

import java.util.Optional;

import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Fake password initializer.
 *
 * <p>
 * The password initializer should be an external service that doesn't exist yet, this service
 * fills the hole until it is available.
 *
 * By default the password is randomly generated and printed to the logs when the first account
 * is created, only one password is generated per execution.
 *
 * You can set a specific fake password with the property <b>app.fake.password.initializer</b>.
 * </p>
 */
@Service
@CommonsLog
public class FakePasswordInitializer implements PasswordInitializer {

    @Value("${app.fake.password.initializer:#{null}}")
    private Optional<String> fakePassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String generateNewPasswordHash() {

        return passwordEncoder.encode(getPassword());
    }

    private String getPassword() {

        if (fakePassword.isPresent()) {

            return fakePassword.get();
        }

        String randomPassword = RandomStringUtils.randomAlphanumeric(8);

        log.info(String.format("\n\nACCOUNT FAKE PASSWORD: %s\n\n", randomPassword));

        fakePassword = Optional.of(randomPassword);

        return randomPassword;
    }

}
