import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { Observable } from 'rxjs/Observable';

import { PeriodService } from './period.service';
import { l10nConfig } from '../../../shared/base/conf/l10n.config';

describe('PeriodService', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [PeriodService, { provide: HttpClient, useValue: _HttpClient }]
    });
  });

  it('should be created', inject([PeriodService], (service: PeriodService) => {
    expect(service).toBeTruthy();
  }));

  it('getPeriodWithISO, 200', inject(
    [PeriodService],
    (service: PeriodService) => {
      _HttpClient.get.and.returnValue(Observable.of(200));
      _HttpClient.get.calls.reset();
      const url = service.getUrl();

      service.getPeriodWithISO('AAA');
      expect(_HttpClient.get.calls.count()).toBe(1, 'OK');
      expect(service.getUrl() == url).toBe(true);
    }
  ));
});
