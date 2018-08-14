import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs';
import { AlertsService } from '../../../core/services/alerts.service';
import { ConfigurationService } from '../../files/services/configuration.service';
import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { AdmAcmConfigurationService } from './adm-acm-configuration.service';


describe('ConfigurationService', () => {
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', [
    'setAlertTranslate'
  ]);
  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        { provide: AlertsService, useValue: _AlertsService },
        { provide: HttpClient, useValue: HTTP },
        AdmAcmConfigurationService
      ]
    });
  });

  it('should be created', inject(
    [ConfigurationService],
    (service: ConfigurationService) => {
      expect(service).toBeTruthy();
    }
  ));
/*
  it('getConfiguration', inject(
    [ConfigurationService],
    (service: ConfigurationService) => {
      expect(service.getConfiguration()).toBeTruthy();
    }
  ));

  it('getWithISO', inject(
    [ConfigurationService],
    (service: ConfigurationService) => {
      HTTP.get.calls.reset();
      HTTP.get.and.returnValue(Observable.of(200));
      service.getWithISO('ABC');
      expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
    }
  )); */
});
