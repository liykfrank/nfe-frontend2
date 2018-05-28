import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { NwRepositoryAbstract } from '../../../shared/base/nwe-repository.abstract';
import { environment } from './../../../../environments/environment';
import { SftpAccountPassword, SftpAccountPasswordIf } from './../../models/sftp-account-password.model';
import { SftpAccountIf } from './../../models/sftp-account.model';


@Injectable()
export class SftpAccountsPasswordResource extends NwRepositoryAbstract<SftpAccountPasswordIf, Object> {
  constructor(http: HttpClient, injector: Injector) {
    super(
      http,
      environment.accounts.basePath + environment.accounts.api.accounts,
      injector
    );
  }

  public putPassword<T>(sftpAccount: SftpAccountIf, sftpPassword: SftpAccountPasswordIf): Observable<T> {
    return this.httpService.put<T>(
      this.getUrl([sftpAccount.login, 'password']),
      sftpPassword
    );
  }

}
