package org.iata.bsplink.filemanager.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.filemanager.exception.BsplinkValidationException;
import org.iata.bsplink.filemanager.model.entity.FileAccessPermission;
import org.iata.bsplink.filemanager.model.entity.FileAccessType;
import org.iata.bsplink.filemanager.model.repository.FileAccessPermissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileAccessPermissionControllerTest {

    private static final String BASE_URL = "/v1/fileAccessPermissions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileAccessPermissionRepository repository;

    @Autowired
    protected WebApplicationContext webAppContext;

    @Autowired
    private ObjectMapper mapper;


    @Before
    public void setUp() throws IOException, BsplinkValidationException {

        repository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).build();
    }


    @Test
    public void testGetByUser() throws Exception {

        List<FileAccessPermission> faps = savedFaps();

        String user = faps.get(0).getUser();
        String json = mapper.writeValueAsString(faps.stream()
                .filter(fap -> user.equals(fap.getUser())));

        mockMvc.perform(get(BASE_URL + "?user=" + user))
                .andExpect(status().isOk())
                .andExpect(content().json(json, true));
    }


    @Test
    public void testGetAll() throws Exception {

        List<FileAccessPermission> faps = savedFaps();

        String json = mapper.writeValueAsString(faps);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(json, true));
    }


    @Test
    public void testGetOne() throws Exception {

        FileAccessPermission fap = savedFaps().get(0);

        String json = mapper.writeValueAsString(fap);

        mockMvc.perform(get(BASE_URL + "/" + fap.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(json, true));
    }


    @Test
    public void testGetOneNotFound() throws Exception {

        mockMvc.perform(get(BASE_URL + "/8"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDelete() throws Exception {

        FileAccessPermission fap = savedFaps().get(0);

        Long id = fap.getId();

        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isOk());

        assertFalse(repository.existsById(id));
    }


    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/8"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {

        FileAccessPermission fap = faps().get(0);

        String json = mapper.writeValueAsString(fap);

        mockMvc.perform(post(BASE_URL).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<FileAccessPermission> fapsFound = repository.findAll();
        assertThat(fapsFound, hasSize(1));

        fap.setId(fapsFound.get(0).getId());
        assertThat(fapsFound.get(0), equalTo(fap));
    }


    @Test
    public void testCreateReturnsValue() throws Exception {

        FileAccessPermission fap = faps().get(0);

        String json = mockMvc.perform(post(BASE_URL)
                .content(mapper.writeValueAsString(fap))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        FileAccessPermission fapReturned = mapper.readValue(json, fap.getClass());
        fap.setId(fapReturned.getId());
        assertThat(fapReturned, equalTo(fap));
    }


    @Test
    public void testConflict() throws Exception {

        FileAccessPermission fap = savedFaps().get(0);

        String json = mapper.writeValueAsString(fap);

        mockMvc.perform(post(BASE_URL).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }


    @Test
    public void testBadRequest() throws Exception {

        FileAccessPermission fap = faps().get(0);

        fap.setIsoCountryCode("NNN");

        String json = mapper.writeValueAsString(fap);

        mockMvc.perform(post(BASE_URL).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName",
                        equalTo("isoCountryCode")));
    }


    private List<FileAccessPermission> savedFaps() {

        List<FileAccessPermission> faps = faps();
        faps.forEach(repository::save);
        return faps;
    }


    private List<FileAccessPermission> faps() {

        FileAccessPermission fap;

        fap = new FileAccessPermission();
        fap.setAccess(FileAccessType.WRITE);
        fap.setFileType("xx");
        fap.setIsoCountryCode("IT");
        fap.setUser("USER1");

        List<FileAccessPermission> faps = new ArrayList<>();
        faps.add(fap);

        fap = new FileAccessPermission();
        fap.setAccess(FileAccessType.READ);
        fap.setFileType("xx");
        fap.setIsoCountryCode("IT");
        fap.setUser("USER1");
        faps.add(fap);

        fap = new FileAccessPermission();
        fap.setAccess(FileAccessType.WRITE);
        fap.setFileType("yy");
        fap.setIsoCountryCode("SG");
        fap.setUser("USER2");
        faps.add(fap);

        return faps;
    }
}
