package org.iata.bsplink.refund.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.refund.model.entity.BsplinkFile;
import org.iata.bsplink.refund.model.entity.Refund;
import org.iata.bsplink.refund.model.entity.RefundAction;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.repository.BsplinkFileRepository;
import org.iata.bsplink.refund.response.SimpleResponse;
import org.iata.bsplink.refund.utils.FilesUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BsplinkFileService {

    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    @Autowired
    private RefundHistoryService refundHistoryService;

    @Autowired
    private FilesUtility filesUtility;

    public BsplinkFileService(BsplinkFileRepository bsplinkFileRepository) {
        this.bsplinkFileRepository = bsplinkFileRepository;
    }

    public Optional<BsplinkFile> findById(Long id) {
        return bsplinkFileRepository.findById(id);
    }

    public List<BsplinkFile> findByRefundId(Long id) {
        return bsplinkFileRepository.findByRefundId(id);
    }

    public BsplinkFile save(BsplinkFile file) {
        return bsplinkFileRepository.save(file);
    }

    /**
     * Save Files.
     */
    public List<SimpleResponse> saveFiles(Refund refund, List<MultipartFile> files)
            throws Exception {

        List<SimpleResponse> simpleResponses = new ArrayList<>(files.size());

        for (MultipartFile file : files) {

            filesUtility.saveMultipartFileFromLocalToRemote(refund, file);

            BsplinkFile bsplinkFile = new BsplinkFile();
            bsplinkFile.setName(file.getOriginalFilename());
            bsplinkFile.setPath(
                    filesUtility.getRemoteFullPathForFile(refund, file.getOriginalFilename()));
            bsplinkFile.setBytes(file.getSize());
            bsplinkFile.setUploadDateTime(Instant.now());
            bsplinkFile.setRefundId(refund.getId());

            RefundHistory refundHistory = new RefundHistory();
            refundHistory.setAction(RefundAction.ATTACH_FILE);
            refundHistory.setInsertDateTime(Instant.now());

            refundHistory.setRefundId(refund.getId());
            refundHistory.setFileName(file.getOriginalFilename());

            BsplinkFile fileSaved = save(bsplinkFile);

            SimpleResponse simpleResponse;
            simpleResponse = new SimpleResponse(null, HttpStatus.OK);
            simpleResponse.setSubject(file.getOriginalFilename());
            simpleResponse.setId(fileSaved.getId());
            simpleResponse.setPath(fileSaved.getPath());

            refundHistoryService.save(refundHistory);

            simpleResponses.add(simpleResponse);
        }

        return simpleResponses;
    }

}

