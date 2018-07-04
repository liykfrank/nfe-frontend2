import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';

import { environment } from '../../../../../environments/environment';

import { Configuration } from '../../models/configuration.model';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs/Observable';
import { AlertsService } from '../../../../core/services/alerts.service';
import { AlertType } from '../../../../core/models/alert-type.enum';

@Injectable()
export class ConfigurationService extends NwRepositoryAbstract<Configuration, Object> {

  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.configuration;

  private $configuration = new BehaviorSubject<Configuration>(this.defaultConf());

  constructor(private http: HttpClient, injector: Injector, private _AlertsService: AlertsService) {
    super(
      http,
      environment.basePath +
        environment.adm_acm.basePath +
        environment.adm_acm.api.configuration,
      injector
    );
  }

  public getWithISO(iso: string) {
    this.configureUrl(this.getUrl([iso]));
    this.get()
      .finally(() => this.configureUrl(this.base))
      .subscribe(
        data => this.$configuration.next(data)
        , err => {
          this.$configuration.next(new Configuration());
          this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.CONF.title', 'ADM_ACM.SVCS.CONF.desc', AlertType.ERROR);
        });
  }

  public getConfiguration(): Observable<Configuration> {
    return this.$configuration.asObservable();
  }

  private defaultConf(): Configuration {
    const conf = new Configuration();

    conf.agentVatNumberEnabled = false;
    conf.airlineVatNumberEnabled = false;
    conf.companyRegistrationNumberEnabled = false;
    conf.cpPermittedForConcerningIssue = false;
    conf.cpPermittedForConcerningRefund = false;
    conf.defaultStat = '';
    conf.freeStat = true;
    conf.isoCountryCode = '';
    conf.maxNumberOfRelatedDocuments = -1;
    conf.mfPermittedForConcerningIssue = false;
    conf.mfPermittedForConcerningRefund = false;
    conf.nridAndSpamEnabled = false;
    conf.taxOnCommissionEnabled = false;
    conf.taxOnCommissionSign = -1;

    return conf;
  }

}
