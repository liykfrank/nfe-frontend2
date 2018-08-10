import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../../environments/environment';
import { HttpServiceAbstract } from '../../../../shared/base/http-service-abstract';

@Injectable()
export class RemoveFileResource extends HttpServiceAbstract<any, Object> {
  constructor(private http: HttpClient) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.removeFile
    );
  }

  deleteFile<T>(idFile: string): Observable<T> {
    return this.httpService.delete<T>(this.getUrl([idFile]));
  }
}
