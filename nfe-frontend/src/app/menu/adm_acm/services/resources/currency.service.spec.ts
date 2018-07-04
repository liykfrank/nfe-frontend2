import { TestBed, inject } from '@angular/core/testing';

import { CurrencyService } from './currency.service';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { FilesService } from '../files.service';
import { AlertsService } from '../../../../core/services/alerts.service';
import { Observable } from 'rxjs/Observable';

describe('CurrencyService', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get', 'post']);
  const _FilesService = jasmine.createSpyObj<FilesService>('HttpClient', ['setFiles']);
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
        {provide: FilesService, useValue: _FilesService},
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

  it('getCurrencyWithISO', inject([CurrencyService], (service: CurrencyService) => {
    _HttpClient.get.calls.reset();
    _HttpClient.get.and.returnValue(Observable.of({}));
    const url = service.getUrl();

    service.getCurrencyWithISO('AAA');
    expect(_HttpClient.get.calls.count()).toBe(1);
    expect(url == service.getUrl()).toBe(true);
  }));

});
