import { Logger } from 'ng2-logger/src/logger';
import { Injectable, Injector } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Configuration } from '../models/configuration';
import { Observable } from 'rxjs/Observable';
import { NwRepositoryAbstract } from '../../shared/base/nwe-repository.abstract';

@Injectable()
export class ConfigurationService extends NwRepositoryAbstract<any, Configuration> {

  constructor(private http: HttpClient, protected injector: Injector) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.apiConfiguration,
      injector
    );
  }

}
