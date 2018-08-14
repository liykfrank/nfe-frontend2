import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Reason } from '../models/reason.model';
import { environment } from './../../../../../environments/environment';

@Injectable()
export class ReasonsService {
  constructor(private http: HttpClient) {}

  getReasonsByIndirectRefund(iso: string): Observable<Reason[]> {

    const params: HttpParams = new HttpParams();
    params.append('isoCountryCode', iso);
    params.append('refundType', 'REASON_FOR_ISSUANCE');
    return this.http.get<Reason[]>(environment.basePath + environment.api.refund.reasons_indirects, {params: params});
  }


  getReasonsByAdcms(iso: string): Observable<Reason[]> {

    const params: HttpParams = new HttpParams();
    params.append('isoCountryCode', iso);
    return this.http.get<Reason[]>(environment.basePath + environment.api.adm_acm.reasons, {params: params});

  }
}
