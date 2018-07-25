package org.iata.bsplink.agencymemo.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.agencymemo.test.fixtures.ReasonFixtures.getReasons;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.iata.bsplink.agencymemo.model.entity.Reason;
import org.iata.bsplink.agencymemo.model.repository.ReasonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReasonControllerTest {

    private static final String BASE_URI = "/v1/reasons";
    private static final String NON_EXISTENT_REASON_URI = "/v1/reasons/9999";

    private List<Reason> reasons;
    private Reason reason;
    private String reasonJson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ReasonRepository reasonRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {

        reasons = getReasons();
        reason = reasons.get(0);
        reasonJson = mapper.writeValueAsString(reason);
    }

    @Test
    public void testCreatesReason() throws Exception {

        mockMvc.perform(
                post(BASE_URI).content(reasonJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<Reason> reasons = reasonRepository.findAll();

        assertThat(reasons, hasSize(1));
        assertThat(reasons.get(0), equalTo(reason));
    }

    @Test
    public void testReturnsCreatedReason() throws Exception {

        String responseBody = mockMvc.perform(
                post(BASE_URI).content(reasonJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getRequest().getContentAsString();

        assertThat(readReasonFromJson(responseBody), equalTo(reason));
    }

    private Reason readReasonFromJson(String json) throws Exception {

        return mapper.readValue(json, Reason.class);
    }

    @Test
    public void testValidatesReason() throws Exception {

        reason.setDetail(null);

        String reasonJson = mapper.writeValueAsString(reason);

        mockMvc.perform(
                post(BASE_URI).content(reasonJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testListsReasons() throws Exception {

        createReasons();

        String responseBody = mockMvc.perform(
                get(BASE_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Reason> actual = mapper.readValue(responseBody, new TypeReference<List<Reason>>() {});

        assertThat(actual, equalTo(reasons));
    }

    @Test
    public void testListsReasonsByIsoCountryCode() throws Exception {

        createReasons();

        String isoc = reasons.get(0).getIsoCountryCode();

        String responseBody = mockMvc.perform(
                get(BASE_URI + "?isoCountryCode=" + isoc).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Reason> actual = mapper.readValue(responseBody, new TypeReference<List<Reason>>() {});

        List<Reason> filteredReasons = reasons.stream()
                .filter(r -> isoc.equals(r.getIsoCountryCode()))
                .collect(Collectors.toList());
        assertThat(actual, equalTo(filteredReasons));
    }

    private List<Reason> createReasons() {

        List<Reason> savedReasons = reasonRepository.saveAll(reasons);
        reasonRepository.flush();

        return savedReasons;
    }

    @Test
    public void testDeletesReason() throws Exception {

        Reason reasonToDelete = createReasons().get(0);
        Long reasonId = reasonToDelete.getId();

        mockMvc.perform(
                delete(BASE_URI + "/" + reasonId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(reasonRepository.findById(reasonId).isPresent(), is(false));
    }

    @Test
    public void testReturnsReasonOnDeletion() throws Exception {

        Reason reasonToDelete = createReasons().get(0);
        Long reasonId = reasonToDelete.getId();

        String responseBody = mockMvc.perform(
                delete(BASE_URI + "/" + reasonId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(readReasonFromJson(responseBody), equalTo(reasonToDelete));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToDeleteNonExistentReason() throws Exception {

        mockMvc.perform(delete(NON_EXISTENT_REASON_URI)).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatesReason() throws Exception {

        Reason reasonToUpdate = createReasons().get(0);
        entityManager.detach(reasonToUpdate);

        reasonToUpdate.setDetail("updated");

        String reasonToUpdateJson = mapper.writeValueAsString(reasonToUpdate);

        mockMvc.perform(
                put(BASE_URI + "/" + reasonToUpdate.getId())
                .content(reasonToUpdateJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Reason actual = reasonRepository.findById(reasonToUpdate.getId()).get();

        assertThat(actual.getDetail(), equalTo(reasonToUpdate.getDetail()));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToUpdateNonExistentReason() throws Exception {

        mockMvc.perform(
                put(NON_EXISTENT_REASON_URI).contentType(MediaType.APPLICATION_JSON)
                .content(reasonJson)).andExpect(status().isNotFound());
    }

    @Test
    public void testReturnsReasonOnUpdate() throws Exception {

        Reason reasonToUpdate = createReasons().get(0);
        entityManager.detach(reasonToUpdate);

        reasonToUpdate.setDetail("updated");

        String reasonToUpdateJson = mapper.writeValueAsString(reasonToUpdate);

        String responseBody = mockMvc.perform(
                put(BASE_URI + "/" + reasonToUpdate.getId())
                .content(reasonToUpdateJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(readReasonFromJson(responseBody), equalTo(reasonToUpdate));
    }

    @Test
    public void testGetsReason() throws Exception {

        Reason reason = createReasons().get(0);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + reason.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Reason actual = mapper.readValue(responseBody, Reason.class);

        assertThat(actual, equalTo(reason));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToRetrieveNonExistentReason() throws Exception {

        mockMvc.perform(get(NON_EXISTENT_REASON_URI)).andExpect(status().isNotFound());
    }

}
