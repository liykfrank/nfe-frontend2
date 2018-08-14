import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Country } from '../models/country.model';

@Injectable()
export class CountryService extends HttpServiceAbstract<Country[], Object> {
  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.adm_acm.country);
  }
}
