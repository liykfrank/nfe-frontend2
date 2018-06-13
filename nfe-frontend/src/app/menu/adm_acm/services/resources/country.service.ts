import { Injectable, Injector } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';

import { environment } from '../../../../../environments/environment';
import { Country } from '../../models/country.model';

@Injectable()
export class CountryService extends NwRepositoryAbstract<Country[], Object> {

  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.adm_acm.basePath +
        environment.adm_acm.api.country,
      injector
    );
  }
}
