import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../../../../environments/environment';
import { HttpServiceAbstract } from '../../../../shared/base/http-service-abstract';

@Injectable()
export class DownFilesResource extends HttpServiceAbstract<any, Object> {
  constructor(http: HttpClient) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.downloadFiles
    );
  }
}
