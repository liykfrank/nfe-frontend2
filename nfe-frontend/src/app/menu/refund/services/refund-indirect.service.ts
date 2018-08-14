import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Refund } from '../models/api/refund.model';

@Injectable()
export class RefundIndirectService extends HttpServiceAbstract<Refund, Object> {
  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.refund.refund_indirect);
  }
}
