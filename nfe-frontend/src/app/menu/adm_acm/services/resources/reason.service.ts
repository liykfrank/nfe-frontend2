import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { Reason } from '../../models/reason.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';

@Injectable()
export class ReasonService extends NwRepositoryAbstract<Reason[], Object> {

  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
      environment.adm_acm.api.reasons,
      injector
    );
  }

}
