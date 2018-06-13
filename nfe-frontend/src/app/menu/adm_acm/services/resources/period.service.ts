import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';

@Injectable()
export class PeriodService extends NwRepositoryAbstract<number[], Object> {

  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.adm_acm.basePath +
        environment.adm_acm.api.period,
      injector
    );
  }

}
