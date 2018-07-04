import { TestBed, inject } from '@angular/core/testing';

import { ConfigurationService } from './configuration.service';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { AlertsService } from '../../../../core/services/alerts.service';
import { Observable } from 'rxjs';

describe('ConfigurationService', () => {

  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);
  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        {provide: AlertsService, useValue: _AlertsService},
        {provide: HttpClient, useValue: HTTP},
        ConfigurationService
      ]
    });
  });

  it('should be created', inject([ConfigurationService], (service: ConfigurationService) => {
    expect(service).toBeTruthy();
  }));

  it('getConfiguration', inject([ConfigurationService], (service: ConfigurationService) => {
    expect(service.getConfiguration()).toBeTruthy();
  }));


  it('getWithISO', inject([ConfigurationService], (service: ConfigurationService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.getWithISO('ABC');
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');

  }));
});
