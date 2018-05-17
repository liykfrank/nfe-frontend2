package @packageName@.model.repository;

import @packageName@.model.entity.Resource;
import @packageName@.pojo.ResourceSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@FunctionalInterface
public interface CustomResourceRepository {

    Page<Resource> find(ResourceSearchCriteria searchCriteria, Pageable pageable, Sort sort);
}
