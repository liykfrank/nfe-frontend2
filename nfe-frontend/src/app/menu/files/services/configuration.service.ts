import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { Configuration } from '../models/configuration';

@Injectable()
export class ConfigurationService extends HttpServiceAbstract<any, Configuration> {
  constructor(private http: HttpClient) {
    super(
      http,
      environment.basePath +
      environment.api.files.configuration
    );
  }
}
