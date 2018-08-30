import { HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';

import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { RefundIndirectService } from './refund-indirect.service';

describe('RefundIndirectService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [RefundIndirectService]
    });
  });

  it('should be created', inject(
    [RefundIndirectService],
    (service: RefundIndirectService) => {
      expect(service).toBeTruthy();
    }
  ));
});
