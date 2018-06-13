package org.iata.bsplink.agencymemo.service;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.iata.bsplink.agencymemo.dto.CommentRequest;
import org.iata.bsplink.agencymemo.model.entity.Comment;
import org.iata.bsplink.agencymemo.model.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Saves comments for Acdm.
     * @param commentRequest Request object to save.
     * @return Comment object.
     * @throws IllegalAccessException Cannot access to property.
     * @throws InvocationTargetException Cannot access to target.
     */
    public Comment save(CommentRequest commentRequest)
            throws IllegalAccessException, InvocationTargetException {

        Comment comment = new Comment();
        comment.setInsertDateTime(Instant.now());
        BeanUtils.copyProperties(comment, commentRequest);

        return commentRepository.save(comment);
    }

    public List<Comment> findByAcdmId(Long id) {
        return commentRepository.findByAcdmId(id);
    }

}
