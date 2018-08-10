import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { EnvironmentType } from '../../../enums/environment-type.enum';
import { Reason } from '../models/reason.model';
import { environment } from './../../../../../environments/environment';

@Injectable()
export class ReasonsService {
  constructor(private http: HttpClient) {}

  getWithParams(iso: string, type: EnvironmentType): Observable<Reason[]> {
    let urlSvc = '';
    const params: HttpParams = new HttpParams();
    params.append('isoCountryCode', iso);

    switch (type) {
      case EnvironmentType.REFUND_INDIRECT:
        urlSvc = environment.api.refund.reasons_indirects;
        params.append('refundType', 'REASON_FOR_ISSUANCE');
        break;
      case EnvironmentType.ACDM:
        urlSvc = environment.api.adm_acm.reasons;
        break;
    }

    return this.http.get<Reason[]>(environment.basePath + urlSvc, {
      params: params
    });
  }
}
