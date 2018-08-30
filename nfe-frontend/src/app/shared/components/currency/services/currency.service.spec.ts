import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';

import { AlertsService } from '../../../../core/services/alerts.service';
import { l10nConfig } from '../../../base/conf/l10n.config';
import { EnvironmentType } from '../../../enums/environment-type.enum';
import { CurrencyService } from './currency.service';

describe('CurrencyService', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', [
    'get',
    'post'
  ]);
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', [
    'setAlertTranslate'
  ]);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        CurrencyService,
        { provide: AlertsService, useValue: _AlertsService },
        { provide: HttpClient, useValue: _HttpClient }
      ]
    });
  });

  it('should be created', inject(
    [CurrencyService],
    (service: CurrencyService) => {
      expect(service).toBeTruthy();
    }
  ));

  it('getWithISO', inject([CurrencyService], (service: CurrencyService) => {
    service.getWithISO(EnvironmentType.REFUND_INDIRECT, 'AAA');
    expect(_HttpClient.get.calls.count()).toBe(1);
  }));
});
