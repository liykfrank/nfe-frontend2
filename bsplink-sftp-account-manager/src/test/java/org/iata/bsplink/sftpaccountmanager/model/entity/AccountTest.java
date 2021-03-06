package org.iata.bsplink.sftpaccountmanager.model.entity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

public class AccountTest {

    private Account account;
    private Instant referenceTime;

    @Before
    public void setUp() {

        account =  new Account();
        referenceTime = Instant.now();
    }

    @Test
    public void testDefaultStatusIsEnabled() {

        assertEquals(AccountStatus.ENABLED, account.getStatus());
    }

    @Test
    public void testOnPrePersistSetsCreationAndUpdateTime() {

        assertNull(account.getCreationTime());
        assertNull(account.getUpdatedTime());

        account.onPrePersist();

        assertThat(account.getCreationTime(), greaterThanOrEqualTo(referenceTime));
        assertThat(account.getUpdatedTime(), equalTo(account.getCreationTime()));
    }

    @Test
    public void testOnPreUpdateSetsUpdateTime() {

        assertNull(account.getUpdatedTime());

        account.onPreUpdate();

        assertThat(account.getUpdatedTime(), greaterThanOrEqualTo(referenceTime));
    }

}
