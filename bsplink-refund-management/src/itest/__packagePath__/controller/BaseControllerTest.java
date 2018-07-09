package @packageName@.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;

import @packageName@.Application;
import @packageName@.pojo.BaseResponse;
import @packageName@.service.ResourceService;
import @packageName@.utils.BaseTest;
import @packageName@.utils.ResourceUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BaseControllerTest extends BaseTest {

    @MockBean
    private ResourceService service;

    @Autowired
    protected WebApplicationContext webAppContext;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private MockMvc mockMvc;

    private String requestString;

    /**
     * Initializes the required variables before running the tests.
     *
     * @throws Exception the exception thrown during the initialization
     */
    @Before
    public void init() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();

        response = new BaseResponse();
        response.setId(RESOURCE_ID);
        response.setDescription(DESCRIPTION);
        response.setCreated(CREATED);

        responseList = new ArrayList<>();
        responseList.add(response);

        createResource();
        
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Get resource list test.
     *
     * @throws Exception the exception thrown during the test execution
     */
    @Test
    public void getResourceListTest() throws Exception {

        doReturn(responseList).when(service).getResourceList();

        mockMvc.perform(get(GET_RESOURCE_LIST_URL)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(responseList)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Get a specific resource test.
     *
     * @throws Exception the exception thrown during the test execution
     */
    @Test
    public void getResourceTest() throws Exception {

        doReturn(response).when(service).getResource(RESOURCE_ID);

        mockMvc.perform(get(GET_RESOURCE_URL, RESOURCE_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(response)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Create a new resource test.
     *
     * @throws Exception the exception thrown during the test execution
     */
    @Test
    public void createResourceTest() throws Exception {

        doReturn(response).when(service).createResource(any());

        requestString = ResourceUtils.getJson("/mock/requests/create-resource-request.json");

        mockMvc.perform(post(CREATE_RESOURCE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(requestString)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(response)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    
    /**
     * Update resource test.
     *
     * @throws Exception the exception thrown during the test execution
     */
    @Test
    public void updateResourceTest() throws Exception {

        doReturn(response).when(service).updateResource(eq(RESOURCE_ID), any());

        requestString = ResourceUtils.getJson("/mock/requests/update-resource-request.json");

        mockMvc.perform(put(GET_RESOURCE_URL, RESOURCE_ID).contentType(MediaType.APPLICATION_JSON)
                .content(requestString)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(MAPPER.writer().writeValueAsString(response)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    /**
     * Delete resource test.
     *
     * @throws Exception the exception thrown during the test execution
     */
    @Test
    public void deleteResourceTest() throws Exception {

        doNothing().when(service).deleteResource(RESOURCE_ID);

        mockMvc.perform(delete(GET_RESOURCE_URL, RESOURCE_ID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
