import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpServiceAbstract } from '../../../base/http-service-abstract';
import { Airline } from './../models/ariline.model';
import { environment } from '../../../../../environments/environment';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AirlineService  {

  constructor(private http: HttpClient) {
  }

  public validateAirlinet(isRefund: boolean, agentCode: string, isoCountryCode: string): Observable<Airline> {
    const pathUrl = isRefund ? environment.api.refund.airline : environment.api.adm_acm.airline;
    return this.http.get<Airline>(environment.basePath + pathUrl + agentCode  + '/' +  isoCountryCode);
  }

}
