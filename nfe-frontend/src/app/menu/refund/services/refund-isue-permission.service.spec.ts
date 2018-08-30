import { HttpClientModule } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';

import { l10nConfig } from '../../../shared/base/conf/l10n.config';
import { RefundIsuePermissionService } from './refund-isue-permission.service';

describe('RefundIsuePermissionControllerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [RefundIsuePermissionService]
    });
  });

  it('should be created', inject(
    [RefundIsuePermissionService],
    (service: RefundIsuePermissionService) => {
      expect(service).toBeTruthy();
    }
  ));
});
