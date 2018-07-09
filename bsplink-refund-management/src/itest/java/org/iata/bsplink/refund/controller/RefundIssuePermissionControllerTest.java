package org.iata.bsplink.refund.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.refund.test.fixtures.RefundIssuePermissionFixtures.getRefundIssuePermissions;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.model.repository.RefundIssuePermissionRepository;
import org.iata.bsplink.refund.validation.RefundIssuePermissionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RefundIssuePermissionControllerTest {

    private static final String BASE_URI = "/v1/refunds/indirects/permissions";
    private static final String NON_EXISTENT_REFUND_URI = "/v1/refunds/indirects/permissions/9999";

    private List<RefundIssuePermission> refundIssuePermissions;
    private RefundIssuePermission refundIssuePermission;
    private String refundIssuePermissionJson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RefundIssuePermissionRepository refundIssuePermissionRepository;

    @MockBean
    private RefundIssuePermissionValidator refundIssuePermissionValidator;


    @Before
    public void setUp() throws Exception {
        refundIssuePermissions = getRefundIssuePermissions();
        refundIssuePermission = refundIssuePermissions.get(0);
        refundIssuePermissionJson = mapper.writeValueAsString(refundIssuePermission);
    }


    @Test
    public void testCreatesRefundIssuePermission() throws Exception {
        refundIssuePermissionRepository.deleteAll();
        mockMvc.perform(post(BASE_URI).content(refundIssuePermissionJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        List<RefundIssuePermission> refundIssuePermissions =
                refundIssuePermissionRepository.findAll();
        assertThat(refundIssuePermissions, hasSize(1));
        assertThat(refundIssuePermissions.get(0), equalTo(refundIssuePermission));
    }


    @Test
    public void testReturnsCreatedRefundIssuePermission() throws Exception {
        String responseBody = mockMvc
                .perform(post(BASE_URI).content(refundIssuePermissionJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn().getRequest().getContentAsString();
        assertThat(readRefundIssuePermissionFromJson(responseBody), equalTo(refundIssuePermission));
    }

    @Test
    public void testReturnsCreatedRefundIssuePermissionConflict() throws Exception {
        refundIssuePermissionRepository.save(refundIssuePermission);
        mockMvc.perform(post(BASE_URI).content(refundIssuePermissionJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());

    }


    private RefundIssuePermission readRefundIssuePermissionFromJson(String json) throws Exception {
        return mapper.readValue(json, RefundIssuePermission.class);
    }


    @Test
    public void testValidatesRefundIssuePermission() throws Exception {
        refundIssuePermission.setAirlineCode(null);
        String refundIssuePermissionJson = mapper.writeValueAsString(refundIssuePermission);
        mockMvc.perform(post(BASE_URI).content(refundIssuePermissionJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }


    @Test
    public void testListsRefundIssuePermissions() throws Exception {
        createRefundIssuePermissions();
        String responseBody = mockMvc.perform(get(BASE_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<RefundIssuePermission> actual =
                mapper.readValue(responseBody, new TypeReference<List<RefundIssuePermission>>() {});
        assertThat(actual, equalTo(refundIssuePermissions));
    }


    private List<RefundIssuePermission> createRefundIssuePermissions() {
        List<RefundIssuePermission> savedRefundIssuePermissions =
                refundIssuePermissionRepository.saveAll(refundIssuePermissions);
        refundIssuePermissionRepository.flush();
        return savedRefundIssuePermissions;
    }


    @Test
    public void testDeletesRefundIssuePermission() throws Exception {
        RefundIssuePermission refundIssuePermissionToDelete = createRefundIssuePermissions().get(0);
        Long refundIssuePermissionId = refundIssuePermissionToDelete.getId();
        mockMvc.perform(delete(BASE_URI + "/" + refundIssuePermissionId)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        assertThat(refundIssuePermissionRepository.findById(refundIssuePermissionId).isPresent(),
                is(false));
    }


    @Test
    public void testReturnsNotFoundWhenTryingToDeleteNonExistentRefundIssuePermission()
            throws Exception {
        mockMvc.perform(delete(NON_EXISTENT_REFUND_URI)).andExpect(status().isNotFound());
    }


    @Test
    public void testGetsRefundIssuePermission() throws Exception {
        RefundIssuePermission refundIssuePermission = createRefundIssuePermissions().get(0);
        String responseBody = mockMvc
                .perform(get(BASE_URI + "/" + refundIssuePermission.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        RefundIssuePermission actual = mapper.readValue(responseBody, RefundIssuePermission.class);
        assertThat(actual, equalTo(refundIssuePermission));
    }


    @Test
    public void testReturnsNotFoundWhenTryingToRetrieveNonExistentRefundIssuePermission()
            throws Exception {
        mockMvc.perform(get(NON_EXISTENT_REFUND_URI)).andExpect(status().isNotFound());
    }


    @Test
    public void testListsRefundIssuePermissionsByIsoCountryCodeAndAirlineCode() throws Exception {
        createRefundIssuePermissions();
        String isoc = refundIssuePermissions.get(0).getIsoCountryCode();
        String airline = refundIssuePermissions.get(0).getAirlineCode();

        List<RefundIssuePermission> filteredPermissions = refundIssuePermissions.stream().filter(
            rip -> isoc.equals(rip.getIsoCountryCode()) && airline.equals(rip.getAirlineCode()))
                .collect(Collectors.toList());


        String responseBody = mockMvc
                .perform(get(BASE_URI + "?isoCountryCode=" + isoc + "&airlineCode=" + airline)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<RefundIssuePermission> actual =
                mapper.readValue(responseBody, new TypeReference<List<RefundIssuePermission>>() {});
        assertThat(actual, equalTo(filteredPermissions));
    }


    @Test
    public void testListsRefundIssuePermissionsByIsoCountryCodeAndAirlineCodeAndAgentCode()
            throws Exception {
        RefundIssuePermission refundIssuePermission = createRefundIssuePermissions().get(0);
        String isoc = refundIssuePermissions.get(0).getIsoCountryCode();
        String airline = refundIssuePermissions.get(0).getAirlineCode();
        String agent = refundIssuePermissions.get(0).getAgentCode();
        String responseBody = mockMvc
                .perform(get(BASE_URI + "?isoCountryCode=" + isoc + "&airlineCode=" + airline
                        + "&agentCode=" + agent).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        RefundIssuePermission actual = mapper.readValue(responseBody, RefundIssuePermission.class);
        assertThat(actual, equalTo(refundIssuePermission));
    }


    @Test
    public void testListsRefundIssuePermissionsByIsoCountryCodeAndAirlineCodeAndAgentCodeNotFound()
            throws Exception {
        mockMvc.perform(get(BASE_URI + "?isoCountryCode=AA&airlineCode=220&agentCode=99999992"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testValidatorIsInvokedOnCreation() throws Exception {
        when(refundIssuePermissionValidator.supports(RefundIssuePermission.class)).thenReturn(true);
        mockMvc.perform(post(BASE_URI).content(refundIssuePermissionJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        verify(refundIssuePermissionValidator).validate(any(RefundIssuePermission.class),
                any(Errors.class));
    }
}
