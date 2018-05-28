import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { NwBaseAbstract } from '../../shared/base/nw-base-abstract';
import { SftpAccount } from '../models/sftp-account.model';
import { SftpAccountPassword, SftpAccountPasswordIf } from './../models/sftp-account-password.model';
import { SftpAccountResource } from './resources/sftp-account-resource';
import { SftpAccountsPasswordResource } from './resources/sftp-accounts-password-resource';
import { SftpAccountsResource } from './resources/sftp-accounts-resource';

@Injectable()
export class SftpAccountsService extends NwBaseAbstract {
  constructor(
    injector: Injector,
    private sftpAccountResource: SftpAccountResource,
    private sftpAccountsResource: SftpAccountsResource,
    private sftpAccountsPasswordResource: SftpAccountsPasswordResource
  ) {
    super(injector);
  }

  public accounts(): Observable<SftpAccount[]>{
    return this.sftpAccountsResource.get();
  }

  public deleteAccount(login: string): Observable<any> {
    return this.sftpAccountResource.deleteById(login);
  }

  public modifyAccount(sftpAccount: SftpAccount): Observable<any>{
    return this.sftpAccountResource.putById(sftpAccount);
  }

  public createAccount(sftpAccount: SftpAccount): Observable<any>{
    return this.sftpAccountResource.post<SftpAccount>(sftpAccount);
  }

  public changePassword(sftpAccount: SftpAccount, SftpAccountsPassword: SftpAccountPasswordIf): Observable<any>{
    return this.sftpAccountsPasswordResource.putPassword<SftpAccountPasswordIf>(sftpAccount, SftpAccountsPassword);
  }

}
