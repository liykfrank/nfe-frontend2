import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Acdm } from '../models/acdm.model';
import { environment } from '../../../../environments/environment';

@Injectable()
export class AcdmsService extends HttpServiceAbstract<Acdm[], Object> {

  constructor(private http: HttpClient) {
    super(
      http,
      environment.basePath +
        environment.adm_acm.basePath +
        environment.adm_acm.api.acdm
    );
  }

  public postAcdm(acdm: Acdm): Observable<Acdm> {
    const header = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.postSingle<Acdm>(JSON.stringify(acdm), header);
  }

}
