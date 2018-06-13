import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../shared/base/nwe-repository.abstract';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class SftpModifyPasswordService extends NwRepositoryAbstract<string, any> {

  constructor(private _HttpClient: HttpClient, private _Injector: Injector) {
    super(_HttpClient,
      environment.sftAccount.basePath + environment.sftAccount.api.modify,
      _Injector);
  }

}
