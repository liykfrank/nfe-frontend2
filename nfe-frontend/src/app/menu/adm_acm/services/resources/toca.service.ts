
import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { TocaType } from '../../models/toca-type.model';

@Injectable()
export class TocaService extends NwRepositoryAbstract<TocaType[], Object> {

  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.adm_acm.basePath +
        environment.adm_acm.api.toca,
      injector
    );
  }
}
