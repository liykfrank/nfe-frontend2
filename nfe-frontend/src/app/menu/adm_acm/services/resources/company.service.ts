import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { CompanyModel } from '../../models/company.model';
import { environment } from '../../../../../environments/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { AlertsService } from '../../../../core/services/alerts.service';
import { AlertModel } from '../../../../core/models/alert.model';
import { AlertType } from '../../../../core/models/alert-type.enum';

@Injectable()
export class CompanyService extends NwRepositoryAbstract<CompanyModel, Object> {

  private $airlineCountryAirlineCode = new BehaviorSubject<CompanyModel>(null);
  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.company;

  constructor(private http: HttpClient, injector: Injector, private _AlertsService: AlertsService ) {
    super(http
      , environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.company
      , injector);
  }

  public getAirlineCountryAirlineCode(): Observable<CompanyModel> {
    return this.$airlineCountryAirlineCode.asObservable();
  }

  public getFromServerAirlineCountryAirlineCode(isoCountry: string, airlineCode: string): void {
    this.configureUrl(this.getUrl([isoCountry, airlineCode]));

    this.get()
      .finally(() => this.configureUrl(this.base))
      .subscribe(resp => {
        this.$airlineCountryAirlineCode.next(resp);
      }, err => {
        this.$airlineCountryAirlineCode.next(new CompanyModel());

        this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.COMPANY.title', 'ADM_ACM.SVCS.COMPANY.desc', AlertType.ERROR);
      });
  }

}
