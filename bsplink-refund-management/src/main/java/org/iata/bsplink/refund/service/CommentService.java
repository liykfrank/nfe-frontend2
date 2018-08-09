package org.iata.bsplink.refund.service;

import java.time.Instant;
import java.util.List;

import lombok.extern.java.Log;

import org.iata.bsplink.refund.dto.CommentRequest;
import org.iata.bsplink.refund.model.entity.Comment;
import org.iata.bsplink.refund.model.entity.RefundAction;
import org.iata.bsplink.refund.model.entity.RefundHistory;
import org.iata.bsplink.refund.model.repository.CommentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RefundHistoryService refundHistoryService;

    public CommentService(CommentRepository commentRepository,
            RefundHistoryService refundHistoryService) {
        this.commentRepository = commentRepository;
        this.refundHistoryService = refundHistoryService;
    }

    /**
     * Saves comments for Refund.
     *
     * @param commentRequest Request object to save.
     * @return Comment object.
     */
    public Comment save(CommentRequest commentRequest, Long refundId) {

        log.info("Saving Comment: " + commentRequest);

        Comment comment = new Comment();
        comment.setRefundId(refundId);
        comment.setInsertDateTime(Instant.now());
        BeanUtils.copyProperties(commentRequest, comment);

        RefundHistory refundHistory = new RefundHistory();
        refundHistory.setAction(RefundAction.ADD_COMMENT);
        refundHistory.setInsertDateTime(Instant.now());
        refundHistory.setRefundId(refundId);

        Comment commenSaved = commentRepository.save(comment);

        log.info("Saving refund history: " + refundHistory);
        refundHistoryService.save(refundHistory);

        log.info("Comment saved: " + commenSaved);

        return commenSaved;
    }

    public List<Comment> findByRefundId(Long id) {
        log.info("Getting comment list by refund id: " + id);
        return commentRepository.findByRefundId(id);
    }

}

