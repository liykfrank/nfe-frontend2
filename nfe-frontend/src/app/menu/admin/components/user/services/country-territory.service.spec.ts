import { HttpClient, HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../../../../shared/base/conf/l10n.config';
import { CountryTerritoryService } from './country-territory.service';

describe('Service: CountryTerritory', () => {
  const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);
  _HttpClient.get.and.returnValue(of());

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        CountryTerritoryService,
        { provide: HttpClient, useValue: _HttpClient }
      ]
    });
  });

  it('should ...', inject(
    [CountryTerritoryService],
    (service: CountryTerritoryService) => {
      expect(service).toBeTruthy();
    }
  ));
});
