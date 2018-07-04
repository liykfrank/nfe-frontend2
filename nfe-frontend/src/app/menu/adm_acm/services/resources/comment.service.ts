import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';

import { CommentsService } from './../comments.service';
import { CommentAcdm } from '../../models/comment-acdm.model';

@Injectable()
export class CommentService extends NwRepositoryAbstract<any, Object> {
  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.acdm;

  constructor(
    private _CommentsService: CommentsService,
    private http: HttpClient,
    injector: Injector) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
      environment.adm_acm.api.acdm,
      injector
    );
  }

  public postComment(id: number, comment: string): void {
    this.configureUrl(this.getUrl([id.toString(), 'comments']));

    const commentToSend = { text: comment };
    const header = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    this.postSingle<CommentAcdm>(JSON.stringify(commentToSend), header)
      .finally(() => this.configureUrl(this.base))
      .subscribe(data => {
        this._CommentsService.setComments(data);
      });
  }

  public getComments_ID(id: number): void {
    this.configureUrl(this.getUrl([id.toString()]));

    this.getSingle<CommentAcdm>()
      .finally(() => this.configureUrl(this.base))
      .subscribe(resp => {
        this._CommentsService.setComments(resp);
      });
  }

}
