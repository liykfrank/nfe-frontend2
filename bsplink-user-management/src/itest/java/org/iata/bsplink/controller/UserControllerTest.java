package org.iata.bsplink.controller;

import static org.hamcrest.CoreMatchers.equalTo;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Optional;
import javax.transaction.Transactional;

import org.iata.bsplink.Application;
import org.iata.bsplink.model.entity.User;
import org.iata.bsplink.model.entity.UserType;
import org.iata.bsplink.model.repository.UserRepository;
import org.iata.bsplink.pojo.Agent;
import org.iata.bsplink.pojo.Airline;
import org.iata.bsplink.service.AgentService;
import org.iata.bsplink.service.AirlineService;
import org.iata.bsplink.service.UserService;
import org.iata.bsplink.utils.BaseUserTest;
import org.iata.bsplink.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

    @Autowired
    protected WebApplicationContext webAppContext;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MockMvc mockMvc;

    /**
     * Initializes the required variables before running the tests.
     *
     * @throws Exception the exception thrown during the initialization
     */
    @Before
    public void init() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();

        user = new User();
        createUser();

        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(agentService.findAgent(any())).thenReturn(new Agent());
        when(airlineService.findAirline(any(), any())).thenReturn(new Airline());
    }

    @Test
    public void getUserTest() throws Exception {

        Optional<User> optional = Optional.of(user);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        mockMvc.perform(get(GET_USER_URL, USER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(user)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void getUserTestNotFound() throws Exception {

        mockMvc.perform(get(GET_USER_URL, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createUserTest() throws Exception {

        doReturn(user).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(user)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void createUserUserCodeBadRequestTest() throws Exception {

        doReturn(user).when(userService).createUser(any(), any());

        String userRequestString = TestUtils.getJson("/mock/requests/create-user-request.json");

        User userToSave = MAPPER.readValue(userRequestString, User.class);
        userToSave.setUserCode("123.");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createThirdPartyUserTestBadRequest() throws Exception {

        doReturn(user).when(userService).createUser(any(), any());

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

        doReturn(user).when(userService).createUser(any(), any());

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

        doReturn(user).when(userService).createUser(any(), any());

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

        doReturn(user).when(userService).createUser(any(), any());

        String userRequestString =
                TestUtils.getJson("/mock/requests/create-user-agent-request.json");

        mockMvc.perform(post(CREATE_USER_URL).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void updateUserTest() throws Exception {

        doReturn(user).when(userService).updateUser(any(), any());

        Optional<User> optional = Optional.of(user);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        String userRequestString = TestUtils.getJson("/mock/requests/update-user-request.json");

        mockMvc.perform(put(UPDATE_USER_URL, USER_ID).contentType(MediaType.APPLICATION_JSON)
                .content(userRequestString)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(user)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void updateUserBadRequestTest() throws Exception {

        doReturn(user).when(userService).updateUser(any(), any());

        Optional<User> optional = Optional.of(user);

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

        Optional<User> optional = Optional.of(user);

        when(userService.getUser(USER_ID)).thenReturn(optional);

        doNothing().when(userService).deleteUser(user);

        mockMvc.perform(delete(DELETE_USER_URL, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteUserTestNotFound() throws Exception {

        doNothing().when(userService).deleteUser(user);

        mockMvc.perform(delete(DELETE_USER_URL, USER_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
