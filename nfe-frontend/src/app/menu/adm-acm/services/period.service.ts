import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Period } from '../models/period.model';

@Injectable()
export class PeriodService extends HttpServiceAbstract<Period, Object> {
  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.adm_acm.period);
  }

  public getPeriodWithISO(iso: string): Observable<Period> {
    return this.get(null, [iso]);
  }
}
