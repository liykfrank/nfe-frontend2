package org.iata.bsplink.exception.handler;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.iata.bsplink.commons.rest.exception.ApplicationException;
import org.iata.bsplink.commons.rest.response.ApplicationErrorResponse;
import org.iata.bsplink.test.dummy.DummyApplication;
import org.iata.bsplink.test.dummy.DummyExceptionController;
import org.iata.bsplink.test.dummy.DummyModel;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(classes = DummyApplication.class)
@AutoConfigureMockMvc
public class ApplicationExceptionHandlerTest {

    private static final String EXCEPTION_URL = "/exception";
    private static final String MODEL_URL = "/model";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DummyExceptionController exceptionController;

    @Rule
    public OutputCapture capture = new OutputCapture();

    private long timestampReference = Instant.now().toEpochMilli();

    @Test
    @Parameters(method = "useCasesForExceptionHandling")
    public void testCreatesExpectedErrorMessages(HttpStatus status, String message,
            Exception exception) throws Exception {

        when(exceptionController.index()).thenThrow(exception);

        String responseBody = mockMvc.perform(get(EXCEPTION_URL))
                .andExpect(status().is(status.value()))
                .andReturn().getResponse().getContentAsString();

        ApplicationErrorResponse error =
                objectMapper.readValue(responseBody, ApplicationErrorResponse.class);

        assertThat(error.getTimestamp(), greaterThanOrEqualTo(timestampReference));
        assertThat(error.getStatus(), equalTo(status.value()));
        assertThat(error.getError(), equalTo(status.getReasonPhrase()));
        assertThat(error.getMessage(), equalTo(message));
        assertThat(error.getPath(), equalTo(EXCEPTION_URL));
    }

    @SuppressWarnings("unused")
    private Object[][] useCasesForExceptionHandling() {

        return new Object[][] {

            // test that handles an ApplicationException
            {
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                new ApplicationException("Invalid request", HttpStatus.BAD_REQUEST)
            },
            // test that handles an unchecked exception
            {
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                new RuntimeException()
            },
            // test that handles a checked exception
            {
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                new Exception()
            },
        };
    }

    @Test
    @Parameters(method = "useCasesForExceptionHandling")
    public void testLogsResponse(HttpStatus status, String message, Exception exception)
            throws Exception {

        when(exceptionController.index()).thenThrow(exception);

        mockMvc.perform(get(EXCEPTION_URL));

        String applicationErrorClassName = ApplicationErrorResponse.class.getSimpleName();

        List<String> expected = Arrays.asList(
                applicationErrorClassName,
                Integer.valueOf(status.value()).toString(),
                message);

        assertThat(capture.toString(), stringContainsInOrder(expected));
    }

    @Test
    @Parameters
    public void testLogsExceptionCause(Exception exception) throws Exception {

        when(exceptionController.index()).thenThrow(exception);

        mockMvc.perform(get(EXCEPTION_URL));

        List<String> expected = Arrays.asList(
                exception.getClass().getName(),
                exception.getMessage());

        assertThat(capture.toString(), stringContainsInOrder(expected));
    }

    @SuppressWarnings("unused")
    private Object[][] parametersForTestLogsExceptionCause() {

        return new Object[][] {
            {
                new RuntimeException("Any RuntimeException")
            },
            {
                new Exception("Any Exception")
            },
            {
                new ApplicationException("Any ApplicationException", HttpStatus.I_AM_A_TEAPOT)
            },
            {
                new HttpMessageNotReadableException("Any Spring Boot standard exception")
            }
        };
    }

    @Test
    public void testCreatesExpectedValidationErrorMessage() throws Exception {

        DummyModel model = new DummyModel();
        model.setField1("a string longer than expected");

        String modelJson = objectMapper.writeValueAsString(model);

        mockMvc.perform(

                post(MODEL_URL).content(modelJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo(HttpStatus.BAD_REQUEST.getReasonPhrase())))
                .andExpect(jsonPath("$.path", equalTo(MODEL_URL)))

                .andExpect(jsonPath("$.validationErrors", hasSize(1)))

                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("field1")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("field should be 2 characters length")));
    }

    @Test
    public void testGetsAllValidationErrors() throws Exception {

        DummyModel model = new DummyModel();
        model.setField1("a string longer than expected");
        model.setField2(99999);

        String modelJson = objectMapper.writeValueAsString(model);

        mockMvc.perform(
                post(MODEL_URL).content(modelJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", hasSize(2)));
    }

    @Test
    public void testNotApplicationExceptionsMessagesAreNotShownInTheResponse() throws Exception {

        final String exceptionMessage = "Exception message";

        HttpMessageNotReadableException exception =
                new HttpMessageNotReadableException(exceptionMessage);

        when(exceptionController.index()).thenThrow(exception);

        String responseBody = mockMvc.perform(get(EXCEPTION_URL))
                .andReturn().getResponse().getContentAsString();

        ApplicationErrorResponse error =
                objectMapper.readValue(responseBody, ApplicationErrorResponse.class);

        assertThat(error.getMessage(), not(containsString(exceptionMessage)));
    }
}
