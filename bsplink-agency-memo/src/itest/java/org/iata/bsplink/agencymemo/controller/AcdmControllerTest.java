package org.iata.bsplink.agencymemo.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.iata.bsplink.agencymemo.test.fixtures.AcdmFixtures.getAcdms;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.iata.bsplink.agencymemo.dto.Agent;
import org.iata.bsplink.agencymemo.dto.Airline;
import org.iata.bsplink.agencymemo.dto.CommentRequest;
import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.BsplinkFile;
import org.iata.bsplink.agencymemo.model.entity.Config;
import org.iata.bsplink.agencymemo.model.repository.AcdmRepository;
import org.iata.bsplink.agencymemo.model.repository.BsplinkFileRepository;
import org.iata.bsplink.agencymemo.service.AgentService;
import org.iata.bsplink.agencymemo.service.AirlineService;
import org.iata.bsplink.agencymemo.service.CommentService;
import org.iata.bsplink.agencymemo.service.ConfigService;
import org.iata.bsplink.agencymemo.validation.FreeStatValidator;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"app.host.local.protocol=local", "app.host.sftp.protocol=sftp"})
@AutoConfigureMockMvc
@Transactional
public class AcdmControllerTest {

    private static final String BASE_URI = "/v1/acdms";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AcdmRepository acdmRepository;

    @MockBean
    private ConfigService configService;

    @Autowired
    private BsplinkFileRepository bskplinkFileRepository;

    @MockBean
    private YadeUtils yadeUtils;

    @MockBean
    AgentService agentService;

    @MockBean
    private AirlineService airlineService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper mapper;

    private List<Acdm> acdms;

    private Acdm acdm;
    private String acdmJson;
    private String countryCodeNotFound = "NX";
    private Config config;

    @Before
    public void setUp() throws Exception {
        acdmRepository.deleteAll();
        acdms = getAcdms();

        acdm = acdms.get(0);
        acdmJson = mapper.writeValueAsString(acdm);

        Agent agent = new Agent();
        agent.setIataCode(acdm.getAgentCode());
        agent.setIsoCountryCode(acdm.getIsoCountryCode());

        Airline airline = new Airline();
        airline.setAirlineCode(acdm.getAirlineCode());
        airline.setIsoCountryCode(acdm.getIsoCountryCode());

        when(agentService.findAgent(agent.getIataCode())).thenReturn(agent);
        when(airlineService.findAirline(airline.getIsoCountryCode(), airline.getAirlineCode()))
                .thenReturn(airline);
        when(airlineService.findAirline(countryCodeNotFound, airline.getAirlineCode()))
                .thenReturn(null);

        config = new Config();
        config.setIsoCountryCode(acdm.getIsoCountryCode());
        config.setMaxNumberOfRelatedDocuments(-1);

        when(configService.find(acdm.getIsoCountryCode())).thenReturn(config);
        when(configService.find(countryCodeNotFound)).thenReturn(new Config());
    }

    @Test
    public void testCreatesAcdm() throws Exception {
        mockMvc.perform(post(BASE_URI).content(acdmJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<Acdm> findAll = acdmRepository.findAll();
        assertThat(findAll, hasSize(1));
        assertThat(findAll.get(0), equalTo(acdm));
    }

    @Test
    public void testCreatesAcdmBadRequest() throws Exception {
        acdm.setAirlineCode(null);
        String json = mapper.writeValueAsString(acdm);
        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
        List<Acdm> findAll = acdmRepository.findAll();
        assertTrue(findAll.isEmpty());
    }

    @Test
    public void testCreatesAcdmBadRequestForCountryCodeNotExists() throws Exception {
        acdm.setIsoCountryCode(countryCodeNotFound);
        String json = mapper.writeValueAsString(acdm);

        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));

        List<Acdm> findAll = acdmRepository.findAll();
        assertTrue(findAll.isEmpty());
    }

