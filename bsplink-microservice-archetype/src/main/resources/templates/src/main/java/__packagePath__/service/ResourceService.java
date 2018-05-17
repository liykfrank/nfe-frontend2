package @packageName@.service;

import @packageName@.pojo.ResourceSearchCriteria;
import @packageName@.pojo.BaseRequest;
import @packageName@.model.entity.Resource;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ResourceService {

    List<Resource> getResourceList();
    
    Page<Resource> find(ResourceSearchCriteria searchCriteria, Pageable pageable,
            Sort sort);

    Resource getResource(String resourceId);

    Resource createResource(BaseRequest request);

    Resource updateResource(String resourceId, BaseRequest request);

    void deleteResource(String resourceId);
}
