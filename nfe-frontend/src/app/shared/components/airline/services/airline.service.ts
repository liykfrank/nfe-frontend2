import { EnvironmentType } from './../../../enums/environment-type.enum';
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

  public validateAirlinet(type: EnvironmentType, airlineCode: string, isoCountryCode: string): Observable<Airline> {
    let pathUrl;

    switch (type) {
      case EnvironmentType.REFUND_INDIRECT:
        pathUrl = environment.api.refund.airline;
        break;
      case EnvironmentType.ACDM:
        pathUrl = environment.api.adm_acm.airline;
        break;
      case EnvironmentType.MASTER_AGENT:
        // TODO: HACER EL MOCK
        break;
    }

    return this.http.get<Airline>(environment.basePath + pathUrl + airlineCode  + '/' +  isoCountryCode);
  }

}
