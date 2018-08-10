import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../environments/environment';
import { HttpServiceAbstract } from '../../../shared/base/http-service-abstract';
import { TocaType } from '../models/toca-type.model';

@Injectable()
export class TocaService extends HttpServiceAbstract<TocaType[], Object> {
  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.adm_acm.toca);
  }

  public getTocaWithISO(iso: string): Observable<TocaType[]> {
    return this.get(null, [iso]);
  }
}
