package org.iata.bsplink.agencymemo.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.agencymemo.test.fixtures.TaxOnCommissionTypeFixtures.getTaxOnCommissionTypes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
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

import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionType;
import org.iata.bsplink.agencymemo.model.entity.TaxOnCommissionTypePk;
import org.iata.bsplink.agencymemo.model.repository.TaxOnCommissionTypeRepository;
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
public class TaxOnCommissionTypeControllerTest {

    private static final String BASE_URI = "/v1/tctps";
    private static final String NON_EXISTENT_REASON_URI = "/v1/tctps/XX/NIXEX";

    private List<TaxOnCommissionType> taxOnCommissionTypes;
    private TaxOnCommissionType taxOnCommissionType;
    private String taxOnCommissionTypeJson;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TaxOnCommissionTypeRepository taxOnCommissionTypeRepository;

    @Before
    public void setUp() throws Exception {
        taxOnCommissionTypeRepository.deleteAll();
        taxOnCommissionTypes = getTaxOnCommissionTypes();
        taxOnCommissionType = taxOnCommissionTypes.get(0);
        taxOnCommissionTypeJson = mapper.writeValueAsString(taxOnCommissionType);
    }

    @Test
    public void testSave() throws Exception {

        mockMvc.perform(post(BASE_URI).content(taxOnCommissionTypeJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<TaxOnCommissionType> taxOnCommissionTypes = taxOnCommissionTypeRepository.findAll();

        assertThat(taxOnCommissionTypes, hasSize(1));
        assertThat(taxOnCommissionTypes.get(0), equalTo(taxOnCommissionType));
    }

    @Test
    public void testReturnsSavedTaxOnCommissionType() throws Exception {

        String responseBody = mockMvc.perform(post(BASE_URI)
                .content(taxOnCommissionTypeJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getRequest().getContentAsString();

        assertThat(readTaxOnCommissionTypeFromJson(responseBody), equalTo(taxOnCommissionType));
    }

    private TaxOnCommissionType readTaxOnCommissionTypeFromJson(String json) throws Exception {

        return mapper.readValue(json, TaxOnCommissionType.class);
    }

    @Test
    public void testValidatesTaxOnCommissionType() throws Exception {

        taxOnCommissionType.setDescription(null);

        String taxOnCommissionTypeJson = mapper.writeValueAsString(taxOnCommissionType);

        mockMvc.perform(post(BASE_URI).content(taxOnCommissionTypeJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testValidatesTaxOnCommissionTypeCodeLength() throws Exception {

        taxOnCommissionType.getPk().setCode("1234567898912235445");

        String taxOnCommissionTypeJson = mapper.writeValueAsString(taxOnCommissionType);

        mockMvc.perform(post(BASE_URI).content(taxOnCommissionTypeJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", containsString("ode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("size must be between 1 and 6")));
    }

    @Test
    public void testGetTctps() throws Exception {

        createTaxOnCommissionTypes();

        String isoc = taxOnCommissionType.getPk().getIsoCountryCode();

        List<TaxOnCommissionType> tctps = taxOnCommissionTypes.stream()
                .filter(t -> t.getPk().getIsoCountryCode().equals(isoc))
                .collect(Collectors.toList());

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + isoc).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TaxOnCommissionType> actual =
                mapper.readValue(responseBody, new TypeReference<List<TaxOnCommissionType>>() {});

        assertThat(actual, equalTo(tctps));
    }

    private List<TaxOnCommissionType> createTaxOnCommissionTypes() {
        List<TaxOnCommissionType> savedTaxOnCommissionTypes =
                taxOnCommissionTypeRepository.saveAll(taxOnCommissionTypes);
        taxOnCommissionTypeRepository.flush();

        return savedTaxOnCommissionTypes;
    }

    @Test
    public void testDeletesTaxOnCommissionType() throws Exception {
        TaxOnCommissionType taxOnCommissionTypeToDelete = createTaxOnCommissionTypes().get(0);
        TaxOnCommissionTypePk pk = taxOnCommissionTypeToDelete.getPk();

        mockMvc.perform(delete(BASE_URI + "/" + pk.getIsoCountryCode() + "/" + pk.getCode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(taxOnCommissionTypeRepository.findById(pk).isPresent());
    }

    @Test
    public void testReturnsTaxOnCommissionTypeOnDeletion() throws Exception {
        TaxOnCommissionType taxOnCommissionTypeToDelete = createTaxOnCommissionTypes().get(0);
        TaxOnCommissionTypePk pk = taxOnCommissionTypeToDelete.getPk();

        String responseBody = mockMvc.perform(
                delete(BASE_URI + "/" + pk.getIsoCountryCode() + "/" + pk.getCode())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        assertThat(readTaxOnCommissionTypeFromJson(responseBody),
                equalTo(taxOnCommissionTypeToDelete));
    }

    @Test
    public void testReturnsNotFoundWhenTryingToDeleteNonExistentTaxOnCommissionType()
            throws Exception {
        mockMvc.perform(delete(NON_EXISTENT_REASON_URI)).andExpect(status().isNotFound());
    }

}
