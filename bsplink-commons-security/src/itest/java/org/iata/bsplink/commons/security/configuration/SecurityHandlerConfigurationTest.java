package org.iata.bsplink.commons.security.configuration;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.iata.bsplink.test.dummy.DummyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DummyApplication.class)
public class SecurityHandlerConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testExceptionHandlerBeanIsLoaded() {
        assertThat(applicationContext.getBean(SecurityHandlerConfiguration.class), notNullValue());
    }

}
