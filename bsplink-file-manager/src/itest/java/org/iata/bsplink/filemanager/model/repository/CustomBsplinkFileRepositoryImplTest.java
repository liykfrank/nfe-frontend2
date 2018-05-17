package org.iata.bsplink.filemanager.model.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.List;

import org.iata.bsplink.filemanager.model.entity.BsplinkFile;
import org.iata.bsplink.filemanager.model.entity.BsplinkFileStatus;
import org.iata.bsplink.filemanager.pojo.BsplinkFileSearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql("/fixtures/sql/user_files.sql")
public class CustomBsplinkFileRepositoryImplTest {

    private static int TOTAL_FIXTURE_FILES = 15;

    @Autowired
    CustomBsplinkFileRepositoryImpl repository;

    private BsplinkFileSearchCriteria searchCriteria;
    private Pageable pageable;
    private Sort sort;

    @Before
    public void setUp() {

        searchCriteria = new BsplinkFileSearchCriteria();
        pageable = PageRequest.of(0, 20);
        sort = mock(Sort.class);
    }

    @Test
    public void testReturnsAllFilesIfThereIsNoFilter() {

        assertThat(repository.find(searchCriteria, pageable, sort).getContent(),
                hasSize(TOTAL_FIXTURE_FILES));
    }

    @Test
    public void testFindsFilesFilteredById() {

        searchCriteria.setId(2L);

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(1));
        assertThat(files.get(0).getId(), equalTo(2L));
    }

    @Test
    public void testFindsFilesFilteredByName() {

        searchCriteria.setName("ILei8385_20180128_file1");

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(1));
        assertThat(files.get(0).getName(), equalTo("ILei8385_20180128_file1"));
    }

    @Test
    public void testFindsFilesFilteredByFileType() {

        searchCriteria.setType("fileType4");

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(1));
        assertThat(files.get(0).getType(), equalTo("fileType4"));
    }

    @Test
    public void testFindsFilesFilteredByFileStatus() {

        searchCriteria.setStatus(BsplinkFileStatus.DELETED);

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(1));
        assertThat(files.get(0).getStatus(), equalTo(BsplinkFileStatus.DELETED));
    }

    @Test
    public void testFindsFilesFilteredByMinUploadDate() {

        Instant instant = Instant.parse("2018-01-05T00:00:00Z");

        searchCriteria.setMinUploadDateTime(instant);

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(11));
        assertThat(files.get(0).getId(), equalTo(5L));
    }

    @Test
    public void testFindsFilesFilteredByMaxUploadDate() {

        Instant instant = Instant.parse("2018-01-01T00:00:00Z");

        searchCriteria.setMaxUploadDateTime(instant);

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(1));
        assertThat(files.get(0).getId(), equalTo(1L));
    }

    @Test
    public void testFindsFilesFilteredByMinBytes() {

        Long bytes = 1005L;

        searchCriteria.setMinBytes(bytes);

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(11));
        assertThat(files.get(0).getId(), equalTo(5L));
    }

    @Test
    public void testFindsFilesFilteredByMaxBytes() {

        Long bytes = 1010L;

        searchCriteria.setMaxBytes(bytes);

        List<BsplinkFile> files = repository.find(searchCriteria, pageable, sort).getContent();

        assertThat(files, hasSize(10));
        assertThat(files.get(0).getId(), equalTo(1L));
    }

    @Test
    public void testDoesPaginationWhenThereIsNoFilter() {

        Page<BsplinkFile> page0 = repository.find(searchCriteria, PageRequest.of(0, 2), sort);

        assertThat(page0.getNumber(), equalTo(0));
        assertThat(page0.getTotalPages(), equalTo(8));
        assertThat(page0.getTotalElements(), equalTo((long)TOTAL_FIXTURE_FILES));

        List<BsplinkFile> page0Contents = page0.getContent();

        assertThat(page0Contents, hasSize(2));
        assertThat(page0Contents.get(0).getId(), equalTo(1L));
        assertThat(page0Contents.get(1).getId(), equalTo(2L));

        Page<BsplinkFile> page1 = repository.find(searchCriteria, PageRequest.of(1, 2), sort);

        List<BsplinkFile> page1Contents = page1.getContent();

        assertThat(page1Contents, hasSize(2));
        assertThat(page1Contents.get(0).getId(), equalTo(3L));
        assertThat(page1Contents.get(1).getId(), equalTo(4L));

        Page<BsplinkFile> page2 = repository.find(searchCriteria, PageRequest.of(2, 2), sort);

        List<BsplinkFile> page2Contents = page2.getContent();

        assertThat(page2Contents, hasSize(2));
        assertThat(page2Contents.get(0).getId(), equalTo(5L));
    }

    @Test
    public void testSortsFilesByOneField() {

        Sort sort = Sort.by("id").descending();

        List<BsplinkFile> files = repository
                .find(searchCriteria, PageRequest.of(0, 20), sort)
                .getContent();

        assertThat(files, hasSize(15));
        assertThat(files.get(1).getId(), equalTo(14L));
        assertThat(files.get(5).getId(), equalTo(10L));
    }

    @Test
    public void testSortsFilesByMoreThanOneField() {

        Order statusOrder = Order.asc("status");
        Order idOrder = Order.desc("id");

        Sort sort = Sort.by(statusOrder, idOrder);

        List<BsplinkFile> files = repository
                .find(searchCriteria, PageRequest.of(0, 20), sort)
                .getContent();

        assertThat(files, hasSize(15));
        assertThat(files.get(4).getId(), equalTo(3L));
    }

}
