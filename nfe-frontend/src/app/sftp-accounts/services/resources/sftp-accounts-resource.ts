import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';

import { SftpAccount } from '../../models/sftp-account.model';
import { SftpAccountsResourceGen } from './sftp-accounts-resource-gen';

@Injectable()
export class SftpAccountsResource extends SftpAccountsResourceGen<SftpAccount[]> {
  constructor(private http: HttpClient, injector: Injector){
    super(http, injector);
  }
}
