import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { SftpAccount } from '../models/sftp-account';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class SftpAccountsService {

  private serviceUrl: string;

  constructor(
    private httpClient: HttpClient
  ) {
    this.serviceUrl = 
      environment.accounts.basePath +
      environment.accounts.api.accounts;
  }

  public accounts(): Observable<SftpAccount[]>{
    return this.httpClient.get<SftpAccount[]>(this.serviceUrl);
  }

  public account(login: string): Observable<SftpAccount> {
    const url = `${this.serviceUrl}/${login}`;
    return this.httpClient.get<SftpAccount>(url);
  }

  public createAccount(sftpAccount: SftpAccount) {
    return this.httpClient.post<SftpAccount>(this.serviceUrl, sftpAccount);
  }

  public deleteAccount(login: string) {
    const url = `${this.serviceUrl}/${login}`;
    return this.httpClient.delete(url);
  }

  public putAccount(sftpAccount: SftpAccount) {
    const url = `${this.serviceUrl}/${sftpAccount.login}`;
    return this.httpClient.put(url, sftpAccount);
  }
}
