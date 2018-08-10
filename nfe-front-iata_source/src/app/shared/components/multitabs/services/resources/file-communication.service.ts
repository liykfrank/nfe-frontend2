import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { HttpServiceAbstract } from '../../../../base/http-service-abstract';

@Injectable()
export class FileCommunicationService extends HttpServiceAbstract<any, Object> {

  constructor(private http: HttpClient) {
    super(http, '');
  }

}
