import { TestBed, inject } from '@angular/core/testing';

import { CountryService } from './country.service';

import { ConfigurationService } from './configuration.service';
import { HttpClientModule } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';

describe('CountryService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [CountryService]
    });
  });

  it('should be created', inject([CountryService], (service: CountryService) => {
    expect(service).toBeTruthy();
  }));
});
