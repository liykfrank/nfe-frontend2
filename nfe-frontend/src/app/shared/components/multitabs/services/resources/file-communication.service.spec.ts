import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';

import { l10nConfig } from '../../../../base/conf/l10n.config';
import { FileCommunicationService } from './file-communication.service';
import { MessageService } from 'primeng/components/common/messageservice';

describe('FileCommunicationService', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);
  const _MessageService = jasmine.createSpyObj<MessageService>('MessageService', ['add']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        {provide: HttpClient, useValue: _HttpClient},
        {provide: MessageService, useValue: _MessageService},
        FileCommunicationService
      ]
    });
  });

  it('should be created', inject([FileCommunicationService], (service: FileCommunicationService) => {
    expect(service).toBeTruthy();
  }));
/*
  it('getMessagesFromUploadFiles', inject([FileCommunicationService], (service: FileCommunicationService) => {
    expect(service.getMessagesFromUploadFiles()).toBeTruthy();
  }));

  it('setBaseURL', inject([FileCommunicationService], (service: FileCommunicationService) => {
    service.setBaseURL('TEST');
    expect(service.getUrl()).toBeTruthy('TEST');
  }));

  it('getFiles_ID', inject([FileCommunicationService], (service: FileCommunicationService) => {
    service.setBaseURL('TEST');

    _HttpClient.get.calls.reset();
    _HttpClient.get.and.returnValue(Observable.of({}));

    service.getFiles_ID(0);
    expect(_HttpClient.get.calls.count()).toBe(1);
    expect(service.getUrl()).toBe('TEST');
  }));

  it('postFiles', inject([FileCommunicationService], (service: FileCommunicationService) => {
    service.setBaseURL('TEST');

    _HttpClient.post.calls.reset();
    _HttpClient.post.and.returnValue(Observable.of({}));

    service.postFiles(0, [new File([], 'test')]);
    expect(_HttpClient.post.calls.count()).toBe(1);
    expect(service.getUrl()).toBe('TEST');
  }));
 */
});
