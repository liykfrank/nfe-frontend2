import { TestBed, inject } from '@angular/core/testing';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';
import { HttpClientModule, HttpClient } from '@angular/common/http';

import { l10nConfig } from '../../../../shared/base/conf/l10n.config';

import { AcdmsService } from './acdms.service';
import { Acdm } from '../../models/acdm.model';

describe('AcdmsService', () => {

  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        {provide: HttpClient, useValue: HTTP},
        AcdmsService
      ]
    });
  });

  it('should be created', inject([AcdmsService], (service: AcdmsService) => {
    expect(service).toBeTruthy();
  }));

  it('getAcdmFromID', inject([AcdmsService], (service: AcdmsService) => {
    const observable = service.getAcdmFromID();
    expect(observable).toBeTruthy();
  }));

  it('getAcdm_ID', inject([AcdmsService], (service: AcdmsService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.getAcdm_ID(0);
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
  }));

  it('postAcdm', inject([AcdmsService], (service: AcdmsService) => {
    HTTP.post.calls.reset();
    HTTP.post.and.returnValue(Observable.of(200));
    let adcm = new Acdm();
    service.postAcdm(adcm);
    expect(HTTP.post.calls.count()).toBe(1, 'expected only one call');
  }));

});
