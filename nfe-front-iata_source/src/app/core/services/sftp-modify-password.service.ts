import { Injectable, Injector } from '@angular/core';
import { HttpServiceAbstract } from '../../shared/base/http-service-abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class SftpModifyPasswordService extends HttpServiceAbstract<string, any> {

  constructor(private _HttpClient: HttpClient) {
    super(_HttpClient, environment.sftAccount.basePath + environment.sftAccount.api.modify);
  }

}
