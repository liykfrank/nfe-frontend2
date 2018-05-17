package org.iata.bsplink.filemanager.model.repository;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.pojo.BsplinkFileSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@FunctionalInterface
public interface CustomBsplinkFileRepository {

    Page<BsplinkFile> find(BsplinkFileSearchCriteria searchCriteria, Pageable pageable, Sort sort);
}
