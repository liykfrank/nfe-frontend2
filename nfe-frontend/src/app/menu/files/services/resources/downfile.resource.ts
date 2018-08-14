import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../../environments/environment';
import { HttpServiceAbstract } from '../../../../shared/base/http-service-abstract';

@Injectable()
export class DownFileResource extends HttpServiceAbstract<any, Object> {
  constructor(private http: HttpClient) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.downloadFile
    );
  }

  public getFile(params?: HttpParams, idFile?: string): Observable<Blob> {
    return this.httpService
      .get(this.getUrl([idFile]), {
        params: params,
        observe: 'response',
        responseType: 'blob'
      })
      .map(resp => {
        return resp.body;
      });
  }
}
