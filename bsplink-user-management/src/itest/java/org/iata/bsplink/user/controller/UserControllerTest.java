package org.iata.bsplink.user.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsArgAt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Optional;

import javax.transaction.Transactional;

import org.iata.bsplink.user.model.entity.User;
import org.iata.bsplink.user.model.entity.UserPreferences;
import org.iata.bsplink.user.model.entity.UserRegion;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.UserRepository;
import org.iata.bsplink.user.pojo.Agent;
import org.iata.bsplink.user.pojo.Airline;
import org.iata.bsplink.user.preferences.Languages;
import org.iata.bsplink.user.service.AgentService;
import org.iata.bsplink.user.service.AirlineService;
import org.iata.bsplink.user.service.KeycloakService;
import org.iata.bsplink.user.service.UserService;
import org.iata.bsplink.user.utils.BaseUserTest;
import org.iata.bsplink.user.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserControllerTest extends BaseUserTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AgentService agentService;

    @MockBean
    private AirlineService airlineService;

    @MockBean
    private KeycloakService keycloakService;

    @Autowired
    protected WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    /**
     * Initializes the required variables before running the tests.
     *
     * @throws Exception the exception thrown during the initialization
     */
    @Before
    public void init() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();

        userPending = new User();
        initiatePendingUser();
        userCreated = new User();
        initiateCreatedUser();

        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(agentService.findAgent(any())).thenReturn(new Agent());
        when(airlineService.findAirline(any(), any())).thenReturn(new Airline());
    }

    @Test
    public void getUserTest() throws Exception {

        Optional<User> optional = Optional.of(userPending);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        mockMvc.perform(get(GET_USER_URL, USER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(userPending)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void getUserTestNotFound() throws Exception {

        mockMvc.perform(get(GET_USER_URL, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUsersByTypeTest() throws Exception {

        when(userService.findByUserType(UserType.AGENT)).thenReturn(getUsersList());

        mockMvc.perform(get(BASE_USER_URL).param("userType", UserType.AGENT.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(getUsersList())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void createUserTest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(userPending)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void createUserAirlineNotFoundTest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());
        when(airlineService.findAirline(any(String.class), any(String.class))).thenReturn(null);

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("userCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("Airline does not exist.")));
    }


    @Test
    public void createUserTestUserAlreadyExists() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userCreated));

        when(keycloakService.findUser(any(User.class))).thenReturn(new UserRepresentation());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("username")))
                .andExpect(
                        jsonPath("$.validationErrors[0].message", equalTo("User already exists.")));
    }


    @Test
    public void createUserUsernameBadRequestTest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUsername("not-well-formed-user");
        userToSave.setUserCode("782000101");
        userToSave.setUserType(UserType.AGENT);

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("username")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("must be a well-formed email address")));
    }

    @Test
    public void createUserAgentNotExists() throws Exception {

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");
        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUserCode("78200021");
        userToSave.setUserType(UserType.AGENT);

        doReturn(userPending).when(userService).createUser(any(), any());
        when(agentService.findAgent(userToSave.getUserCode())).thenReturn(null);

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("userCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("The agent does not exist.")));
    }

    @Test
    public void createUserUserCodeBadRequestTest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUserCode("123.");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createThirdPartyUserTestBadRequest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUserType(UserType.THIRDPARTY);
        userToSave.setUserCode("1234.");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("userCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("Length must be grather than 6")));
    }

    @Test
    public void createAgentUserTestBadRequest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUserType(UserType.AGENT);
        userToSave.setUserCode("123BADS");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("userCode")))
                .andExpect(jsonPath("$.validationErrors[0].message", equalTo("Incorrect Format.")));
    }

    @Test
    public void createDpcUserTestBadRequest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUserType(UserType.DPC);
        userToSave.setUserCode("123456");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("userCode")))
                .andExpect(
                        jsonPath("$.validationErrors[0].message", equalTo("Field must be null")));
    }

    @Test
    public void createUserBadRequestTest() throws Exception {

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-bad-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createUserAgentTest() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString =
                TestUtils.getJson("/mock/requests/create-user-agent-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void updateUserTest() throws Exception {

        doReturn(userPending).when(userService).updateUser(any(), any(), any());

        Optional<User> optional = Optional.of(userPending);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        String userRequestString = TestUtils.getJson("/mock/requests/update-user-request.json");

        mockMvc.perform(put(UPDATE_USER_URL, USER_ID).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(userPending)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUserBadRequestTest() throws Exception {

        doReturn(userPending).when(userService).updateUser(any(), any(), any());

        Optional<User> optional = Optional.of(userPending);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        String userRequestString = TestUtils.getJson("/mock/requests/update-user-bad-request.json");

        mockMvc.perform(put(UPDATE_USER_URL, USER_ID).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateUserNotFoundTest() throws Exception {

        String userRequestString = TestUtils.getJson("/mock/requests/update-user-request.json");

        mockMvc.perform(put(UPDATE_USER_URL, USER_ID).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteUserTest() throws Exception {

        Optional<User> optional = Optional.of(userPending);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        doNothing().when(userService).deleteUser(userPending);

        mockMvc.perform(delete(DELETE_USER_URL, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteUserTestNotFound() throws Exception {

        doNothing().when(userService).deleteUser(userPending);

        mockMvc.perform(delete(DELETE_USER_URL, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testReturnsUserPreferencesWhenUserIsCreated() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        String savedUserJson = mockMvc.perform(
                post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(userPending)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();

        User savedUser = MAPPER.readValue(savedUserJson, User.class);

        assertThat(savedUser.getPreferences(), notNullValue());
    }

    @Test
    public void testReturnsUserPreferences() throws Exception {

        when(userService.getUser(USER_ID)).thenReturn(Optional.of(userCreated));

        String userPreferencesJson = mockMvc.perform(get(USER_PREFERENCES, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserPreferences savedUserPreferences =
                MAPPER.readValue(userPreferencesJson, UserPreferences.class);

        assertThat(savedUserPreferences.getLanguage(),
                equalTo(userCreated.getPreferences().getLanguage()));
        assertThat(savedUserPreferences.getTimeZone(),
                equalTo(userCreated.getPreferences().getTimeZone()));
    }

    @Test
    public void testUpdatesUserPreferences() throws Exception {

        when(userService.getUser(USER_ID)).thenReturn(Optional.of(userCreated));
        when(userService.updateUserPreferences(any(User.class), any(UserPreferences.class)))
                .thenAnswer((returnsArgAt(1)));

        String userRequestString = TestUtils.getJson("/mock/requests/update-user-preferences.json");

        String savedUserPreferencesJson = mockMvc.perform(put(USER_PREFERENCES, USER_ID)
                .contentType(MediaType.APPLICATION_JSON).content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserPreferences savedUserPreferences =
                MAPPER.readValue(savedUserPreferencesJson, UserPreferences.class);

        assertThat(savedUserPreferences.getLanguage(), equalTo(Languages.DE.toString()));
        assertThat(savedUserPreferences.getTimeZone(), equalTo("Europe/Berlin"));
        assertThat(savedUserPreferences.getRegions(), hasSize(2));

        UserRegion region0 = savedUserPreferences.getRegions().get(0);
        assertThat(region0.getName(), equalTo("Europe"));
        assertThat(region0.isDefault(), equalTo(true));
        assertThat(region0.getIsoCountryCodes(), hasSize(3));

        UserRegion region1 = savedUserPreferences.getRegions().get(1);
        assertThat(region1.getName(), equalTo("America"));
        assertThat(region1.isDefault(), equalTo(false));
        assertThat(region1.getIsoCountryCodes(), hasSize(3));
    }

    @Test
    public void testValidatesUserPreferences() throws Exception {

        when(userService.getUser(USER_ID)).thenReturn(Optional.of(userCreated));
        when(userService.updateUserPreferences(any(User.class), any(UserPreferences.class)))
                .thenAnswer((returnsArgAt(1)));

        String userRequestString =
                TestUtils.getJson("/mock/requests/update-wrong-user-preferences.json");

        mockMvc.perform(put(USER_PREFERENCES, USER_ID)
                .contentType(MediaType.APPLICATION_JSON).content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].message",
                        is("Not supported language preference 'XX'")));
    }

    @Test
    public void testValidatesUserPreferencesWhenUserIsCreated() throws Exception {

        doReturn(userPending).when(userService).createUser(any(), any());

        String userRequestString =
                TestUtils.getJson("/mock/requests/create-user-request-with-wrong-preferences.json");

        mockMvc.perform(post(CREATE_USER_URL)
                .contentType(MediaType.APPLICATION_JSON).content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].message",
                        is("Not supported language preference 'XX'")));
    }

}
