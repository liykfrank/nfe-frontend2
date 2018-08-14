import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';

import { CommentCommunicationService } from './comment-communication.service';

describe('CommentCommunicationService', () => {
  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        { provide: HttpClient, useValue: HTTP },
        CommentCommunicationService
      ]
    });
  });

  it('should be created', inject(
    [CommentCommunicationService],
    (service: CommentCommunicationService) => {
      expect(service).toBeTruthy();
    }
  ));
});
