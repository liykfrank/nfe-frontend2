import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../../environments/environment';
import { HttpServiceAbstract } from '../../../../shared/base/http-service-abstract';
import { IListFiles } from '../../models/contract/list-files';

@Injectable()
export class ListFilesResource extends HttpServiceAbstract<IListFiles, Object> {
  constructor(http: HttpClient) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.listFiles
    );
  }
}
