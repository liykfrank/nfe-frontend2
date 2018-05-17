package org.iata.bsplink.sftpaccountmanager.model.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountFixture;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.time.Instant;

import javax.persistence.EntityManager;

import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    private Account account;
    private Instant referenceTime;

    @Before
    public void setUp() {

        account = getAccountFixture();
        referenceTime = Instant.now();
    }

    @Test
    public void testCreationTimeIsNotUpdatable() {

        account = accountRepository.saveAndFlush(account);

        final Instant creationTime = account.getCreationTime();

        account.setCreationTime(Instant.parse("2018-01-01T00:00:00Z"));
        accountRepository.saveAndFlush(account);

        entityManager.refresh(account);

        assertThat(account.getCreationTime(), equalTo(creationTime));
    }

    @Test
    public void testCreationTimeIsSetWhenAccountPersisted() {

        assertNull(account.getCreationTime());

        account = accountRepository.saveAndFlush(account);

        assertThat(account.getCreationTime(), greaterThanOrEqualTo(referenceTime));
    }

    @Test
    public void testUpdateTimeIsSetWhenAccountIsUpdated() {

        assertNull(account.getUpdatedTime());

        account = accountRepository.saveAndFlush(account);

        assertThat(account.getUpdatedTime(), greaterThanOrEqualTo(referenceTime));
    }

}
