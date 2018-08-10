import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { delay } from 'rxjs/operators';

import { ParamsEnum } from '../../../models/params.enum';
import { ListFilesResource } from '../listfiles.resopurce';
import * as dataDown from './listfiles-down.json';
import * as dataEmpty from './listfiles-empty.json';
import * as dataPage1 from './listfiles-page-1.json';
import * as dataPage2 from './listfiles-page-2.json';
import * as data from './listfiles.json';

@Injectable()
export class ListFilesMockResource extends ListFilesResource {
  TIME_DELAY: number = 1000;

  constructor(private http: HttpClient) {
    super(http);
  }

  get(params?: HttpParams, addUrl?: string[]): Observable<any> {
    if (params.get(ParamsEnum.STATUS) == 'DOWNLOADED') {
      return of(dataDown).pipe(delay(this.TIME_DELAY));
    } else if (params.get(ParamsEnum.PAGE) == '1') {
      return of(dataPage1).pipe(delay(this.TIME_DELAY));
    } else if (params.get(ParamsEnum.PAGE) == '2') {
      return of(dataPage2).pipe(delay(this.TIME_DELAY));
    } else if (params.get(ParamsEnum.TYPE) == 'xxx') {
      return of(dataEmpty).pipe(delay(this.TIME_DELAY));
    }

    return of(data).pipe(delay(3000));
  }

}
