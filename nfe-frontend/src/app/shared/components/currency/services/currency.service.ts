import { environment } from './../../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { EnvironmentType } from './../../../enums/environment-type.enum';
import { CurrencyGet } from './../models/currency-get.model.';

@Injectable()
export class CurrencyService {
  constructor(private http: HttpClient) {}

  public getWithISO(type: EnvironmentType, iso: string): Observable<CurrencyGet> {

    let url: string = environment.basePath;
    switch (type) {
      case (EnvironmentType.REFUND_INDIRECT):
        url += environment.api.refund.currency;
        break;
      case (EnvironmentType.ACDM):
        url += environment.api.adm_acm.currency;
        break;
    }
    url += iso;
    return this.http.get<CurrencyGet>(url);
  }


}
