package @packageName@.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;

import java.util.Optional;

import @packageName@.exception.BaseErrorEnum;
import @packageName@.exception.BaseException;
import @packageName@.model.dao.BaseDao;
import @packageName@.model.entity.Resource;
import @packageName@.utils.BaseTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ResourceServiceTest extends BaseTest {

    private ResourceServiceImpl service;

    @MockBean
    private BaseDao dao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Initializes the required variables before running the tests.
     *
     * @throws Exception the exception thrown during the initialization
     */
    @Before
    public void init() {

        this.service = new ResourceServiceImpl(dao);
        
        createRequest();
        createResource();
    }

    /**
     * Get resource list test.
     */
    @Test
    public void getResourceListTest() {

        doReturn(Lists.newArrayList(resource).stream()).when(dao).getResourceList();

        responseList = service.getResourceList();

        assertNotNull(responseList);
        assertThat(responseList.size()).isEqualTo(1);
        commonResponseAssertions(responseList.get(0));

        verify(dao, times(1)).getResourceList();
    }

    /**
     * Get a specific resource test.
     */
    @Test
    public void getResourceTest() {

        doReturn(Optional.of(resource)).when(dao).getResource(Long.valueOf(RESOURCE_ID));

        response = service.getResource(RESOURCE_ID);

        commonResponseAssertions(response);

        verify(dao, times(1)).getResource(Long.valueOf(RESOURCE_ID));
    }

    /**
     * Get a non-existing resource test.
     */
    @Test
    public void getResourceNotFoundTest() {

        expectedException.expect(BaseException.class);
        expectedException.expectMessage(BaseErrorEnum.RESOURCE_NOT_FOUND.getDescription()
                .replace("{0}", String.valueOf(RESOURCE_ID)));

        doReturn(Optional.empty()).when(dao).getResource(Long.valueOf(RESOURCE_ID));

        response = service.getResource(RESOURCE_ID);
    }

    /**
     * Create resource test.
     */
    @Test
    public void createResourceTest() {

        doReturn(resource).when(dao).saveResource(any(Resource.class));

        response = service.createResource(request);

        commonResponseAssertions(response);

        verify(dao, times(1)).saveResource(any(Resource.class));
    }

    /**
     * Update resource test.
     */
    @Test
    public void updateResourceTest() {

        doReturn(Optional.of(resource)).when(dao).getResource(Long.valueOf(RESOURCE_ID));
        doReturn(resource).when(dao).updateResource(any(Resource.class));

        response = service.updateResource(RESOURCE_ID, request);

        commonResponseAssertions(response);

        verify(dao, times(1)).updateResource(any(Resource.class));
    }

    /**
     * Delete resource test.
     */
    @Test
    public void updateResourceNotFoundTest() {
        
        expectedException.expect(BaseException.class);
        expectedException.expectMessage(BaseErrorEnum.RESOURCE_NOT_FOUND.getDescription()
                .replace("{0}", String.valueOf(RESOURCE_ID)));

        doReturn(Optional.empty()).when(dao).getResource(Long.valueOf(RESOURCE_ID));

        service.updateResource(RESOURCE_ID, request);
    }
    
    /**
     * Delete resource test.
     */
    @Test
    public void deleteResourceTest() {

        doReturn(Optional.of(resource)).when(dao).getResource(Long.valueOf(RESOURCE_ID));
        doNothing().when(dao).deleteResource(resource);

        service.deleteResource(RESOURCE_ID);

        verify(dao, times(1)).deleteResource(resource);
    }
    
    /**
     * Delete resource not found test.
     */
    @Test
    public void deleteResourceNotFoundTest() {
        
        expectedException.expect(BaseException.class);
        expectedException.expectMessage(BaseErrorEnum.RESOURCE_NOT_FOUND.getDescription()
                .replace("{0}", String.valueOf(RESOURCE_ID)));

        doReturn(Optional.empty()).when(dao).getResource(Long.valueOf(RESOURCE_ID));

        service.deleteResource(RESOURCE_ID);
    }
}
