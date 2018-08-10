import { Component, EventEmitter, Injector, Input, Output } from '@angular/core';
import { CommentDataServer } from '../../models/comment-data-server.model';

@Component({
  selector: 'bspl-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent {
  @Input() comments: CommentDataServer[] = [];
  @Input() commentToUpload: CommentDataServer;

  @Input() disabled = false;

  @Output() saveComment = new EventEmitter();

  text: string;

  constructor() { }

  public saveCommentWithEnter(event: any) {
    if (event.keyCode === 13) {
      this.saveCommentWithClick();
    }
  }

  public saveCommentWithClick() {
    const valid = this.text && this.text.length > 0;

    if (valid) {
      this.saveComment.emit(this.text);
      this.text = '';
    }
  }

  public checkComments() {
    return this.comments.length > 0;
  }
}
