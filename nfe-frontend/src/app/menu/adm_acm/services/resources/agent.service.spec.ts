import { TestBed, inject } from '@angular/core/testing';

import { AgentService } from './agent.service';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { Observable } from 'rxjs/Observable';
import { AlertsService } from '../../../../core/services/alerts.service';

describe('AgentService', () => {
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
        AgentService
      ]
    });
  });

  it('should be created', inject([AgentService], (service: AgentService) => {
    expect(service).toBeTruthy();
  }));

  it('getAgent', inject([AgentService], (service: AgentService) => {
    const observable = service.getAgent();
    expect(observable).toBeTruthy();
  }));

  it('getAgentWithCode', inject([AgentService], (service: AgentService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.getAgentWithCode('ABC');
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
  }));

});
