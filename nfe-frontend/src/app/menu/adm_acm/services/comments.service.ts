import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { Acdm } from './../models/acdm.model';
import { CommentAcdm } from './../models/comment-acdm.model';

@Injectable()
export class CommentsService {

  private $comments = new BehaviorSubject<CommentAcdm[]>([]);
  private $comment_to_upload = new BehaviorSubject<any>({});

  constructor() { }

  public getComments(): Observable<CommentAcdm[]> {
    return this.$comments.asObservable();
  }

  public setComments(comment: CommentAcdm): void {
    const comments = this.$comments.getValue();
    comments.push(comment);
    this.$comments.next(comments);
  }

  public getCommentsToUpload(): Observable<any> {
    return this.$comment_to_upload.asObservable();
  }

  public setCommentToUpload(comment: any): void {
    this.$comment_to_upload.next(comment);
  }

}
