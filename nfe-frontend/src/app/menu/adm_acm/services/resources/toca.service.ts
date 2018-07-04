import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs';

import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { TocaType } from '../../models/toca-type.model';
import { AlertsService } from '../../../../core/services/alerts.service';
import { AlertType } from '../../../../core/models/alert-type.enum';

@Injectable()
export class TocaService extends NwRepositoryAbstract<TocaType[], Object> {

  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.toca;
  private $toca = new BehaviorSubject<TocaType[]>([]);

  constructor(private http: HttpClient, injector: Injector, private _AlertsService: AlertsService) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
        environment.adm_acm.api.toca,
      injector
    );
  }

  public getTocaWithISO(iso: string): void {
    this.configureUrl(this.getUrl([iso]));
    this.get()
      .finally(() => this.configureUrl(this.base))
      .subscribe(
        data => {
          this.$toca.next(data);
        }, err => {
          this.$toca.next([]);
          this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.TOCA.title', 'ADM_ACM.SVCS.TOCA.desc', AlertType.ERROR);
        });
  }

  public getToca(): Observable<TocaType[]> {
    return this.$toca.asObservable();
  }

}
