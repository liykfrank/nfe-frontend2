package org.iata.bsplink.agencymemo.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.iata.bsplink.agencymemo.dto.CommentRequest;
import org.iata.bsplink.agencymemo.model.entity.Comment;
import org.iata.bsplink.agencymemo.model.repository.CommentRepository;
import org.junit.Before;
import org.junit.Test;

public class CommentServiceTest {

    private CommentService commentService;
    private CommentRepository commentRepository;

    @Before
    public void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void testSave() throws IllegalAccessException, InvocationTargetException {

        Comment commentToSave = getComments().get(0);

        when(commentRepository.save(any())).thenReturn(commentToSave);

        Comment commentSaved = commentService.save(getCommentsRequest().get(0));

        assertThat(commentToSave.getId(), equalTo(commentSaved.getId()));
        assertThat(commentToSave.getAcdmId(), equalTo(commentSaved.getAcdmId()));
        assertThat(commentToSave.getText(), equalTo(commentSaved.getText()));
    }

    @Test
    public void testFindByAcdmId() {

        List<Comment> listToFind = getComments();

        when(commentRepository.findByAcdmId(1L)).thenReturn(listToFind);
        
        List<Comment> listFound = commentService.findByAcdmId(1L);
        
        assertThat(listFound.size(), equalTo(listToFind.size()));
        assertThat(listFound.get(0), equalTo(listToFind.get(0)));
        assertThat(listFound.get(1), equalTo(listToFind.get(1)));

    }

    /**
     * List of comments entities.
     * 
     * @return Comment
     */
    public List<Comment> getComments() {

        Comment commentOne = new Comment();
        commentOne.setAcdmId(98798798L);
        commentOne.setId(1L);
        commentOne.setInsertDateTime(Instant.now());
        commentOne.setText("Comment one");

        List<Comment> comments = new ArrayList<>();
        comments.add(commentOne);

        Comment commentTwo = new Comment();
        commentTwo.setAcdmId(98798798L);
        commentTwo.setId(2L);
        commentTwo.setInsertDateTime(Instant.now());
        commentTwo.setText("Comment two");

        comments.add(commentTwo);

        return comments;
    }


    /**
     * Returns a list of comments to save.
     * 
     * @return CommentRequest
     */
    public List<CommentRequest> getCommentsRequest() {

        CommentRequest requestOne = new CommentRequest();
        requestOne.setAcdmId(98798798L);
        requestOne.setText("Text one");

        List<CommentRequest> listComments = new ArrayList<>();
        listComments.add(requestOne);

        CommentRequest requestTwo = new CommentRequest();
        requestTwo.setAcdmId(98798798L);
        requestTwo.setText("Text one");
        listComments.add(requestTwo);

        return listComments;
    }

}
