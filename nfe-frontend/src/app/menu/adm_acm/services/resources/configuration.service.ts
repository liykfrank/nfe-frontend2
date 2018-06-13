import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';

import { environment } from '../../../../../environments/environment';

import { Configuration } from '../../models/configuration.model';

@Injectable()
export class ConfigurationService extends NwRepositoryAbstract<Configuration, Object> {

  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
        environment.adm_acm.basePath +
        environment.adm_acm.api.configuration,
      injector
    );
  }

  public defaultConf(): Configuration {
    const conf = new Configuration();

    conf.agentVatNumberEnabled = false;
    conf.airlineVatNumberEnabled = false;
    conf.companyRegistrationNumberEnabled = false;
    conf.cpPermittedForConcerningIssue = false;
    conf.cpPermittedForConcerningRefund = false;
    conf.defaultStat = '';
    conf.freeStat = false;
    conf.isoc = '';
    conf.maxNumberOfRelatedDocuments = -1;
    conf.mfPermittedForConcerningIssue = false;
    conf.mfPermittedForConcerningRefund = false;
    conf.nridAndSpamEnabled = false;
    conf.taxOnCommissionEnabled = false;
    conf.taxOnCommissionSign = -1;

    return conf;
  }

}
