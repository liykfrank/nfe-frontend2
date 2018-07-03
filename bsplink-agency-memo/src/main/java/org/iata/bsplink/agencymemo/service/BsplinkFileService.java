package org.iata.bsplink.agencymemo.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iata.bsplink.agencymemo.model.entity.Acdm;
import org.iata.bsplink.agencymemo.model.entity.BsplinkFile;
import org.iata.bsplink.agencymemo.model.repository.BsplinkFileRepository;
import org.iata.bsplink.agencymemo.response.SimpleResponse;
import org.iata.bsplink.agencymemo.utils.FilesUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BsplinkFileService {

    @Autowired
    private BsplinkFileRepository bsplinkFileRepository;

    @Autowired
    private FilesUtility filesUtility;
    
    public BsplinkFileService(BsplinkFileRepository bsplinkFileRepository) {
        this.bsplinkFileRepository = bsplinkFileRepository;
    }
    

    public Optional<BsplinkFile> findById(Long id) {
        return bsplinkFileRepository.findById(id);
    }

    public List<BsplinkFile> findByAcdmId(Long id) {
        return bsplinkFileRepository.findByAcdmId(id);
    }

    public BsplinkFile save(BsplinkFile file) {
        return bsplinkFileRepository.save(file);
    }

    /**
     * Save Files.
     */
    public List<SimpleResponse> saveFiles(Acdm acdm, List<MultipartFile> files) throws Exception {

        List<SimpleResponse> simpleResponses = new ArrayList<>(files.size());

        for (MultipartFile file : files) {

            filesUtility.saveMultipartFileFromLocalToRemote(acdm, file);

            BsplinkFile bsplinkFile = new BsplinkFile();
            bsplinkFile.setName(file.getOriginalFilename());
            bsplinkFile.setPath(
                    filesUtility.getRemoteFullPathForFile(acdm, file.getOriginalFilename()));
            bsplinkFile.setBytes(file.getSize());
            bsplinkFile.setUploadDateTime(Instant.now());
            bsplinkFile.setAcdmId(acdm.getId());

            BsplinkFile fileSaved = save(bsplinkFile);

            SimpleResponse simpleResponse;
            simpleResponse = new SimpleResponse(null, HttpStatus.OK);
            simpleResponse.setSubject(file.getOriginalFilename());
            simpleResponse.setId(fileSaved.getId());
            simpleResponse.setPath(fileSaved.getPath());

            simpleResponses.add(simpleResponse);
        }

        return simpleResponses;
    }

}
