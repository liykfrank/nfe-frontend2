import { TestBed, inject } from '@angular/core/testing';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { AlertsService } from '../../../core/services/alerts.service';
import { Observable } from 'rxjs/Observable';
import { RefundConfigurationService } from './refund-configuration.service';

describe('RefundConfigurationService', () => {

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
        RefundConfigurationService
      ]
    });
  });

  it('should be created', inject([RefundConfigurationService], (service: RefundConfigurationService) => {
    expect(service).toBeTruthy();
  }));

  it('getConfiguration', inject([RefundConfigurationService], (service: RefundConfigurationService) => {
    expect(service.changeConfigurationByISO).toBeTruthy();
  }));


  it('getWithISO', inject([RefundConfigurationService], (service: RefundConfigurationService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.changeConfigurationByISO('ABC');
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');

  }));
});
