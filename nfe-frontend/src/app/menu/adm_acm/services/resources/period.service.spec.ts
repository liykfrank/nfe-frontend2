import { TestBed, inject } from '@angular/core/testing';

import { PeriodService } from './period.service';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { AlertsService } from '../../../../core/services/alerts.service';
import { Observable } from 'rxjs/Observable';

describe('PeriodService', () => {
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        PeriodService,
        {provide: AlertsService, useValue: _AlertsService},
        {provide: HttpClient, useValue: _HttpClient}
      ]
    });
  });

  it('should be created', inject([PeriodService], (service: PeriodService) => {
    expect(service).toBeTruthy();
  }));

  it('getToca', inject([PeriodService], (service: PeriodService) => {
    expect(service.getPeriod()).toBeTruthy();
  }));

  it('getPeriodWithISO, 200', inject([PeriodService], (service: PeriodService) => {
    _HttpClient.get.and.returnValue(Observable.of(200));
    _HttpClient.get.calls.reset();
    const url = service.getUrl();

    service.getPeriodWithISO('AAA');
    expect(_HttpClient.get.calls.count()).toBe(1, 'OK');
    expect(service.getUrl() == url).toBe(true);
  }));

});
