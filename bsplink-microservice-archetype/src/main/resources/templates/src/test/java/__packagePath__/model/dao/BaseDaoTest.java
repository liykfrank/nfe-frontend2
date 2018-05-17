package @packageName@.model.dao;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Lists;

import java.util.Optional;
import java.util.stream.Stream;

import @packageName@.model.entity.Resource;
import @packageName@.model.repository.BaseRepository;
import @packageName@.utils.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BaseDaoTest extends BaseTest {

    private BaseDao dao;

    @MockBean
    private BaseRepository repository;

    /**
     * Initializes the required variables before running the tests.
     *
     * @throws Exception the exception thrown during the initialization
     */
    @Before
    public void init() {

        dao = new BaseDaoImpl(repository);

        createResource();
    }

    /**
     * Get resource list test.
     */
    @Test
    public void getResourceListTest() {

        doReturn(Lists.newArrayList(resource)).when(repository).findAll();

        Stream<Resource> resourceStream = dao.getResourceList();

        resourceStream.forEach(this::commonResourceAssertions);
    }

    /**
     * Get a specific resource test.
     */
    @Test
    public void getResourceTest() {

        doReturn(Optional.of(resource)).when(repository).findById(Long.valueOf(RESOURCE_ID));

        Optional<Resource> resourceOptional = dao.getResource(Long.valueOf(RESOURCE_ID));

        assertTrue(resourceOptional.isPresent());
        commonResourceAssertions(resourceOptional.get());
    }

    /**
     * Save resource test.
     */
    @Test
    public void saveResourceTest() {

        doReturn(resource).when(repository).save(resource);

        commonResourceAssertions(dao.saveResource(resource));
    }

    /**
     * Update resource test.
     */
    @Test
    public void updateResourceTest() {

        doReturn(resource).when(repository).save(resource);

        commonResourceAssertions(dao.updateResource(resource));
    }

    /**
     * Delete resource test.
     */
    @Test
    public void deleteResourceTest() {

        doNothing().when(repository).delete(resource);

        dao.deleteResource(resource);

        verify(repository, times(1)).delete(resource);
    }
}
