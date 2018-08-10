import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { AdmAcmConfiguration } from '../models/adm-acm-configuration.model';

@Injectable()
export class AcdmConfigurationService extends HttpServiceAbstract<AdmAcmConfiguration, Object> {
  private $acdmConfiguration = new BehaviorSubject<AdmAcmConfiguration>(new AdmAcmConfiguration());

  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.adm_acm.configuration);
  }

  public getConfiguration(): Observable<AdmAcmConfiguration> {
    return this.$acdmConfiguration.asObservable();
  }

  public getConfigurationByISO(iso: string) {
    if (iso != this.$acdmConfiguration.getValue().isoCountryCode) {
      this.get(null, [iso]).subscribe(data =>
        this.$acdmConfiguration.next(data)
      );
    }
  }
}
