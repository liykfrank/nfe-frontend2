import { TestBed, inject } from '@angular/core/testing';

import { FileService } from './file.service';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { AlertsService } from '../../../../core/services/alerts.service';
import { FilesService } from '../files.service';
import { Observable } from 'rxjs/Observable';

describe('FileService', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);
  const _FilesService = jasmine.createSpyObj<FilesService>('HttpClient', ['setFiles']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        FileService,
        {provide: FilesService, useValue: _FilesService},
        {provide: HttpClient, useValue: _HttpClient}
      ]
    });
  });

  it('should be created', inject([FileService], (service: FileService) => {
    expect(service).toBeTruthy();
  }));

  it('getMessagesFromUploadFiles', inject([FileService], (service: FileService) => {
    expect(service.getMessagesFromUploadFiles()).toBeTruthy();
  }));

  it('getFiles_ID', inject([FileService], (service: FileService) => {
    _HttpClient.get.calls.reset();
    _HttpClient.get.and.returnValue(Observable.of({}));
    const url = service.getUrl();

    service.getFiles_ID(0);
    expect(_HttpClient.get.calls.count()).toBe(1);
    expect(url == service.getUrl()).toBe(true);
  }));

  it('postFile', inject([FileService], (service: FileService) => {
    _HttpClient.post.calls.reset();
    _HttpClient.post.and.returnValue(Observable.of({}));
    const url = service.getUrl();

    service.postFile(0, [new File([], 'test')]);
    expect(_HttpClient.post.calls.count()).toBe(1);
    expect(url == service.getUrl()).toBe(true);
  }));

});
