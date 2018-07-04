import { Currency } from './../../models/currency.model';
import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { AlertType } from '../../../../core/models/alert-type.enum';
import { AlertsService } from '../../../../core/services/alerts.service';

@Injectable()
export class CurrencyService extends NwRepositoryAbstract<Currency[], Object> {

  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.currency;
  private $currency = new BehaviorSubject<Currency[]>([]);

  constructor(private http: HttpClient, injector: Injector, private _AlertsService: AlertsService) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
        environment.adm_acm.api.currency,
      injector
    );
  }

  public getCurrencyWithISO(iso: string): void {
    this.configureUrl(this.getUrl([iso]));
    this.get()
      .finally(() => this.configureUrl(this.base))
      .subscribe(
        data => this.$currency.next(data)
        , err => {
          this.$currency.next([]);
          this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.TOCA.title', 'ADM_ACM.SVCS.TOCA.desc', AlertType.ERROR);
        });
  }

  public getCurrency(): Observable<Currency[]> {
    return this.$currency.asObservable();
  }

}
