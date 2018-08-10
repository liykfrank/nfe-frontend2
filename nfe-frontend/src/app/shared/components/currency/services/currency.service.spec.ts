import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';

import { AlertsService } from '../../../core/services/alerts.service';
import { l10nConfig } from '../../base/conf/l10n.config';
import { CurrencyService } from './currency.service';

describe('CurrencyService', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        CurrencyService,
        {provide: AlertsService, useValue: _AlertsService},
        {provide: HttpClient, useValue: _HttpClient}
      ]
    });
  });

  it('should be created', inject([CurrencyService], (service: CurrencyService) => {
    expect(service).toBeTruthy();
  }));

  it('getCurrency', inject([CurrencyService], (service: CurrencyService) => {
    expect(service.getCurrency()).toBeTruthy();
  }));

  it('setBaseURL', inject([CurrencyService], (service: CurrencyService) => {
    service.setBaseURL('TEST');
    expect(service.getUrl()).toBeTruthy('TEST');
  }));

  it('getCurrencyWithISO', inject([CurrencyService], (service: CurrencyService) => {
    service.setBaseURL('TEST');

    _HttpClient.get.calls.reset();
    _HttpClient.get.and.returnValue(Observable.of({}));

    const url = service.getUrl();

    service.getCurrencyWithISO('AAA');
    expect(_HttpClient.get.calls.count()).toBe(1);
    expect(url == service.getUrl()).toBe(true);
  }));

});
