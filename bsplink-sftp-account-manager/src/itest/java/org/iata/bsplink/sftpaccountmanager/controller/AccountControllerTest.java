package org.iata.bsplink.sftpaccountmanager.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.LOGIN;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.PUBLIC_KEY;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountFixture;
import static org.iata.bsplink.sftpaccountmanager.test.fixtures.AccountFixtures.getAccountRequestFixture;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.sftpaccountmanager.dto.AccountPasswordRequest;
import org.iata.bsplink.sftpaccountmanager.dto.AccountRequest;
import org.iata.bsplink.sftpaccountmanager.model.entity.Account;
import org.iata.bsplink.sftpaccountmanager.model.entity.AccountStatus;
import org.iata.bsplink.sftpaccountmanager.model.repository.AccountRepository;
import org.iata.bsplink.sftpaccountmanager.service.AccountService;
import org.iata.bsplink.sftpaccountmanager.service.SftpServerAccountManager;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@RunWith(JUnitParamsRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest {

    private static final String BASE_URI = "/v1/accounts";
    private static final String LOGIN_URI = BASE_URI + "/" + LOGIN;
    private static final String PASSWORD_URI = LOGIN_URI + "/password";
    private static final String PASSWORD = "anyPassword";
    private static final String NEW_PASSWORD = "anyNewPassword";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @SpyBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SftpServerAccountManager sftpServerAccountManager;

    private Account account;

    private AccountRequest accountRequest;
    private String accountRequestJson;

    private Instant referenceTime;

    @Before
    public void setUp() throws Exception {

        accountRepository.deleteAll();

        account = getAccountFixture();
        account.setPassword(passwordEncoder.encode(PASSWORD));

        accountRequest = getAccountRequestFixture();
        accountRequestJson = mapper.writeValueAsString(accountRequest);

        referenceTime = Instant.now();

        configurePublicKeyValidatorToReturn(true);
    }

    private void configurePublicKeyValidatorToReturn(boolean value) {

        when(sftpServerAccountManager.publicKeyIsValid(anyString())).thenReturn(value);
    }

    @Test
    public void testCreatesAccount() throws Exception {

        String responseBody = mockMvc.perform(
                post(BASE_URI).content(accountRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Account createdAccount = mapper.readValue(responseBody, Account.class);

        assertAccountHasExpectedValues(createdAccount, account);

        verify(accountService).create(accountRequest);
    }

    private void assertAccountHasExpectedValues(Account actualAccount, Account expectedAccount) {

        assertThat(actualAccount.getLogin(), equalTo(LOGIN));
        assertThat(actualAccount.getPublicKey(), equalTo(PUBLIC_KEY));
        assertThat(actualAccount.getCreationTime(), greaterThanOrEqualTo(referenceTime));
        assertThat(actualAccount.getUpdatedTime(), greaterThanOrEqualTo(referenceTime));
    }

    @Test
    public void testReturnsErrorIfLoginExistsAlready() throws Exception {

        createAccount();

        String expectedMessage = String.format("Login name %s exists already", LOGIN);

        String actualMessage = mockMvc.perform(
                post(BASE_URI).content(accountRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResolvedException().getMessage();

        assertThat(actualMessage, equalTo(expectedMessage));
    }

    private Account createAccount() {

        return accountRepository.saveAndFlush(account);
    }

    @Test
    public void testReturnsNotFoundWhenTryingToUpdateInexistentAccount() throws Exception {

        mockMvc.perform(put(LOGIN_URI).content(accountRequestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatesAccount() throws Exception {

        final Account savedAccount = createAccount();

        accountRequest.setStatus(AccountStatus.DISABLED);
        accountRequest.setPublicKey("modifiedKey");

        accountRequestJson = mapper.writeValueAsString(accountRequest);

        String responseBody = mockMvc
                .perform(put(LOGIN_URI).content(accountRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Account updatedAccount = mapper.readValue(responseBody, Account.class);

        assertThat(updatedAccount.getLogin(), equalTo(LOGIN));
        assertThat(updatedAccount.getPublicKey(), equalTo("modifiedKey"));

        assertThat(updatedAccount.getCreationTime(), equalTo(savedAccount.getCreationTime()));
        assertThat(updatedAccount.getUpdatedTime(),
                greaterThanOrEqualTo(savedAccount.getUpdatedTime()));

        verify(accountService).update(LOGIN, accountRequest);
    }

    @Test
    public void testDeletesAccount() throws Exception {

        createAccount();

        String responseBody = mockMvc.perform(delete(LOGIN_URI))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Account deletedAccount = mapper.readValue(responseBody, Account.class);

        assertThat(deletedAccount.getLogin(), equalTo(LOGIN));
        verify(accountService).delete(LOGIN);
    }

    @Test
    public void testReturnsNotFoundWhenTryingToDeleteInexistentAccount() throws Exception {

        mockMvc.perform(delete(LOGIN_URI)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetsAccount() throws Exception {

        createAccount();

        String responseBody = mockMvc
                .perform(get(LOGIN_URI))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Account retrievedAccount = mapper.readValue(responseBody, Account.class);

        assertAccountHasExpectedValues(retrievedAccount, account);
    }

    @Test
    public void testDoesNotReturnPasswordWhenAccountIsRetrieved() throws Exception {

        createAccount();

        String responseBody = mockMvc
                .perform(get(LOGIN_URI))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(responseBody.toLowerCase(), not(containsString("password")));
        assertThat(responseBody, not(containsString(account.getPassword())));
    }

    @Test
    public void testIsAwareOfInexistentAccountsOnLookup() throws Exception {

        mockMvc.perform(get(LOGIN_URI)).andExpect(status().isNotFound());
    }

    @Test
    public void testRetrievesAccounts() throws Exception {

        createAccounts("user1", "user2");

        mockMvc
            .perform(get(BASE_URI)).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].login", equalTo("user1")))
            .andExpect(jsonPath("$[1].login", equalTo("user2")));
    }

    private void createAccounts(String... logins) {

        for (String login : logins) {

            Account account = getAccountFixture();
            account.setLogin(login);

            accountRepository.saveAndFlush(account);
        }
    }

    @Test
    public void testReturnsNotFoundWhenTryingToUpdatePasswordForInexistentAccount()
            throws Exception {

        AccountPasswordRequest accountPassword = new AccountPasswordRequest("a", "b", "b");

        String accountPasswordJson = mapper.writeValueAsString(accountPassword);

        mockMvc.perform(put(PASSWORD_URI).content(accountPasswordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testReturnsForbiddenWhenOldPasswordDoesNotMatch()
            throws Exception {

        createAccount();

        AccountPasswordRequest accountPassword =
                new AccountPasswordRequest("a", NEW_PASSWORD, NEW_PASSWORD);

        String accountPasswordJson = mapper.writeValueAsString(accountPassword);

        mockMvc.perform(put(PASSWORD_URI).content(accountPasswordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", equalTo("Incorrect password")));
    }

    @Test
    @Parameters
    public void testValidatesPasswordFields(String oldPassword, String password,
            String confirmPassword, String fieldName, String message) throws Exception {

        createAccount();

        AccountPasswordRequest accountPassword =
                new AccountPasswordRequest(oldPassword, password, confirmPassword);

        String accountPasswordJson = mapper.writeValueAsString(accountPassword);

        mockMvc.perform(put(PASSWORD_URI).content(accountPasswordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo(fieldName)))
                .andExpect(jsonPath("$.validationErrors[0].message", equalTo(message)));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestValidatesPasswordFields() {

        return new Object[][] {

            { null, NEW_PASSWORD, NEW_PASSWORD, "oldPassword", "must not be null" },
            { PASSWORD, NEW_PASSWORD, "foo", null, "passwords don't match" },
            { PASSWORD, "short", "short", "password", "must be at least 8 characters length" }
        };
    }

    @Test
    public void testUpdatesPassword() throws Exception {

        createAccount();

        AccountPasswordRequest accountPassword =
                new AccountPasswordRequest(PASSWORD, NEW_PASSWORD, NEW_PASSWORD);

        String accountPasswordJson = mapper.writeValueAsString(accountPassword);

        mockMvc.perform(put(PASSWORD_URI).content(accountPasswordJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Account savedAccount = accountRepository.findById(account.getLogin()).get();

        assertThat(passwordEncoder.matches(NEW_PASSWORD, savedAccount.getPassword()), is(true));
    }

    @Test
    public void testValidatesPublicKeyOnAccountCreation() throws Exception {

        configurePublicKeyValidatorToReturn(false);

        accountRequestJson = mapper.writeValueAsString(accountRequest);

        mockMvc.perform(
                post(BASE_URI).content(accountRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("publicKey")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("public key is not valid")));
    }

    @Test
    public void testValidatesPublicKeyOnAccountUpdate() throws Exception {

        configurePublicKeyValidatorToReturn(false);
        createAccount();

        accountRequest.setPublicKey("modifiedKey");
        accountRequestJson = mapper.writeValueAsString(accountRequest);

        mockMvc.perform(
                put(LOGIN_URI).content(accountRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("publicKey")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("public key is not valid")));
    }
}
