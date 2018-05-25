package org.iata.bsplink.filemanager.service;

import java.util.List;
import java.util.Optional;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.pojo.BsplinkFileSearchCriteria;
import org.iata.bsplink.filemanager.response.EntityActionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface BsplinkFileService {

    public Optional<BsplinkFile> findById(Long id);
    
    public List<BsplinkFile> updateStatusToTrashed(BsplinkFile file);

    public List<BsplinkFile> findAllById(Iterable<Long> ids);

    public BsplinkFile save(BsplinkFile file);   

    public BsplinkFile updateStatusToDownloaded(BsplinkFile file);

    public Page<BsplinkFile> find(BsplinkFileSearchCriteria searchCriteria, Pageable pageable,
            Sort sort);

    public void deleteOneFile(BsplinkFile file) throws Exception;

    public List<EntityActionResponse<Long>> deleteMultipleFiles(List<Long> ids);
}