    @Test
    public void testCreatesAcdmBadRequestIncorrectStatisticalCode() throws Exception {
        config.setFreeStat(false);
        acdm.setStatisticalCode("IXT");
        String json = mapper.writeValueAsString(acdm);

        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("statisticalCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo(FreeStatValidator.INCORRECT_VALUE_MSG)));
        List<Acdm> findAll = acdmRepository.findAll();
        assertTrue(findAll.isEmpty());
    }

    @Test
    public void testCreatesAcdmCorrectStatisticalCode() throws Exception {
        config.setFreeStat(false);
        acdm.setStatisticalCode("D");
        String json = mapper.writeValueAsString(acdm);
        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreatesAcdmCorrectStatisticalCodeWithFreeStatEnabled() throws Exception {
        config.setFreeStat(true);
        acdm.setStatisticalCode("I 8");
        String json = mapper.writeValueAsString(acdm);
        mockMvc.perform(post(BASE_URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreatesAcdmReturnsEntity() throws Exception {

        String responseBody = mockMvc
                .perform(post(BASE_URI).content(acdmJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        Acdm savedAcdm = readAcdmFromJson(responseBody);

        assertThat(savedAcdm, equalTo(acdm));

    }

    @Test
    public void testCreatesAcdmMassload() throws Exception {
        mockMvc.perform(post(BASE_URI + "?fileName=file.txt").content(acdmJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        List<Acdm> findAll = acdmRepository.findAll();
        assertThat(findAll, hasSize(1));
        assertThat(findAll.get(0), equalTo(acdm));
    }

    @Test
    public void testCreatesAcdmMassloadBadRequest() throws Exception {
        acdm.setAirlineCode(null);
        String json = mapper.writeValueAsString(acdm);
        mockMvc.perform(post(BASE_URI + "?fileName=file.txt").content(json)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        List<Acdm> findAll = acdmRepository.findAll();
        assertTrue(findAll.isEmpty());
    }

    private Acdm readAcdmFromJson(String json) throws Exception {

        return mapper.readValue(json, Acdm.class);
    }

    @Test
    public void testReturnsNotFound() throws Exception {
        mockMvc.perform(get(BASE_URI + "/78787878787")).andExpect(status().isNotFound());
    }

    private void createAcdms() {
        acdmRepository.saveAll(acdms);
    }

    @Test
    public void testGetAcdm() throws Exception {
        createAcdms();
        Acdm expected = acdmRepository.findAll().get(0);
        String responseBody = mockMvc.perform(get(BASE_URI + "/" + expected.getId()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Acdm retrievedAcdm = readAcdmFromJson(responseBody);
        assertThat(retrievedAcdm, equalTo(expected));
    }

    @Test
    public void testGetAcdms() throws Exception {
        createAcdms();
        String responseBody = mockMvc.perform(get(BASE_URI)).andExpect(status().isOk())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<Acdm> createdAccount =
                mapper.readValue(responseBody, new TypeReference<List<Acdm>>() {});
        assertThat(createdAccount, equalTo(acdms));
    }


    @Test
    public void getAllFilesForAcdmOk() throws Exception {

        Acdm acdm = acdmRepository.save(acdms.get(0));

        List<BsplinkFile> listFilesSaved = saveFiles(acdm);

        mockMvc.perform(get(BASE_URI + "/" + acdm.getId() + "/files")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listFilesSaved)));
    }

    @Test
    public void getAllFilesForAcdmBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URI + "/93333354/files")).andExpect(status().isBadRequest());
    }


    @Test
    public void testSaveFilesOk() throws Exception {

        String[] fileNames = {"file_one.txt", "file_two.pdf", "file_three.txt"};

        String[] fileTextContents = {"File one", "File two", "File three"};

        MockMultipartFile[] multipartFiles = {
            new MockMultipartFile("file", fileNames[0], "text/plain",
                        fileTextContents[0].getBytes()),
            new MockMultipartFile("file", fileNames[1], "application/pdf",
                        fileTextContents[1].getBytes()),
            new MockMultipartFile("file", fileNames[2], "text/plain",
                        fileTextContents[2].getBytes())};


        Acdm acdm = acdmRepository.save(acdms.get(0));

        mockMvc.perform(multipart(BASE_URI + "/" + acdm.getId() + "/files").file(multipartFiles[0])
                .file(multipartFiles[1]).file(multipartFiles[2])).andExpect(status().isOk());
    }

    @Test
    public void testSaveFilesBadRequest() throws Exception {

        MockMultipartFile[] multipartFiles =
            {new MockMultipartFile("file", "fileOne.txt", "text/plain", "Text".getBytes())};

        mockMvc.perform(multipart(BASE_URI + "/9666/files").file(multipartFiles[0]))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testSaveFilesThrowsException() throws Exception {

        when(yadeUtils.transfer(any(), any(), any(), any())).thenThrow(new Exception());

        MockMultipartFile[] multipartFiles =
            {new MockMultipartFile("file", "fileOne.txt", "text/plain", "Text".getBytes())};

        Acdm acdm = acdmRepository.save(acdms.get(0));

        mockMvc.perform(multipart(BASE_URI + "/" + acdm.getId() + "/files").file(multipartFiles[0]))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testSaveComments() throws Exception {

        Acdm acdm = acdmRepository.save(acdms.get(0));
        String commentJson = mapper.writeValueAsString(getCommentsRequest().get(0));

        mockMvc.perform(post(BASE_URI + "/" + acdm.getId() + "/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testSaveCommentsNoText() throws Exception {

        Acdm acdm = acdmRepository.save(acdms.get(0));
        String commentJson = "{\"text\":\null\"}";

        mockMvc.perform(post(BASE_URI + "/" + acdm.getId() + "/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveCommentsNoAcdmId() throws Exception {

        String commentJson = "{\"text\":\"Text one\"}";

        mockMvc.perform(post(BASE_URI + "/0/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }


    @Test
    public void testSaveCommentsNoAcdmFound() throws Exception {

        String commentJson = "{\"text\":\"Text one\"}";

        mockMvc.perform(post(BASE_URI + "/98798798897/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveCommentsThorwsException() throws Exception {

        Acdm acdm = acdmRepository.save(acdms.get(0));
        String commentJson = mapper.writeValueAsString(getCommentsRequest().get(0));

        when(commentService.save(getCommentsRequest().get(0), acdm.getId()))
                .thenThrow(new IllegalAccessException());

        mockMvc.perform(post(BASE_URI + "/" + acdm.getId() + "/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetAllCommentsForAcdm() throws Exception {
        Acdm acdm = acdmRepository.save(acdms.get(0));
        mockMvc.perform(get(BASE_URI + "/" + acdm.getId() + "/comments"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllCommentsForAcdmBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URI + "/9878979878/comments")).andExpect(status().isBadRequest());
    }

    /**
     * Returns a list of comments to save.
     *
     * @return CommentRequest
     */
    public List<CommentRequest> getCommentsRequest() {

        CommentRequest requestOne = new CommentRequest();
        requestOne.setText("Text one");

        List<CommentRequest> listComments = new ArrayList<>();
        listComments.add(requestOne);

        CommentRequest requestTwo = new CommentRequest();
        requestTwo.setText("Text one");
        listComments.add(requestTwo);

        return listComments;
    }

    /**
     * Returns File List.
     */
    public List<BsplinkFile> getFileList() {

        BsplinkFile file1 = new BsplinkFile();
        file1.setAcdmId(acdm.getId());
        file1.setBytes(1000L);
        file1.setName("file_1.txt");
        file1.setPath("/path");
        file1.setUploadDateTime(Instant.now());

        BsplinkFile file2 = new BsplinkFile();
        file1.setAcdmId(acdm.getId());
        file2.setBytes(1000L);
        file2.setName("file_2.txt");
        file2.setPath("/path");
        file2.setUploadDateTime(Instant.now());

        List<BsplinkFile> filesList = new ArrayList<>();
        filesList.add(file1);
        filesList.add(file2);

        return filesList;
    }

    /**
     * Save Files.
     */
    public List<BsplinkFile> saveFiles(Acdm acdm) {

        List<BsplinkFile> fileList = new ArrayList<>();

        BsplinkFile fileOne = getFileList().get(0);
        fileOne.setAcdmId(acdm.getId());
        fileList.add(fileOne);

        BsplinkFile fileTwo = getFileList().get(1);
        fileTwo.setAcdmId(acdm.getId());
        fileList.add(fileTwo);

        return bskplinkFileRepository.saveAll(fileList);
    }

}
