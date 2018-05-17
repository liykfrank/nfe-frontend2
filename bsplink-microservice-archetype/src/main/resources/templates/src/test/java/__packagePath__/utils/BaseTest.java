package @packageName@.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import @packageName@.model.entity.Resource;
import @packageName@.pojo.BaseRequest;
import @packageName@.pojo.BaseResponse;

public abstract class BaseTest {

    protected static final String GET_RESOURCE_LIST_URL = "/bsplink/resources";
    protected static final String GET_RESOURCE_URL = "/bsplink/resources/{id}";
    protected static final String CREATE_RESOURCE_URL = "/bsplink/resources";
    protected static final String UPDATE_RESOURCE_URL = "/bsplink/resources";
    protected static final String DELETE_RESOURCE_URL = "/bsplink/resources/{id}";

    protected static final String RESOURCE_ID = RandomStringUtils.randomNumeric(2);
    protected static final String DESCRIPTION = RandomStringUtils.randomAlphanumeric(10);
    protected static final Date CREATED = new Date();

    protected Resource resource;
    protected BaseResponse response;
    protected Stream<BaseResponse> responseStream;
    protected List<BaseResponse> responseList;
    protected BaseRequest request;

    /**
     * Creates the request for the tests.
     */
    protected void createRequest() {

        request = new BaseRequest();
        request.setDescription(DESCRIPTION);
    }

    /**
     * Creates the resource for the tests.
     */
    protected void createResource() {

        resource = new Resource();
        resource.setId(Long.valueOf(RESOURCE_ID));
        resource.setDescription(DESCRIPTION);
        resource.setCreated(CREATED);
    }

    /**
     * Common response assertions.
     *
     * @param response the response that is going to be checked
     */
    protected void commonResponseAssertions(BaseResponse response) {

        assertNotNull(response);
        assertEquals(RESOURCE_ID, response.getId());
        assertEquals(DESCRIPTION, response.getDescription());
        assertEquals(CREATED, response.getCreated());
    }

    /**
     * Common resource assertions.
     *
     * @param entity the entity that is going to be checked
     */
    protected void commonResourceAssertions(Resource entity) {

        assertNotNull(entity);
        assertEquals(Long.valueOf(RESOURCE_ID), entity.getId());
        assertEquals(DESCRIPTION, entity.getDescription());
        assertEquals(CREATED, entity.getCreated());
    }
}
