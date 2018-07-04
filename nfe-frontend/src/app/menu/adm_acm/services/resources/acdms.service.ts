import { Injectable, Injector } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { environment } from '../../../../../environments/environment';

import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';

import { Acdm } from '../../models/acdm.model';

@Injectable()
export class AcdmsService extends NwRepositoryAbstract<Acdm[], Object>  {

  // TODO: Cambiar el observable al que se añade el elemento.
  private $acdmFromID = new BehaviorSubject<Acdm>(null);
  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.acdm;

  constructor(
    private http: HttpClient,
            injector: Injector
  ) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
      environment.adm_acm.api.acdm,
      injector
    );
  }

  public getAcdm_ID(id: number): void {

    this.configureUrl(this.getUrl([id.toString()]));

    this.getSingle<Acdm>()
      .finally(() => this.configureUrl(this.base))
      .subscribe(resp => {
        this.$acdmFromID.next(resp);
      });
  }

  public postAcdm(acdm: Acdm): Observable<Acdm> {
    const header = new HttpHeaders({
      'Content-Type':  'application/json'
    });
    return this.postSingle<Acdm>(JSON.stringify(acdm), header);
  }

  public getAcdmFromID(): Observable<Acdm> {
    return this.$acdmFromID.asObservable();
  }

}
