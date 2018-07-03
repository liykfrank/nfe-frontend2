package org.iata.bsplink.ssoserviceprovider;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SsoServiceProviderApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private static final String USER = "user";

    private static final String PASSWORD = "password";

    @Before
    public void setUp() {
        MockMvcBuilders.webAppContextSetup(this.context)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void contextLoads() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void testMetadata() throws Exception {
        mockMvc.perform(get("http://localhost:8081/saml/metadata").with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testSso() throws Exception {
        mockMvc.perform(post("http://localhost:8081/saml/SSO").with(httpBasic(USER, PASSWORD)))
                .andExpect(status().is3xxRedirection());
    }

}
