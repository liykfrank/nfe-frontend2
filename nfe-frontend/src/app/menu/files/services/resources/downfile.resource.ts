import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { environment } from '../../../../../environments/environment';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { IListFiles } from '../../models/contract/list-files';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DownFileResource extends NwRepositoryAbstract<any, Object> {
  constructor(private http: HttpClient, injector: Injector) {
    super(
      http,
      environment.basePath +
        environment.files.basePath +
        environment.files.api.downloadFile,
      injector
    );
  }

  public getFile(params?: HttpParams, idFile?:string): Observable<Blob> {
    return this.httpService
      .get(this.getUrl([idFile]), {
        params: params,
        observe: 'response',
        responseType: "blob"
      })
       .map(resp=>{
        this.log.info(resp.headers.get('Content-Disposition'));
        return resp.body;
      });
  }
}
