package @packageName@.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.apachecommons.CommonsLog;

import @packageName@.model.entity.Resource;
import @packageName@.model.repository.ResourceRepository;
import @packageName@.pojo.ResourceSearchCriteria;
import @packageName@.service.ResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Service
@CommonsLog
public class ResourceServiceImpl implements ResourceService {

    private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {

        this.resourceRepository = resourceRepository;
    }
    
    @Override
    public List<BaseResponse> getResourceList() {

        log.info("Getting resource list");

        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(Resource::getCreated));
    }
    
    @Override
    public Page<Resource> find(ResourceSearchCriteria searchCriteria, Pageable pageable,
            Sort sort) {

        return bsplinkFileRepository.find(searchCriteria, pageable, sort);
    }
    
    @Override
    public BaseResponse getResource(String resourceId) {

        log.info("Getting resource with id: {}", resourceId);

        return repository.findById(resourceId).getResource(Long.valueOf(resourceId)).map(BaseUtils::mapResourceToResponse)
                .orElseThrow(() -> new BaseException(BaseErrorEnum.RESOURCE_NOT_FOUND, resourceId));
    }

    @Override
    public BaseResponse createResource(BaseRequest request) {

        log.info("Creating new resource");

        return BaseUtils
                .mapResourceToResponse(dao.saveResource(BaseUtils.mapRequestToResource(request)));
    }

    @Override
    public BaseResponse updateResource(String resourceId, BaseRequest request) {

        log.info("Updating resource with id: {}", resourceId);

        Resource resource = dao.getResource(Long.valueOf(resourceId))
                .orElseThrow(() -> new BaseException(BaseErrorEnum.RESOURCE_NOT_FOUND, resourceId));

        resource.setDescription(request.getDescription());

        return BaseUtils.mapResourceToResponse(dao.updateResource(resource));
    }

    @Override
    public void deleteResource(String resourceId) {

        log.info("Deleting resource with id: {}", resourceId);

        dao.deleteResource(dao.getResource(Long.valueOf(resourceId))
               .orElseThrow(() -> new BaseException(BaseErrorEnum.RESOURCE_NOT_FOUND, resourceId)));
    }
}
