import { TestBed, inject } from '@angular/core/testing';

import { ReasonService } from './reason.service';
import { HttpClientModule } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';

describe('ReasonService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [ReasonService]
    });
  });

  it('should be created', inject([ReasonService], (service: ReasonService) => {
    expect(service).toBeTruthy();
  }));
});
