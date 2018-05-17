import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ConfigurationService } from './configuration.service';
import { Configuration } from '../models/configuration';
import { defer } from 'rxjs/observable/defer';
import { HttpErrorResponse, HttpClient, HttpClientModule } from '@angular/common/http';
import { Injector } from '@angular/core';
import { TestBed, inject, async } from '@angular/core/testing';

import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../shared/base/conf/l10n.config';

describe('ConfigurationService', () => {

  let httpClientSpy: { get: jasmine.Spy };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        ConfigurationService
      ]
    });

    this.httpClientSpy = jasmine.createSpyObj('HttpClient', ['get']);

  });

  it('should return expected configuration (HttpClient called once)', () => {
    const expectedConfiguration: Configuration = {
        allowedFileExtensions: ['pdf', 'txt'],
        fileNamePatterns: ['.*'],
        maxDownloadFilesNumber: 4,
        maxDownloadTotalFileSizeForMultipleFiles: 2000,
        maxUploadFileSize: '500',
        maxUploadFilesNumber: 2
    };

    this.httpClientSpy.get.and.returnValue(asyncData(expectedConfiguration));
    inject([ConfigurationService], (configurationService: ConfigurationService) => {
      configurationService.get().subscribe(
        configuration => expect(configuration).toEqual(expectedConfiguration, 'expected configuration'),
        fail
      );
      expect(this.httpClientSpy.get.calls.count()).toBe(1, 'one call');
    });
  });

  it('should return an error when the server returns a 404', () => {
    const errorResponse = new HttpErrorResponse({
      error: 'test 404 error',
      status: 404, statusText: 'Not Found'
    });

    this.httpClientSpy.get.and.returnValue(asyncError(errorResponse));
    inject([ConfigurationService], (configurationService: ConfigurationService) => {
      configurationService.get().subscribe(
        configuration => fail('expected an error, not configuration'),
        error  => expect(error.message).toContain('404 Not Found')
      );
    });
  });

  function asyncData<T>(data: T) {
    return defer(() => Promise.resolve(data));
  }

  function asyncError<T>(errorObject: any) {
    return defer(() => Promise.reject(errorObject));
  }

});
