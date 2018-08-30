import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { AcdmConfigurationService } from './acdm-configuration.service';

describe('AcdmConfigurationService', () => {
  const httpClientStub = jasmine.createSpyObj<HttpClient>('HttpClient', [
    'get'
  ]);
  httpClientStub.get.and.returnValue(of());

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule, TranslationModule.forRoot(l10nConfig)],
      providers: [
        { provide: HttpClient, useValue: httpClientStub },
        AcdmConfigurationService
      ]
    });
  });

  it('should be created', inject(
    [AcdmConfigurationService],
    (service: AcdmConfigurationService) => {
      expect(service).toBeTruthy();
    }
  ));

  it('getConfiguration', inject(
    [AcdmConfigurationService],
    (service: AcdmConfigurationService) => {
      expect(service.getConfiguration()).toBeTruthy();
    }
  ));

  it('getConfigurationByISO', inject(
    [AcdmConfigurationService],
    (service: AcdmConfigurationService) => {
      httpClientStub.get.calls.reset();
      service.getConfigurationByISO('ABC');
      expect(httpClientStub.get.calls.count()).toBe(
        1,
        'expected only one call'
      );
    }
  ));
});
