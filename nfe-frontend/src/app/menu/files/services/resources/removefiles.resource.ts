import { IFileDeleted } from '../../models/contract/delete-files.model';
import { HttpClient } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { IListFiles } from '../../models/contract/list-files';

@Injectable()
export class RemoveFilesResource extends NwRepositoryAbstract<Array<IFileDeleted>, Object> {
  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.removeFiles,
      injector
    );
  }


}
