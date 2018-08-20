package org.iata.bsplink.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.iata.bsplink.user.Application;
import org.iata.bsplink.user.model.entity.BsplinkOption;
import org.iata.bsplink.user.model.entity.BsplinkTemplate;
import org.iata.bsplink.user.model.entity.UserType;
import org.iata.bsplink.user.model.repository.BsplinkOptionRepository;
import org.iata.bsplink.user.model.repository.BsplinkTemplateRepository;
import org.iata.bsplink.utils.BaseUserTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class BsplinkTemplateControllerTest extends BaseUserTest {

    private static final String BASE_URI = "/v1/bsplinkTemplates";

    @Autowired
    private BsplinkTemplateRepository repository;

    @Autowired
    private BsplinkOptionRepository optionRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private List<BsplinkTemplate> templates;
    private List<BsplinkOption> options;


    @Before
    public void setUp() {

        repository.deleteAll();

        BsplinkTemplate template1 = new BsplinkTemplate();
        template1.setId("TEMPL1");
        template1.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        repository.save(template1);

        BsplinkTemplate template2 = new BsplinkTemplate();
        template2.setId("TEMPL2");
        template2.setUserTypes(Arrays.asList(UserType.AIRLINE, UserType.BSP));
        repository.save(template2);

        BsplinkTemplate template3 = new BsplinkTemplate();
        template3.setId("TEMPL3");
        template3.setUserTypes(Arrays.asList(UserType.AGENT));
        repository.save(template3);
        templates = Arrays.asList(template1, template2, template3);

        optionRepository.deleteAll();

        BsplinkOption option1 = new BsplinkOption();
        option1.setId("OPT1");
        option1.setUserTypes(Arrays.asList(UserType.AGENT, UserType.AIRLINE, UserType.BSP));
        optionRepository.save(option1);

        BsplinkOption option2 = new BsplinkOption();
        option2.setId("OPT2");
        option2.setUserTypes(Arrays.asList(UserType.AIRLINE, UserType.BSP));
        optionRepository.save(option2);

        BsplinkOption option3 = new BsplinkOption();
        option3.setId("OPT3");
        option3.setUserTypes(Arrays.asList(UserType.AGENT));
        optionRepository.save(option3);

        options = Arrays.asList(option1, option2, option3);
    }


    @Test
    public void testGetTemplates() throws Exception {

        String responseBody = mockMvc.perform(
                get(BASE_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BsplinkTemplate> templatesRead = mapper.readValue(responseBody,
                new TypeReference<List<BsplinkTemplate>>() {});

        assertThat(templatesRead, equalTo(templates));
    }


    @Test
    public void testGetTemplatesFullView() throws Exception {

        String json = mapper.writeValueAsString(repository.findAll());

        String responseBody = mockMvc.perform(
                get(BASE_URI + "?fullView").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BsplinkTemplate> templatesRead = mapper.readValue(responseBody,
                new TypeReference<List<BsplinkTemplate>>() {});

        assertThat(templatesRead, equalTo(templates));
        assertThat(responseBody, equalTo(json));
    }


    @Test
    public void testGetTemplatesByUserType() throws Exception {

        List<BsplinkTemplate> templatesAirline = templates.stream()
                .filter(t -> t.getUserTypes().contains(UserType.AIRLINE))
                .collect(Collectors.toList());

        String responseBody = mockMvc.perform(
                get(BASE_URI + "?userType=AIRLINE").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BsplinkTemplate> templatesRead = mapper.readValue(responseBody,
                new TypeReference<List<BsplinkTemplate>>() {});

        assertThat(templatesRead, equalTo(templatesAirline));
    }


    @Test
    public void testGetTemplate() throws Exception {

        BsplinkTemplate template = templates.get(0);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + template.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BsplinkTemplate templateRead = mapper.readValue(responseBody, BsplinkTemplate.class);

        assertThat(templateRead, equalTo(template));
    }


    @Test
    public void testGetTemplateFullView() throws Exception {

        BsplinkTemplate template = templates.get(0);

        String json = mapper.writeValueAsString(template);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + template.getId() + "?fullView")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BsplinkTemplate templateRead = mapper.readValue(responseBody, BsplinkTemplate.class);

        assertThat(templateRead, equalTo(template));
        assertThat(responseBody, equalTo(json));
    }


    @Test
    public void testRemoveTemplate() throws Exception {

        String id = templates.get(0).getId();

        mockMvc.perform(
                delete(BASE_URI + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(repository.findById(id).isPresent());
    }


    @Test
    public void testCreateTemplate() throws Exception {

        List<BsplinkOption> options = optionRepository.findByUserTypes(UserType.AIRLINE);

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("NewTemplate");
        template.setUserTypes(Arrays.asList(UserType.AIRLINE));
        template.setOptions(options);

        String json = mapper.writeValueAsString(template);

        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        Optional<BsplinkTemplate> templateCreated = repository.findById(template.getId());
        assertTrue(templateCreated.isPresent());
        assertEquals(template, templateCreated.get());
    }


    @Test
    public void testCreateTemplateConflict() throws Exception {

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId(templates.get(1).getId());
        template.setUserTypes(Arrays.asList(UserType.AIRLINE));

        String json = mapper.writeValueAsString(template);

        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }


    @Test
    public void testUpdateTemplate() throws Exception {

        BsplinkOption option = optionRepository.findByUserTypes(UserType.AGENT).get(0);

        String id = templates.get(0).getId();

        BsplinkTemplate template = new BsplinkTemplate();
        template.setUserTypes(Arrays.asList(UserType.AGENT));
        template.setOptions(Arrays.asList(option));

        String json = mapper.writeValueAsString(template);

        mockMvc.perform(
                put(BASE_URI + "/" + id).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<BsplinkTemplate> templateUpdated = repository.findById(id);
        assertTrue(templateUpdated.isPresent());
        assertEquals(template.getOptions(), templateUpdated.get().getOptions());
        assertEquals(template.getUserTypes(), templateUpdated.get().getUserTypes());
    }


    @Test
    public void testAddOption() throws Exception {

        BsplinkOption option = new BsplinkOption();
        option.setId("OptionX");
        option.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(option);

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(UserType.DPC));
        repository.save(template);

        String json = mapper.writeValueAsString(option);

        mockMvc.perform(
                post(BASE_URI + "/" + template.getId() + "/options/").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<BsplinkTemplate> templateUpdated = repository.findById(template.getId());
        assertTrue(templateUpdated.isPresent());
        assertThat(templateUpdated.get().getOptions(), hasSize(1));
        assertEquals(option, templateUpdated.get().getOptions().get(0));
    }


    @Test
    public void testRemoveOption() throws Exception {

        BsplinkOption optionX = new BsplinkOption();
        optionX.setId("OptionX");
        optionX.setUserTypes(Arrays.asList(UserType.DPC, UserType.BSP));
        optionRepository.save(optionX);

        BsplinkOption optionY = new BsplinkOption();
        optionY.setId("OptionY");
        optionY.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(optionY);

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(UserType.DPC));
        template.setOptions(Arrays.asList(optionX, optionY));
        repository.save(template);

        mockMvc.perform(
                delete(BASE_URI + "/" + template.getId() + "/options/" + optionX.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<BsplinkTemplate> templateUpdated = repository.findById(template.getId());
        assertTrue(templateUpdated.isPresent());
        assertThat(templateUpdated.get().getOptions(), hasSize(1));
        assertEquals(optionY, templateUpdated.get().getOptions().get(0));
    }


    @Test
    public void testGetTemplateNotFound() throws Exception {

        mockMvc.perform(
                get(BASE_URI + "/TEMPLATE_NOT_FOUND").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRemoveTemplateNotFound() throws Exception {

        mockMvc.perform(
                delete(BASE_URI + "/TEMPLATE_NOT_FOUND").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testUpdateTemplateNotFound() throws Exception {

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TEMPLATE_NOT_FOUND");
        template.setUserTypes(Arrays.asList(UserType.AGENT));

        String json = mapper.writeValueAsString(template);

        mockMvc.perform(
                put(BASE_URI + "/" + template.getId()).content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddOptionTemplateNotFound() throws Exception {

        BsplinkOption option = new BsplinkOption();
        option.setId("OptionX");
        option.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(option);

        String json = mapper.writeValueAsString(option);

        mockMvc.perform(
                post(BASE_URI + "/TEMPLATE_NOT_FOUND/options").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddOptionNotFound() throws Exception {

        BsplinkOption option = new BsplinkOption();
        option.setId("OPTION_NOT_FOUND");
        option.setUserTypes(Arrays.asList(UserType.DPC));

        String json = mapper.writeValueAsString(option);

        mockMvc.perform(
                post(BASE_URI + "/" + templates.get(0).getId() + "/options").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddOptionConflict() throws Exception {

        BsplinkOption option = new BsplinkOption();
        option.setId("OptionX");
        option.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(option);

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(UserType.DPC));
        template.setOptions(Arrays.asList(option));
        repository.save(template);

        String json = mapper.writeValueAsString(option);

        mockMvc.perform(
                post(BASE_URI + "/" + template.getId() + "/options").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }


    @Test
    public void testRemoveOptionTemplateNotFound() throws Exception {

        mockMvc.perform(
                delete(BASE_URI + "/TEMPLATE_NOT_FOUND/options/OPT1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRemoveOptionNotFound() throws Exception {

        mockMvc.perform(
                delete(BASE_URI + "/" + templates.get(0).getId() + "/options/OPTION_NOT_FOUND")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRemoveOptionNotFoundInTemplate() throws Exception {

        BsplinkOption option = new BsplinkOption();
        option.setId("OptionX");
        option.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(option);

        mockMvc.perform(
                delete(BASE_URI + "/" + templates.get(0).getId() + "/options/" + option.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetOptionsTemplateNotFound() throws Exception {

        mockMvc.perform(
                get(BASE_URI + "/TEMPLATE_NOT_FOUND/options")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetOptions() throws Exception {

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(UserType.DPC));

        BsplinkOption optionA = new BsplinkOption();
        optionA.setId("OptionA");
        optionA.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(optionA);

        BsplinkOption optionB = new BsplinkOption();
        optionB.setId("OptionB");
        optionB.setUserTypes(Arrays.asList(UserType.DPC));
        optionRepository.save(optionB);

        template.setOptions(Arrays.asList(optionA, optionB));
        repository.save(template);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + template.getId() + "/options?fullView")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BsplinkOption> optionsRead = mapper.readValue(responseBody,
                new TypeReference<List<BsplinkOption>>() {});

        assertThat(optionsRead, equalTo(template.getOptions()));
    }


    @Test
    public void testGetOption() throws Exception {

        BsplinkTemplate template = templates.get(0);
        BsplinkOption option = options.get(0);
        template.getOptions().add(option);
        repository.save(template);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + template.getId() + "/options/" + option.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BsplinkOption optionRead = mapper.readValue(responseBody, BsplinkOption.class);

        assertThat(optionRead.getId(), equalTo(option.getId()));
    }


    @Test
    public void testGetOptionTemplateNotFound() throws Exception {

        mockMvc.perform(
                get(BASE_URI + "/TEMPLATE_NOT_FOUND/options/" + options.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetOptionNotFound() throws Exception {

        mockMvc.perform(
                get(BASE_URI + "/" + templates.get(0).getId() + "/options/OPTION_NOT_FOUND")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetOptionNotFoundInTemplate() throws Exception {

        mockMvc.perform(
                get(BASE_URI + "/" + templates.get(0).getId()
                        + "/options/" + options.get(0).getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetOptionFullView() throws Exception {

        BsplinkTemplate template = templates.get(0);
        BsplinkOption option = options.get(0);
        template.getOptions().add(option);
        repository.save(template);

        String json = mapper.writeValueAsString(option);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + template.getId() + "/options/" + option.getId() + "?fullView")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BsplinkOption optionRead = mapper.readValue(responseBody, BsplinkOption.class);

        assertThat(optionRead, equalTo(option));
        assertThat(responseBody, equalTo(json));
    }


    @Test
    public void testAddUserTypeTemplateNotFound() throws Exception {

        UserType userType = UserType.BSP;

        String json = mapper.writeValueAsString(userType);

        mockMvc.perform(
                post(BASE_URI + "/TEMPLATE_NOT_FOUND/userTypes").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAddUserType() throws Exception {

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(UserType.DPC));
        repository.save(template);

        String json = mapper.writeValueAsString(UserType.BSP);

        mockMvc.perform(
                post(BASE_URI + "/" + template.getId() + "/userTypes").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Optional<BsplinkTemplate> templateUpdated = repository.findById(template.getId());
        assertTrue(templateUpdated.isPresent());
        assertEquals(Arrays.asList(UserType.DPC, UserType.BSP),
                templateUpdated.get().getUserTypes());
    }


    @Test
    public void testAddUserTypeConflict() throws Exception {

        UserType userType = UserType.BSP;

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(userType));
        repository.save(template);

        String json = mapper.writeValueAsString(userType);

        mockMvc.perform(
                post(BASE_URI + "/" + template.getId() + "/userTypes").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }


    @Test
    public void testRemoveUserType() throws Exception {

        BsplinkTemplate template = templates.get(0);
        template.setUserTypes(Arrays.asList(UserType.BSP, UserType.GDS, UserType.IATA));
        repository.save(template);

        mockMvc.perform(
                delete(BASE_URI + "/" + template.getId() + "/userTypes/" + UserType.GDS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<BsplinkTemplate> templateUpdated = repository.findById(template.getId());

        assertTrue(templateUpdated.isPresent());
        assertEquals(Arrays.asList(UserType.BSP, UserType.IATA),
                templateUpdated.get().getUserTypes());
    }


    @Test
    public void testRemoveUserTypeTemplateNotFound() throws Exception {

        mockMvc.perform(
                delete(BASE_URI + "/TEMPLATE_NOT_FOUND/userTypes/DPC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRemoveUserTypeNotFoundInTemplate() throws Exception {

        UserType userType = UserType.IATA;

        mockMvc.perform(
                delete(BASE_URI + "/" + templates.get(0).getId() + "/userTypes/" + userType)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetUserTypesTemplateNotFound() throws Exception {

        mockMvc.perform(
                get(BASE_URI + "/TEMPLATE_NOT_FOUND/userTypes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetUserTypes() throws Exception {

        BsplinkTemplate template = new BsplinkTemplate();
        template.setId("TemplateX");
        template.setUserTypes(Arrays.asList(UserType.DPC, UserType.BSP));
        repository.save(template);

        String responseBody = mockMvc.perform(
                get(BASE_URI + "/" + template.getId() + "/userTypes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<UserType> userTypesRead = mapper.readValue(responseBody,
                new TypeReference<List<UserType>>() {});

        assertThat(userTypesRead, equalTo(template.getUserTypes()));
    }
}
