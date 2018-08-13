package org.iata.bsplink.refund.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.iata.bsplink.refund.test.fixtures.RefundFixtures.getRefunds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.refund.dto.Agent;
import org.iata.bsplink.refund.dto.CommentRequest;
import org.iata.bsplink.refund.dto.RefundStatusRequest;
import org.iata.bsplink.refund.model.entity.BsplinkFile;
import org.iata.bsplink.refund.model.entity.Comment;
import org.iata.bsplink.refund.model.entity.FormOfPaymentAmount;
import org.iata.bsplink.refund.model.entity.FormOfPaymentType;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundAction;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.entity.RefundIssuePermission;
import org.iata.bsplink.refund.model.entity.RefundStatus;
import org.iata.bsplink.refund.model.repository.BsplinkFileRepository;
import org.iata.bsplink.refund.model.repository.CommentRepository;
import org.iata.bsplink.refund.model.repository.RefundRepository;
import org.iata.bsplink.refund.service.AgentService;
import org.iata.bsplink.refund.service.RefundHistoryService;
import org.iata.bsplink.refund.service.RefundIssuePermissionService;
import org.iata.bsplink.refund.service.RefundService;
import org.iata.bsplink.refund.validation.AgentValidator;
import org.iata.bsplink.refund.validation.AirlineValidator;
import org.iata.bsplink.refund.validation.CountryValidator;
import org.iata.bsplink.refund.validation.CurrencyValidator;
import org.iata.bsplink.refund.validation.IssuePermissionValidator;
import org.iata.bsplink.refund.validation.MassloadFileNameValidator;
import org.iata.bsplink.refund.validation.MassloadValidator;
import org.iata.bsplink.refund.validation.PartialRefundValidator;
import org.iata.bsplink.refund.validation.RefundCompositeValidator;
import org.iata.bsplink.refund.validation.RefundUpdateValidator;
import org.iata.bsplink.yadeutils.YadeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"app.host.local.protocol=local", "app.host.sftp.protocol=sftp"})
@AutoConfigureMockMvc
@Transactional
public class RefundControllerTest {

    private static final String BASE_URI = "/v1/refunds/indirects";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private RefundService refundService;

    @Autowired
    private RefundHistoryService refundHistoryService;

    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private YadeUtils yadeUtils;

    @MockBean
    private AgentValidator agentValidator;

    @MockBean
    private AirlineValidator airlineValidator;

    @MockBean
    private CurrencyValidator currencyValidator;

    @MockBean
    private CountryValidator countryValidator;

    @MockBean
    private RefundIssuePermissionService refundIssuePermissionService;

    @MockBean
    private PartialRefundValidator partialRefundValidator;

    @MockBean
    private AgentService agentService;

    private RefundIssuePermission refundIssuePermission;

    @SpyBean
    private RefundCompositeValidator refundValidator;

    @SpyBean
    private RefundUpdateValidator refundUpdateValidator;

    @SpyBean
    private MassloadValidator massloadValidator;

    private Refund refund;

    private String massloadFileName;

    @Before
    public void setUp() {
        refund = getRefunds().get(0);
        massloadFileName =
                refund.getIsoCountryCode() + "e9EARS_20001224_" + refund.getAirlineCode() + "9_123";
        refundIssuePermission = new RefundIssuePermission();
        when(refundIssuePermissionService.findByIsoCountryCodeAndAirlineCodeAndAgentCode(any(),
                any(), any())).thenReturn(Optional.of(refundIssuePermission));

        when(agentService.findAgent(any())).thenReturn(new Agent());
    }

