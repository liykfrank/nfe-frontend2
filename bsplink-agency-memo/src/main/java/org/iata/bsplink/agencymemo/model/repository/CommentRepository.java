package org.iata.bsplink.agencymemo.model.repository;

import java.util.List;

import org.iata.bsplink.agencymemo.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAcdmId(Long id);
}
