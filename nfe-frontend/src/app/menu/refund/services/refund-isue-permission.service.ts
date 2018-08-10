import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';

@Injectable()
export class RefundIsuePermissionService extends HttpServiceAbstract<string, Object> {

  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.refund.issuePermission);
  }

  getRefundIssuePermission(iso: string, airlineCode: string, agentCode: string): Observable<string> {
    const validIso = iso && iso.length > 0;
    const validAirlineCode = airlineCode && airlineCode.length > 0;
    const validAgentCode = agentCode && agentCode.length > 0;

    if (validIso && validAirlineCode && validAgentCode) {
      const params: HttpParams = new HttpParams();
      params.append('isoCountryCode', iso);
      params.append('airlineCode', airlineCode);
      params.append('agentCode', agentCode);

      return this.get(params);
    } else {
      throw new Error('invalid params');
    }
  }

}
