package org.iata.bsplink.commons.rest.configuration;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.iata.bsplink.commons.rest.exception.handler.ApplicationExceptionHandler;
import org.iata.bsplink.test.dummy.DummyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DummyApplication.class)
public class ExceptionHandlerConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testExceptionHandlerBeanIsLoaded() {

        assertThat(applicationContext.getBean(ApplicationExceptionHandler.class), notNullValue());
    }

}
