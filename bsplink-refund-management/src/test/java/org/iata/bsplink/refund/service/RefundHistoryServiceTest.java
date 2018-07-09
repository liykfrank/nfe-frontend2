package org.iata.bsplink.refund.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.iata.bsplink.refund.test.fixtures.RefundFixtures.getRefundHistoryList;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.repository.RefundHistoryRepository;
import org.junit.Before;
import org.junit.Test;

public class RefundHistoryServiceTest {

    private RefundHistoryService refundHistoryService;
    private RefundHistoryRepository refundHistoryRepository;

    @Before
    public void setUp() {
        refundHistoryRepository = mock(RefundHistoryRepository.class);
        refundHistoryService = new RefundHistoryService(refundHistoryRepository);
    }

    @Test
    public void testFindByRefundId() {

        List<RefundHistory> listHistory = getRefundHistoryList();

        when(refundHistoryRepository.findByRefundId(1L)).thenReturn(listHistory);
        List<RefundHistory> listReturned = refundHistoryService.findByRefundId(1L);
        assertThat(listReturned, equalTo(listHistory));
        verify(refundHistoryRepository).findByRefundId(1L);
    }

    @Test
    public void testSave() {
        List<RefundHistory> listHistory = getRefundHistoryList();
        when(refundHistoryRepository.save(any())).thenReturn(listHistory.get(0));
        RefundHistory refundHistorySaved = refundHistoryService.save(getRefundHistoryList().get(0));
        assertThat(refundHistorySaved, equalTo(listHistory.get(0)));
        verify(refundHistoryRepository).save(any());

    }    
}
