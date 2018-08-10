import { TestBed, inject } from '@angular/core/testing';

import { SftpModifyPasswordService } from './sftp-modify-password.service';
import { HttpClientModule } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../shared/base/conf/l10n.config';

describe('SftpModifyPasswordService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [SftpModifyPasswordService]
    });
  });

  it('should be created', inject([SftpModifyPasswordService], (service: SftpModifyPasswordService) => {
    expect(service).toBeTruthy();
  }));
});
