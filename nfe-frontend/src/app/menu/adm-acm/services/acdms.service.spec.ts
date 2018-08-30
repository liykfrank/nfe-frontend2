import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';

import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { Acdm } from '../models/acdm.model';
import { AcdmsService } from './acdms.service';

describe('AcdmsService', () => {
  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [{ provide: HttpClient, useValue: HTTP }, AcdmsService]
    });
  });

  it('should be created', inject([AcdmsService], (service: AcdmsService) => {
    expect(service).toBeTruthy();
  }));

  it('postAcdm', inject([AcdmsService], (service: AcdmsService) => {
    HTTP.post.calls.reset();
    HTTP.post.and.returnValue(Observable.of(200));
    const adcm = new Acdm();
    service.postAcdm(adcm);
    expect(HTTP.post.calls.count()).toBe(1, 'expected only one call');
  }));
});
