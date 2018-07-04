import { TestBed, inject } from '@angular/core/testing';

import { CompanyService } from './company.service';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { Observable } from 'rxjs/Observable';
import { AlertsService } from '../../../../core/services/alerts.service';

describe('CompanyService', () => {

  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);

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
        CompanyService
      ]
    });
  });

  it('should be created', inject([CompanyService], (service: CompanyService) => {
    expect(service).toBeTruthy();
  }));

  it('getAirlineCountryAirlineCode', inject([CompanyService], (service: CompanyService) => {
    const observable = service.getAirlineCountryAirlineCode();
    expect(observable).toBeTruthy();
  }));

  it('getFromServerAirlineCountryAirlineCode', inject([CompanyService], (service: CompanyService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.getFromServerAirlineCountryAirlineCode('AAA', 'AAA');
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
  }));

});
