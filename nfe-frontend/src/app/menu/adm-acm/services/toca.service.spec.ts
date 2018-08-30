import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';

import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { TocaService } from './toca.service';
import { AlertsService } from '../../../core/services/alerts.service';

describe('TocaService', () => {
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', [
    'setAlertTranslate'
  ]);
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [TocaService, { provide: HttpClient, useValue: _HttpClient }]
    });
  });

  it('should be created', inject([TocaService], (service: TocaService) => {
    expect(service).toBeTruthy();
  }));

  it('getTocaWithISO, 200', inject([TocaService], (service: TocaService) => {
    _HttpClient.get.and.returnValue(Observable.of(200));
    _HttpClient.get.calls.reset();
    const url = service.getUrl();

    service.getTocaWithISO('AAA');
    expect(_HttpClient.get.calls.count()).toBe(1, 'OK');
    expect(service.getUrl() == url).toBe(true);
  }));
});
