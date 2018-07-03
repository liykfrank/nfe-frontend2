import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { DownFilesResource } from '../downfiles.resource';
import { of } from 'rxjs/observable/of';

@Injectable()
export class DownFileMockResource extends DownFilesResource {
  constructor( http: HttpClient, injector: Injector) {
    super(
      http,
      injector
    );
  }

  public getFile(params?: HttpParams, idFile?:string): Observable<Blob> {
    this.log.info('get file mockkkkkkk');
    const blob=new Blob(['foo', 'bar'],{type:'text/xml'});
    return of(blob);
  }

}
