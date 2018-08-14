import { environment } from './../../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { EnvironmentType } from './../../../enums/environment-type.enum';
import { CurrencyGet } from './../models/currency-get.model.';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Currency } from '../models/currency.model';


@Injectable()
export class CurrencyService {

  private $currencyState: BehaviorSubject<Currency> = new BehaviorSubject<Currency>(new Currency);

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

  setCurrencyState(currency: Currency) {
    this.$currencyState.next(currency);
}

  getCurrencyState(): Observable<Currency> {
    return this.$currencyState.asObservable();
  }

}
