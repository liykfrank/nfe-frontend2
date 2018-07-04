import { TestBed, inject } from '@angular/core/testing';

import { CommentsService } from './comments.service';
import { CommentAcdm } from '../models/comment-acdm.model';

describe('CommentsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CommentsService]
    });
  });

  it('should be created', inject([CommentsService], (service: CommentsService) => {
    expect(service).toBeTruthy();
  }));

  it('getComments', inject([CommentsService], (service: CommentsService) => {
    expect(service.getComments()).toBeTruthy();
  }));

  it('getCommentsToUpload', inject([CommentsService], (service: CommentsService) => {
    expect(service.getCommentsToUpload()).toBeTruthy();
  }));


  it('setComments', inject([CommentsService], (service: CommentsService) => {
    let elem;
    service.getComments().subscribe(data => elem = data);
    const msg : CommentAcdm = new CommentAcdm();
    service.setComments(msg);
    expect(elem.length).toBeGreaterThan(0);
  }));

  it('setCommentToUpload', inject([CommentsService], (service: CommentsService) => {
    let elem;
    service.getCommentsToUpload().subscribe(data => elem = data);
    const msg : CommentAcdm = new CommentAcdm();
    service.setCommentToUpload(msg);
    expect(elem).toBeTruthy();
  }));




});
