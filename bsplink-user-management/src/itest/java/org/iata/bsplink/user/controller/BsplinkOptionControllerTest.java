package org.iata.bsplink.user.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkOptionRepository;
import org.iata.bsplink.user.utils.BaseUserTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BsplinkOptionControllerTest extends BaseUserTest {

    private static final String BASE_URI = "/v1/bsplinkOptions";

    @Autowired
    private BsplinkOptionRepository repository;

    @Autowired
    protected WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    private List<BsplinkOption> options;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();

        repository.deleteAll();

        BsplinkOption option1 = new BsplinkOption();
        option1.setId("OPT1");
        option1.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        repository.save(option1);

        BsplinkOption option2 = new BsplinkOption();
        option2.setId("OPT2");
        option2.setUserTypes(Arrays.asList(UserType.AIRLINE, UserType.BSP));
        repository.save(option2);

        BsplinkOption option3 = new BsplinkOption();
        option3.setId("OPT3");
        option3.setUserTypes(Arrays.asList(UserType.AGENT));
        repository.save(option3);

        options = repository.findAll();
    }


    @Test
    public void testGetOptions() throws Exception {

        String responseBody = mockMvc.perform(get(BASE_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<BsplinkOption> optionsRead =
                MAPPER.readValue(responseBody, new TypeReference<List<BsplinkOption>>() {});

        assertThat(optionsRead, equalTo(options));
    }


    @Test
    public void testGetOptionsByUserType() throws Exception {

        List<BsplinkOption> options = repository.findByUserTypes(UserType.AGENT);

        String responseBody = mockMvc
                .perform(get(BASE_URI + "?userType=AGENT").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<BsplinkOption> optionsRead =
                MAPPER.readValue(responseBody, new TypeReference<List<BsplinkOption>>() {});

        assertThat(optionsRead.stream().map(BsplinkOption::getId).collect(Collectors.toList()),
                equalTo(options.stream().map(BsplinkOption::getId).collect(Collectors.toList())));
    }


    @Test
    public void testGetOptionsByUserTypeFullView() throws Exception {
        List<BsplinkOption> options = repository.findByUserTypes(UserType.AGENT);

        String json = MAPPER.writeValueAsString(options);

        String responseBody =
                mockMvc.perform(get(BASE_URI + "?userType=AGENT&fullView")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<BsplinkOption> optionsRead =
                MAPPER.readValue(responseBody, new TypeReference<List<BsplinkOption>>() {});

        assertThat(optionsRead, equalTo(options));
        assertThat(responseBody, equalTo(json));
    }


    @Test
    public void testGetOption() throws Exception {

        BsplinkOption option = options.get(0);

        String responseBody = mockMvc
                .perform(get(BASE_URI + "/" + option.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        BsplinkOption optionRead = MAPPER.readValue(responseBody, BsplinkOption.class);

        assertThat(optionRead, equalTo(option));
    }


    @Test
    public void testGetOptionNotFound() throws Exception {

        mockMvc.perform(get(BASE_URI + "/OPTION_NOT_FOUND").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
