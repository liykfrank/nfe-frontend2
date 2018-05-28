import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { NwRepositoryAbstract } from '../../../shared/base/nwe-repository.abstract';
import { IListFiles } from '../../models/contract/list-files';

@Injectable()
export class ListFilesResource extends NwRepositoryAbstract<IListFiles, Object> {
  constructor( http: HttpClient, injector: Injector) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.listFiles,
      injector
    );
  }
}
