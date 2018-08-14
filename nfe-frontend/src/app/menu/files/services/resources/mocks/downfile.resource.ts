import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';

import { DownFilesResource } from '../downfiles.resource';

@Injectable()
export class DownFileMockResource extends DownFilesResource {
  constructor(http: HttpClient) {
    super(http);
  }

  public getFile(params?: HttpParams, idFile?: string): Observable<Blob> {
    const blob = new Blob(['foo', 'bar'], {type: 'text/xml'});
    return of(blob);
  }
}
