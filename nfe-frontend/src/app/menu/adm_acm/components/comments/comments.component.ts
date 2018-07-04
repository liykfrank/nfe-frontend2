
import { Component, OnInit } from '@angular/core';
import { AdmAcmService } from '../../services/adm-acm.service';
import { CommentsService } from '../../services/comments.service';
import { ScreenType } from '../../../../shared/models/screen-type.enum';
import { CommentAcdm } from '../../models/comment-acdm.model';

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {
  status: ScreenType;

  new_comment = { text: null };
  texto: string;
  comments: CommentAcdm[] = [];

  constructor(
    private _AdmAcmService: AdmAcmService,
    private _CommentsService: CommentsService
  ) {
    this._AdmAcmService.getScreenType().subscribe(screenType => {
      // TODO: añadir el sistema de traducciones, o revisar esta parte para
      //      verificar el valor del ENUM y el resultado de la variable.
      this.status = ScreenType.CREATE;
      // if (this.status != ScreenType.CREATE) {
      //   this._AcdmsService.getComments_ID().subscribe(data => this.comments = data);
      // }
    });

    this._CommentsService.getComments().subscribe(comments => {
      this.comments = comments;
      this.new_comment = {text: null};
    });
  }

  ngOnInit() {

  }

  public saveComment() {
    this.new_comment.text = this.texto;
    this._CommentsService.setCommentToUpload(this.new_comment);
    this.texto = null;
  }
}
