import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { RefundConfiguration } from '../models/refund-configuration.model';

@Injectable()
export class RefundConfigurationService extends HttpServiceAbstract<RefundConfiguration, Object> {
  private $refundConfiguration: BehaviorSubject<RefundConfiguration>
    = new BehaviorSubject<RefundConfiguration>(new RefundConfiguration());

  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.refund.configurations);
  }

  changeConfigurationByISO(iso: string) {
    this.get(null, [iso]).subscribe(conf =>
      this.$refundConfiguration.next(conf)
    );
  }

  getConfiguration(): Observable<RefundConfiguration> {
    return this.$refundConfiguration.asObservable();
  }
}
