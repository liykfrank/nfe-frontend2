package org.iata.bsplink.refund.model.repository;

import java.util.List;

import org.iata.bsplink.refund.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRefundId(Long id);
}

