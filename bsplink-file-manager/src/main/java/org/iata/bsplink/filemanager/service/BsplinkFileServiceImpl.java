package org.iata.bsplink.filemanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.apachecommons.CommonsLog;

import org.iata.bsplink.filemanager.exception.BsplinkFileManagerException;
import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.model.repository.BsplinkFileRepository;
import org.iata.bsplink.filemanager.pojo.BsplinkFileSearchCriteria;
import org.iata.bsplink.filemanager.response.EntityActionResponse;
import org.iata.bsplink.filemanager.utils.BsplinkFileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class BsplinkFileServiceImpl implements BsplinkFileService {
    private BsplinkFileRepository bsplinkFileRepository;
    private BsplinkFileUtils bsplinkFileUtils;

    public BsplinkFileServiceImpl(BsplinkFileRepository bsplinkFileRepository,
            BsplinkFileUtils bsplinkFileUtils) {
        this.bsplinkFileRepository = bsplinkFileRepository;
        this.bsplinkFileUtils = bsplinkFileUtils;
    }

    @Override
    public Optional<BsplinkFile> findById(Long id) {
        return bsplinkFileRepository.findById(id);
    }

    @Override
    public List<BsplinkFile> updateStatusToTrashed(BsplinkFile file) {

        List<BsplinkFile> files = bsplinkFileRepository.findByName(file.getName());

        if (!files.isEmpty()) {

            List<BsplinkFile> filterList = files.stream()
                    .filter(f -> !f.getStatus().equals(BsplinkFileStatus.DELETED)
                            && !f.getStatus().equals(BsplinkFileStatus.TRASHED))
                    .collect(Collectors.toList());

            filterList.forEach(f -> f.setStatus(BsplinkFileStatus.TRASHED));

            filterList.forEach(f -> bsplinkFileRepository.save(f));

            return bsplinkFileRepository.findByName(file.getName());

        }

        return files;
    }

    @Override
    public List<BsplinkFile> findAllById(Iterable<Long> ids) {
        return bsplinkFileRepository.findAllById(ids);
    }

    @Override
    public BsplinkFile save(BsplinkFile file) {
        return bsplinkFileRepository.save(file);
    }

    @Override
    public BsplinkFile updateStatusToDownloaded(BsplinkFile file) {
        if (file.getStatus() == BsplinkFileStatus.NOT_DOWNLOADED) {
            file.setStatus(BsplinkFileStatus.DOWNLOADED);
            return bsplinkFileRepository.save(file);
        }
        return file;
    }

    @Override
    public Page<BsplinkFile> find(BsplinkFileSearchCriteria searchCriteria, Pageable pageable,
            Sort sort) {

        return bsplinkFileRepository.find(searchCriteria, pageable, sort);
    }

    @Override
    public void deleteOneFile(BsplinkFile file) throws BsplinkFileManagerException {
        try {
            bsplinkFileUtils.moveFileToEliminated(file.getName());
        } catch (Exception e) {
            throw new BsplinkFileManagerException(e);
        }
        List<BsplinkFile> deletedFiles = bsplinkFileRepository.findByNameAndStatus(file.getName(),
                BsplinkFileStatus.DELETED);
        deletedFiles.forEach(bsFile -> bsFile.setStatus(BsplinkFileStatus.TRASHED));
        bsplinkFileRepository.saveAll(deletedFiles);
        file.setStatus(BsplinkFileStatus.DELETED);
        bsplinkFileRepository.save(file);
    }

    @Override
    public List<EntityActionResponse<Long>> deleteMultipleFiles(List<Long> ids) {

        List<EntityActionResponse<Long>> result = new ArrayList<>();

        for (Long id : ids) {

            try {

                Optional<BsplinkFile> optionalFile = bsplinkFileRepository.findById(id);

                if (optionalFile.isPresent()) {

                    if (BsplinkFileStatus.DELETED.equals(optionalFile.get().getStatus())) {
                        result.add(new EntityActionResponse<>(id, HttpStatus.BAD_REQUEST));
                    } else {
                        BsplinkFile file = optionalFile.get();
                        deleteOneFile(file);

                        result.add(new EntityActionResponse<>(id, HttpStatus.OK, "deleted"));
                    }

                } else {

                    result.add(new EntityActionResponse<>(id, HttpStatus.NOT_FOUND));
                }

            } catch (Exception exception) {

                log.error("Error deleting file with id " + id, exception);

                result.add(new EntityActionResponse<>(id, HttpStatus.INTERNAL_SERVER_ERROR,
                        "Unable to delete file"));
            }
        }

        return result;
    }

}
