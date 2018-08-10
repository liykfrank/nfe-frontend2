import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../../environments/environment';
import { HttpServiceAbstract } from '../../../../shared/base/http-service-abstract';
import { IFileDeleted } from '../../models/contract/delete-files.model';

@Injectable()
export class RemoveFilesResource extends HttpServiceAbstract<Array<IFileDeleted>, Object> {
  constructor(private http: HttpClient) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.removeFiles
    );
  }
}
