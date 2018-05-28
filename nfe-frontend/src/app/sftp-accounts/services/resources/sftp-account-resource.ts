import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { SftpAccount } from '../../models/sftp-account.model';
import { SftpAccountsResourceGen } from './sftp-accounts-resource-gen';



@Injectable()
export class SftpAccountResource extends SftpAccountsResourceGen<SftpAccount> {
  constructor(private http: HttpClient, injector: Injector) {
    super(http, injector);
  }

  public deleteById<T>(login: string): Observable<T> {
    return this.httpService.delete<T>(this.getUrl([login]));
  }

  public putById<T>(sftpAccount: SftpAccount): Observable<T> {
    return this.httpService.put<T>(
      this.getUrl([sftpAccount.login]),
      sftpAccount
    );
  }
}
