import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Acdm } from '../models/acdm.model';

@Injectable()
export class AcdmsService extends HttpServiceAbstract<Acdm[], Object> {
  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.adm_acm.acdm);
  }

  public postAcdm(acdm: Acdm): Observable<Acdm> {
    const header = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.postSingle<Acdm>(JSON.stringify(acdm), header);
  }
}
