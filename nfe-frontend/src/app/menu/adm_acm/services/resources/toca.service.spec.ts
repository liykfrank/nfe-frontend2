import { TestBed, inject } from '@angular/core/testing';

import { TocaService } from './toca.service';
import { HttpClientModule } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';

describe('TocaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [TocaService]
    });
  });

  it('should be created', inject([TocaService], (service: TocaService) => {
    expect(service).toBeTruthy();
  }));
});