    @Test
    public void testGetAllCommentsForRefund() throws Exception {
        Refund refundSaved = getSavedRefund();
        List<Comment> listCommentsSaved = getSavedCommentList(refundSaved);
        mockMvc.perform(get(BASE_URI + "/" + refundSaved.getId() + "/comments")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listCommentsSaved)));
    }

    @Test
    public void testGetAllCommentsForRefundNotFound() throws Exception {
        mockMvc.perform(
                get(BASE_URI + "/98789765465/comments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveComment() throws Exception {
        Refund refundSaved = getSavedRefund();
        String commentJson = mapper.writeValueAsString(getCommentsRequest().get(0));
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/comments")
                .content(commentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSaveCommentsNoText() throws Exception {
        Refund refundSaved = getSavedRefund();
        String commentJson = mapper.writeValueAsString(new CommentRequest());
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/comments")
                .content(commentJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveCommentsNoRefundId() throws Exception {
        String commentJson = "{\"text\":\"Text one\"}";
        mockMvc.perform(post(BASE_URI + "/" + null + "/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveCommentsNoRefundFound() throws Exception {
        String commentJson = "{\"text\":\"Text one\"}";
        mockMvc.perform(post(BASE_URI + "/98798798897/comments").content(commentJson)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRefund() throws Exception {
        Refund refundWithId = refundRepository.save(refund);
        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId())
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }


    @Test
    public void testUpdateRefundErrors() throws Exception {
        Refund refundWithId = refundRepository.save(refund);
        refundWithId.setId(987897987L);
        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId())
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdateRefundChangeAirlineCode() throws Exception {
        Refund refundCopied = saveAndCopyRefund();
        refundCopied.setAirlineCode("999");

        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("airlineCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("airlineCode can only be modified in status: DRAFT")));
    }

    @Test
    public void testUpdateRefundNotFound() throws Exception {
        Refund refundCopied = saveAndCopyRefund();
        refundCopied.setId(3212312313L);

        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].message", equalTo("Refund not found")));
    }

    @Test
    public void testUpdateRefundChangeAgentCode() throws Exception {
        Refund refundCopied = saveAndCopyRefund();
        refundCopied.setAgentCode("99999999");

        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("agentCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("agentCode cannot be modified")));
    }

    @Test
    public void testUpdateRefundStatusNull() throws Exception {
        Refund refundCopied = saveAndCopyRefund();
        refundCopied.setStatus(null);

        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateRefundUnderInvestigationStatus() throws Exception {
        Refund refundCopied = saveAndCopyRefundWithStatus(RefundStatus.UNDER_INVESTIGATION);
        refundCopied.setPassenger("newPassenger");

        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("passenger")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("passenger can only be modified in DRAFT, PENDING and "
                                + "PENDING_SUPERVISION status")));
    }

    @Test
    public void testUpdateRefundRejectedStatus() throws Exception {
        Refund refundCopied = saveAndCopyRefundWithStatus(RefundStatus.REJECTED);
        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("Refund with status not allowed to be modified: REJECTED")));
    }

    @Test
    public void testUpdateRefundModifyHistory() throws Exception {
        Refund refundCopied = saveAndCopyRefund();

        RefundHistory history = new RefundHistory();
        history.setInsertDateTime(Instant.now());
        history.setAction(RefundAction.ATTACH_FILE);
        history.setRefundId(refundCopied.getId());
        List<RefundHistory> historyList = new ArrayList<>();
        historyList.add(history);
        refundCopied.setHistory(historyList);

        mockMvc.perform(put(BASE_URI + "/" + refundCopied.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("history")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("history cannot be modified")));
    }

    @Test
    public void testUpdateRefundModifyFiles() throws Exception {

        Refund refundWithId = refundRepository.save(getRefunds().get(2));
        Refund refundCopied = new Refund();
        BeanUtils.copyProperties(refundCopied, refundWithId);

        BsplinkFile file = new BsplinkFile();
        file.setBytes(1024L);
        file.setName("file_1.txt");
        file.setPath("path");
        file.setRefundId(refundWithId.getId());
        file.setUploadDateTime(Instant.now());
        List<BsplinkFile> fileList = new ArrayList<>();
        fileList.add(file);
        refundCopied.setAttachedFiles(fileList);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("attachedFiles")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("attachedFiles cannot be modified")));

    }


    @Test
    public void testUpdateRefundModifyComments() throws Exception {

        Refund refundWithId = refundRepository.save(refund);
        Refund refundCopied = new Refund();
        BeanUtils.copyProperties(refundCopied, refundWithId);

        Comment comment = new Comment();
        comment.setInsertDateTime(Instant.now());
        comment.setRefundId(refundWithId.getId());
        comment.setText("Comment");
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        refundCopied.setComments(commentList);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId())
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("comments")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("comments cannot be modified")));
    }

    @Test
    public void testGetRefund() throws Exception {
        refundRepository.save(refund);
        String responseBody = mockMvc.perform(get(BASE_URI + "/" + refund.getId()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Refund retrievedRefund = mapper.readValue(responseBody, Refund.class);
        assertThat(retrievedRefund, equalTo(refund));
    }

    @Test
    public void testGetRefundNotFound() throws Exception {
        refundRepository.save(refund);
        long id = refund.getId();
        refundRepository.deleteById(id);
        mockMvc.perform(get(BASE_URI + "/" + id)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetRefunds() throws Exception {
        List<Refund> refunds = getRefunds();
        refundRepository.deleteAll();
        refundRepository.saveAll(refunds);
        String responseBody = mockMvc.perform(get(BASE_URI)).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        List<Refund> retrievedRefunds =
                mapper.readValue(responseBody, new TypeReference<List<Refund>>() {});
        assertThat(retrievedRefunds, equalTo(refunds));
    }

    @Test
    public void testSaveIndirectRefund() throws Exception {
        refundRepository.deleteAll();
        String refundJson = getRefundJson();
        mockMvc.perform(post(BASE_URI).content(refundJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<Refund> findAll = refundRepository.findAll();
        findAll.get(0).setHistory(null);
        assertThat(findAll, hasSize(1));
        assertEquals(refund, findAll.get(0));
    }

    private String getRefundJson() throws Exception {

        return mapper.writeValueAsString(refund);
    }

    @Test
    public void testSaveIndirectReturnsEntity() throws Exception {
        String refundJson = getRefundJson();
        String responseBody = mockMvc
                .perform(post(BASE_URI).content(refundJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Refund receivedRefund = mapper.readValue(responseBody, Refund.class);
        receivedRefund.setHistory(null);
        assertEquals(refund, receivedRefund);
    }

    @Test
    public void testSaveIndirectRefundHasErrors() throws Exception {
        refund.setAgentCode(null);
        String refundJson = getRefundJson();
        mockMvc.perform(post(BASE_URI).content(refundJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")));
    }

    @Test
    public void testSaveIndirectRefundWithoutPermission() throws Exception {

        when(refundIssuePermissionService.findByIsoCountryCodeAndAirlineCodeAndAgentCode(any(),
                any(), any())).thenReturn(Optional.empty());

        String refundJson = getRefundJson();
        mockMvc.perform(post(BASE_URI).content(refundJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("airlineCode")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo(IssuePermissionValidator.NO_PERMISSION)));
    }

    private Refund getSavedRefund() {
        return refundRepository.save(getRefunds().get(0));
    }

    private List<Comment> getSavedCommentList(Refund refund) {

        Comment commentOne = new Comment();
        commentOne.setRefundId(refund.getId());
        commentOne.setInsertDateTime(Instant.now());
        commentOne.setText("Comment one");
        Comment commentOneSaved = commentRepository.save(commentOne);

        List<Comment> commentListSaved = new ArrayList<>();
        commentListSaved.add(commentOneSaved);

        Comment commentTwo = new Comment();
        commentTwo.setRefundId(refund.getId());
        commentTwo.setInsertDateTime(Instant.now());
        commentTwo.setText("Comment two");
        Comment commentTwoSaved = commentRepository.save(commentTwo);
        commentListSaved.add(commentTwoSaved);

        return commentListSaved;
    }

    @Test
    public void testDeleteIndirectRefund() throws Exception {
        refundRepository.save(refund);
        long id = refund.getId();
        mockMvc.perform(delete(BASE_URI + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertFalse(refundRepository.findById(id).isPresent());
    }

    @Test
    public void testRespondsWithBadRequestIfStatusIsNotAllowedForDeletion() throws Exception {
        refund.setStatus(RefundStatus.AUTHORIZED);
        refundRepository.save(refund);
        long id = refund.getId();

        mockMvc.perform(delete(BASE_URI + "/" + id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertTrue(refundRepository.findById(id).isPresent());
    }

    @Test
    public void testDeleteIndirectRefundNotFound() throws Exception {
        refundRepository.deleteAll();
        mockMvc.perform(delete(BASE_URI + "/15376").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveFiles() throws Exception {

        String[] fileNames = {"file_one.txt", "file_two.pdf", "file_three.txt"};

        String[] fileTextContents = {"File one", "File two", "File three"};

        MockMultipartFile[] multipartFiles = {
            new MockMultipartFile("file", fileNames[0], "text/plain",
                        fileTextContents[0].getBytes()),
            new MockMultipartFile("file", fileNames[1], "application/pdf",
                        fileTextContents[1].getBytes()),
            new MockMultipartFile("file", fileNames[2], "text/plain",
                        fileTextContents[2].getBytes())};

        mockMvc.perform(multipart(BASE_URI + "/" + getSavedRefund().getId() + "/files")
                .file(multipartFiles[0]).file(multipartFiles[1]).file(multipartFiles[2]))
                .andExpect(status().isOk());

    }

    @Test
    public void testSaveFilesRefundNotFound() throws Exception {

        String[] fileNames = {"file_one.txt"};

        String[] fileTextContents = {"File one"};

        MockMultipartFile[] multipartFiles = {new MockMultipartFile("file", fileNames[0],
                "text/plain", fileTextContents[0].getBytes())};

        mockMvc.perform(multipart(BASE_URI + "/98798897978/files").file(multipartFiles[0]))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveFilesThrowsException() throws Exception {

        when(yadeUtils.transfer(any(), any(), any(), any())).thenThrow(new Exception());

        MockMultipartFile[] multipartFiles =
            {new MockMultipartFile("file", "fileOne.txt", "text/plain", "Text".getBytes())};

        mockMvc.perform(multipart(BASE_URI + "/" + getSavedRefund().getId() + "/files")
                .file(multipartFiles[0])).andExpect(status().isInternalServerError());
    }

    @Test
    public void getAllFilesForRefundOk() throws Exception {

        Refund refund = getSavedRefund();

        List<BsplinkFile> listFilesSaved = saveFiles(refund);

        mockMvc.perform(get(BASE_URI + "/" + refund.getId() + "/files")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listFilesSaved)));
    }

    @Test
    public void getAllFilesForRefundBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URI + "/93333354/files")).andExpect(status().isNotFound());
    }

    @Test
    public void testGetRefundHistory() throws Exception {
        Refund refundSaved = refundService.saveIndirectRefund(getRefunds().get(0));
        mockMvc.perform(get(BASE_URI + "/" + refundSaved.getId() + "/history"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRefundHistoryBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URI + "/987987987987564/history"))
                .andExpect(status().isNotFound());
    }

    /**
     * Save Files.
     */
    public List<BsplinkFile> saveFiles(Refund refund) {

        BsplinkFile fileOne = new BsplinkFile();
        fileOne.setRefundId(refund.getId());
        fileOne.setBytes(1024L);
        fileOne.setName("file_one.txt");
        fileOne.setPath("path");
        fileOne.setUploadDateTime(Instant.now());

        List<BsplinkFile> fileList = new ArrayList<>();
        fileList.add(fileOne);

        BsplinkFile fileTwo = new BsplinkFile();
        fileTwo.setRefundId(refund.getId());
        fileTwo.setBytes(1024L);
        fileTwo.setName("file_two.txt");
        fileTwo.setPath("path");
        fileTwo.setUploadDateTime(Instant.now());
        fileList.add(fileTwo);

        return bsplinkFileRepository.saveAll(fileList);
    }


    private Refund saveAndCopyRefund() throws IllegalAccessException, InvocationTargetException {
        Refund refundWithId = refundRepository.save(refund);
        Refund refundCopied = new Refund();
        BeanUtils.copyProperties(refundCopied, refundWithId);
        return refundCopied;
    }

    private Refund saveAndCopyRefundWithStatus(RefundStatus status)
            throws IllegalAccessException, InvocationTargetException {
        refund.setStatus(status);
        Refund refundWithId = refundRepository.save(refund);
        Refund refundCopied = new Refund();
        BeanUtils.copyProperties(refundCopied, refundWithId);
        return refundCopied;
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

    @Test
    public void testRefundValidatorIsInvokedOnRefundCreation() throws Exception {

        mockMvc.perform(
                post(BASE_URI).content(getRefundJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(refundValidator).validate(any(Refund.class), any(Errors.class));
    }

    @Test
    public void testAgentValidatorIsInvokedOnRefundCreation() throws Exception {
        when(agentValidator.supports(Refund.class)).thenReturn(true);
        mockMvc.perform(
                post(BASE_URI).content(getRefundJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(agentValidator).validate(any(Refund.class), any(Errors.class));
    }

    @Test
    public void testAirlineValidatorIsInvokedOnRefundCreation() throws Exception {
        when(airlineValidator.supports(Refund.class)).thenReturn(true);
        mockMvc.perform(
                post(BASE_URI).content(getRefundJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(airlineValidator).validate(any(Refund.class), any(Errors.class));
    }

    @Test
    public void testCountryValidatorIsInvokedOnRefundCreation() throws Exception {
        when(countryValidator.supports(Refund.class)).thenReturn(true);
        mockMvc.perform(
                post(BASE_URI).content(getRefundJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(countryValidator).validate(any(Refund.class), any(Errors.class));
    }

    @Test
    public void testCurrencyValidatorIsInvokedOnRefundCreation() throws Exception {
        when(currencyValidator.supports(Refund.class)).thenReturn(true);
        mockMvc.perform(
                post(BASE_URI).content(getRefundJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(currencyValidator).validate(any(Refund.class), any(Errors.class));
    }

    @Test
    public void testPartialRefundValidatorIsInvokedOnRefundCreation() throws Exception {
        when(partialRefundValidator.supports(Refund.class)).thenReturn(true);
        mockMvc.perform(
                post(BASE_URI).content(getRefundJson()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(partialRefundValidator).validate(any(Refund.class), any(Errors.class));
    }

    @Test
    public void testUpdateRefundStatusOk() throws Exception {
        RefundStatusRequest request = getRefundStatusRequest();
        Refund refundSaved = refundService.saveIndirectRefund(getRefunds().get(0));
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testUpdateRefundStatusNotAllowed() throws Exception {
        RefundStatusRequest request = getRefundStatusRequest();
        request.setStatus(RefundStatus.PENDING);
        Refund refundSaved = refundService.saveIndirectRefund(getRefunds().get(0));
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("Change of status not allowed")));;
    }

    @Test
    public void testUpdateRefundStatusToPending() throws Exception {
        Refund refundToSave = getRefunds().get(0);
        refundToSave.setStatus(RefundStatus.DRAFT);
        RefundStatusRequest request = getRefundStatusRequest();
        request.setStatus(RefundStatus.PENDING);
        Refund refundSaved = refundService.saveIndirectRefund(refundToSave);
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    public void testUpdateRefundStatusToPendingFieldsNotFilled() throws Exception {
        Refund refundToSave = getRefunds().get(0);
        refundToSave.setStatus(RefundStatus.DRAFT);
        refundToSave.setAirlineCode(null);
        refundToSave.setPassenger("");
        refundToSave.setAirlineCodeRelatedDocument(null);
        refundToSave.setIssueReason("");
        refundToSave.setDateOfIssueRelatedDocument(null);

        RefundStatusRequest request = getRefundStatusRequest();
        request.setStatus(RefundStatus.PENDING);

        Refund refundSaved = refundService.saveIndirectRefund(refundToSave);
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("For a pending refund the Airline Code is required.")))
                .andExpect(jsonPath("$.validationErrors[1].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[1].message",
                        equalTo("For a pending refund the Passenger is required.")))
                .andExpect(jsonPath("$.validationErrors[2].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[2].message",
                        equalTo("For a pending refund the Airline Code of the related document "
                                + "is required.")))
                .andExpect(jsonPath("$.validationErrors[3].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[3].message",
                        equalTo("For a pending refund the Issue Reason is required.")))
                .andExpect(jsonPath("$.validationErrors[4].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[4].message",
                        equalTo("For a pending refund the Issue Date of the related document "
                                + "is required.")));

    }

    @Test
    public void testUpdateRefundAmountToPassengerLessThanOne() throws Exception {
        Refund refundToSave = getRefunds().get(0);
        refundToSave.getAmounts().setRefundToPassenger(new BigDecimal(0));
        RefundStatusRequest request = getRefundStatusRequest();
        request.setStatus(RefundStatus.AUTHORIZED);
        Refund refundSaved = refundService.saveIndirectRefund(refundToSave);
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("status")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo("An authorized refund's total amount is expected to be "
                                + "greater than cero")));

    }

    @Test
    public void testUpdateRefundRejectionReasonFilled() throws Exception {
        Refund refundToSave = getRefunds().get(0);
        RefundStatusRequest request = getRefundStatusRequest();
        request.setStatus(RefundStatus.REJECTED);
        request.setRejectionReason("Rejection reason");
        Refund refundSaved = refundService.saveIndirectRefund(refundToSave);
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    public void testUpdateRefundRejectionReasonNotFilled() throws Exception {
        Refund refundToSave = getRefunds().get(0);
        RefundStatusRequest request = getRefundStatusRequest();
        request.setStatus(RefundStatus.REJECTED);
        request.setRejectionReason("");
        Refund refundSaved = refundService.saveIndirectRefund(refundToSave);
        mockMvc.perform(post(BASE_URI + "/" + refundSaved.getId() + "/status")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("rejectionReason")))
                .andExpect(jsonPath("$.validationErrors[0].message", equalTo("Field required.")));

    }

    @Test
    public void testUpdateRefundStatusNotFound() throws Exception {
        RefundStatusRequest request = getRefundStatusRequest();
        mockMvc.perform(
                post(BASE_URI + "/789798987/status").content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRefundStatusErrors() throws Exception {
        RefundStatusRequest request = getRefundStatusRequest();
        request.setRejectionReason(getTextWithMoreThanFiveHundredCharacters());
        mockMvc.perform(post(BASE_URI + "/1/status").content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    private RefundStatusRequest getRefundStatusRequest() {
        RefundStatusRequest request = new RefundStatusRequest();
        request.setAirlineRemark("airlineRemark");
        request.setRejectionReason("rejectionReason");
        request.setStatus(RefundStatus.UNDER_INVESTIGATION);
        return request;
    }

    private String getTextWithMoreThanFiveHundredCharacters() {
        String text = "Text with more than five hundred characters "
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters"
                + "Text with more than five hundred characters";
        return text;
    }

    @Test
    public void testGetRefundByIsoCountryCodeAirlineCodeTicketDocumentNumber() throws Exception {
        refundRepository.save(refund);
        String params = String.format("isoCountryCode=%s&airlineCode=%s&ticketDocumentNumber=%s",
                refund.getIsoCountryCode(), refund.getAirlineCode(),
                refund.getTicketDocumentNumber());
        String responseBody = mockMvc.perform(get(BASE_URI + "?" + params))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Refund retrievedRefund = mapper.readValue(responseBody, Refund.class);
        assertThat(retrievedRefund, equalTo(refund));
    }

    @Test
    public void testGetRefundByIsoCountryCodeAirlineCodeTicketDocumentNumberNotFound()
            throws Exception {
        refundRepository.save(refund);
        String params = String.format("isoCountryCode=%s&airlineCode=%s&ticketDocumentNumber=%s",
                refund.getIsoCountryCode(), refund.getAirlineCode(), "1234567890");
        mockMvc.perform(get(BASE_URI + "?" + params)).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRefundWithFileNameCorrectHistoryEntry() throws Exception {
        Refund refundWithId = refundRepository.save(refund);
        refundWithId.setStatus(RefundStatus.UNDER_INVESTIGATION);
        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + massloadFileName)
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertTrue(refundHistoryService.findByRefundId(refundWithId.getId()).stream()
                .anyMatch(h -> massloadFileName.equals(h.getFileName())));
    }

    @Test
    public void testUpdateRefundWithIncorrectFileName() throws Exception {
        String fileName = "xxe7xxxxxx";
        Refund refundWithId = refundRepository.save(refund);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + fileName)
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo(MassloadValidator.INCORRECT_FILENAME)));
    }

    @Test
    public void testUpdateRefundWithIncorrectAirlineInFileName() throws Exception {
        String fileName = massloadFileName.replaceFirst("^(.{18})(...)(.*)$", "$1ABC$3");
        Refund refundWithId = refundRepository.save(refund);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + fileName)
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo(MassloadValidator.INCORRECT_AIRLINE)));
    }

    @Test
    public void testUpdateRefundWithIncorrectCountryInFileName() throws Exception {
        String fileName = "ZZ" + massloadFileName.substring(2);
        Refund refundWithId = refundRepository.save(refund);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + fileName)
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo(MassloadValidator.INCORRECT_COUNTRY)));
    }

    @Test
    public void testUpdateRefundWithFileNameNotFound() throws Exception {
        Refund refundWithId = refundRepository.save(refund);
        Refund refundCopied = saveAndCopyRefund();
        refundCopied.setStatus(RefundStatus.DRAFT);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + massloadFileName)
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateRefundWithFileNameBadRequestIncorrectAirline() throws Exception {
        refund.setAirlineCode("123");
        Refund refundWithId = refundRepository.save(refund);
        Refund refundCopied = saveAndCopyRefund();
        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId()
                + "?fileName=" + massloadFileName)
                .content(mapper.writeValueAsString(refundCopied))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", nullValue()))
                .andExpect(jsonPath("$.validationErrors[0].message",
                        equalTo(MassloadFileNameValidator.INCORRECT_AIRLINE)));
    }

    @Test
    public void testSaveIndirectRefundWithAuthorizedStatus() throws Exception {
        refund.setStatus(RefundStatus.AUTHORIZED);
        String refundJson = getRefundJson();
        mockMvc.perform(post(BASE_URI).content(refundJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("status")));
    }


    @Test
    public void testUpdateRefundWithFileName() throws Exception {
        Refund refundWithId = refundRepository.save(refund);
        Refund refundUpdate = new Refund();

        BeanUtils.copyProperties(refundUpdate, refundWithId);

        refundUpdate.setId(99999992314L);
        refundUpdate.setStatus(RefundStatus.AUTHORIZED);

        refundUpdate.setAirlineRemark("AIRLINE REMARK");

        refundUpdate.getAmounts().setGrossFare(BigDecimal.TEN);
        refundUpdate.getAmounts().setRefundToPassenger(BigDecimal.TEN);

        FormOfPaymentAmount fopa = new FormOfPaymentAmount() {
            {
                setType(FormOfPaymentType.CA);
                setAmount(BigDecimal.TEN);
            }
        };

        refundUpdate.getFormOfPaymentAmounts().add(fopa);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + massloadFileName)
                .content(mapper.writeValueAsString(refundUpdate))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        Optional<Refund> savedRefund = refundRepository.findById(refundWithId.getId());

        assertTrue(savedRefund.isPresent());
        assertEquals(RefundStatus.AUTHORIZED, savedRefund.get().getStatus());
        assertNotNull(savedRefund.get().getDateOfAirlineAction());
        assertNotNull(savedRefund.get().getBillingPeriod());
        assertThat(savedRefund.get().getBillingPeriod(), greaterThan(0));

        Refund refundExpected = new Refund();
        BeanUtils.copyProperties(refundExpected, refundUpdate);

        refundExpected.setDateOfAirlineAction(savedRefund.get().getDateOfAirlineAction());
        refundExpected.setHistory(savedRefund.get().getHistory());
        refundExpected.setBillingPeriod(savedRefund.get().getBillingPeriod());

        assertThat(savedRefund.get(), equalTo(refundExpected));

        assertTrue(refundHistoryService.findByRefundId(refundWithId.getId()).stream()
                .anyMatch(h -> massloadFileName.equals(h.getFileName())));
    }


    @Test
    public void testChangeRefundStatusViaMassloadFileName() throws Exception {
        refundRepository.save(refund);
        RefundStatusRequest request = getRefundStatusRequest();
        mockMvc.perform(post(BASE_URI + "/" + refund.getId() + "/status?fileName="
                + massloadFileName).content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        assertThat(refund.getStatus(), equalTo(request.getStatus()));
        assertTrue(refundHistoryService.findByRefundId(refund.getId()).stream()
                .anyMatch(h -> massloadFileName.equals(h.getFileName())));
    }

    @Test
    public void testChangeRefundStatusViaMassloadFileNameNotFound() throws Exception {
        RefundStatusRequest request = getRefundStatusRequest();
        mockMvc.perform(post(BASE_URI + "/777888/status?fileName="
                + massloadFileName).content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void testChangeRefundStatusViaMassloadFileNameNotFoundBecauseOfStatus()
            throws Exception {
        refund.setStatus(RefundStatus.DRAFT);
        refundRepository.save(refund);
        RefundStatusRequest request = getRefundStatusRequest();
        mockMvc.perform(post(BASE_URI + "/" + refund.getId() + "/status?fileName="
                + massloadFileName).content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }


    @Test
    public void testChangeRefundStatusViaMassloadFileNameIncorrectAirline() throws Exception {
        refund.setAirlineCode("123");
        refundRepository.save(refund);
        RefundStatusRequest request = getRefundStatusRequest();
        mockMvc.perform(post(BASE_URI + "/" + refund.getId() + "/status?fileName="
                + massloadFileName).content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        assertThat(refund.getStatus(), not(equalTo(request.getStatus())));
    }

    @Test
    public void testValidatesConjunctionsOnRefundCreation() throws Exception {
        refund.setConjunctions(null);
        String refundJson = getRefundJson();
        mockMvc.perform(post(BASE_URI).content(refundJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andExpect(jsonPath("$.validationErrors[0].fieldName", equalTo("conjunctions")))
                .andExpect(jsonPath("$.validationErrors[0].message", equalTo("Field required.")));
    }

    @Test
    public void testOnUpdateRefundUpdateValidatorIsNotCalledIfThereArePreviousErrors()
            throws Exception {

        Refund refundWithId = refundRepository.save(refund);
        refund.setConjunctions(null);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId())
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        verify(refundUpdateValidator, never()).validate(any(), any(), any());
    }

    @Test
    public void testOnUpdateRefundViaMassloadValidatorIsNotCalledIfThereArePreviousErrors()
            throws Exception {

        Refund refundWithId = refundRepository.save(refund);
        refund.setConjunctions(null);

        mockMvc.perform(put(BASE_URI + "/" + refundWithId.getId() + "?fileName=" + massloadFileName)
                .content(mapper.writeValueAsString(refundWithId))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

        verify(massloadValidator, never()).validate(any(), any(), any(), any());
    }

    @Test
    public void testWhenCreatesIndirectRefundReturnsTicketDocumentNumber() throws Exception {

        refundRepository.deleteAll();

        String responseBody = mockMvc.perform(post(BASE_URI).content(getRefundJson())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Refund retrievedRefund = mapper.readValue(responseBody, Refund.class);

        String ticketDocumentNumber = refundRepository.findAll().get(0).getTicketDocumentNumber();

        assertThat(retrievedRefund.getTicketDocumentNumber(), not(isEmptyOrNullString()));
        assertThat(retrievedRefund.getTicketDocumentNumber(), equalTo(ticketDocumentNumber));
    }

}
