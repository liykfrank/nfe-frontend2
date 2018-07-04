import { TestBed, inject } from '@angular/core/testing';

import { CommentService } from './comment.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { CommentsService } from '../comments.service';
import { Observable } from 'rxjs';

describe('CommentService', () => {

  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);
  const _CommentsService = jasmine.createSpyObj<CommentsService>('CommentsService', ['setComments']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        {provide: HttpClient, useValue: HTTP},
        {provide: CommentsService, useValue: _CommentsService},
        CommentService
      ]
    });
  });

  it('should be created', inject([CommentService], (service: CommentService) => {
    expect(service).toBeTruthy();
  }));

  it('getComments_ID', inject([CommentService], (service: CommentService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.getComments_ID(0);
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
  }));

  it('postComment', inject([CommentService], (service: CommentService) => {
    HTTP.post.calls.reset();
    HTTP.post.and.returnValue(Observable.of(200));
    service.postComment(0, 'text');
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
  }));


});
