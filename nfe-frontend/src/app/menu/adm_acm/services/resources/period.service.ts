import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { BehaviorSubject } from 'rxjs';
import { AlertsService } from '../../../../core/services/alerts.service';
import { AlertType } from '../../../../core/models/alert-type.enum';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class PeriodService extends NwRepositoryAbstract<any, Object> {

  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.period;
  private $period = new BehaviorSubject<any>(this.getPeriodEmpty());

  constructor(private http: HttpClient, injector: Injector, private _AlertsService: AlertsService) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
        environment.adm_acm.api.period,
      injector
    );
  }

  public getPeriodWithISO(iso: string): void {
    this.configureUrl(this.getUrl([iso]));
    this.get()
      .finally(() => this.configureUrl(this.base))
      .subscribe(
        data => this.$period.next(data)
        , err => {
          this.$period.next([]);
          this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.PERIOD.title', 'ADM_ACM.SVCS.PERIOD.desc', AlertType.ERROR);
        });
  }

  public getPeriod(): Observable<number[]> {
    return this.$period.asObservable();
  }

  private getPeriodEmpty () {
    const variable: any = {};
    variable.values = [];
    return variable;
  }

}
