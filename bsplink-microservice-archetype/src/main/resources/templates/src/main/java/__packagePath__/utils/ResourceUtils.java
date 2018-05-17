package @packageName@.utils;

import lombok.extern.apachecommons.CommonsLog;

import @packageName@.model.entity.Resource;
import @packageName@.pojo.BaseRequest;
import @packageName@.pojo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommonsLog
public class ResourceUtils {

    private static Logger logger = LoggerFactory.getLogger(BaseUtils.class);

    private BaseUtils() {}

    /**
     * Map resource to response.
     *
     * @param resource the resource
     * @return the base response
     */
    public static BaseResponse mapResourceToResponse(Resource resource) {

        BaseResponse response = new BaseResponse();
        response.setId(String.valueOf(resource.getId()));
        response.setDescription(resource.getDescription());
        response.setCreated(resource.getCreated());
        response.setModified(resource.getModified());

        logger.debug("built response : {}", response);

        return response;
    }

    /**
     * Map request to resource.
     *
     * @param request the request
     * @return the resource
     */
    public static Resource mapRequestToResource(BaseRequest request) {

        Resource resource = new Resource();
        resource.setDescription(request.getDescription());

        logger.debug("built resource : {}", resource);

        return resource;
    }
}
