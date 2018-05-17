import { environment } from './../../../environments/environment';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Type, Injector } from '@angular/core';
import 'rxjs/add/operator/map';
import { NwBaseAbstract } from './nw-base-abstract';

export abstract class NwRepositoryAbstract<T, K> extends NwBaseAbstract {

  constructor(protected httpService: HttpClient, protected url: string,
    injector: Injector) {
    super(injector);
  }

  public getFile(params?: HttpParams): Observable<Blob> {
    return this.httpService
      .get(this.url, {
        params: params,
        responseType: "blob"
      });
  }

  /*public postFile(params?: HttpParams): Observable<Blob> {
    return this.httpService
      .post(this.url, {
        params: params,
        responseType: "json"
      });
  }*/

  get<J = K>(params?: HttpParams, search?: Array<URLSearchParams>): Observable<T> {
    return this.httpService.get<T>(this.url,{params:params});
  }

  post<J = K>(body: K, params?: Array<URLSearchParams>, search?: Array<URLSearchParams>): Observable<T> {
    return this.httpService.post<T>(this.url, JSON.stringify(body), { headers: { 'Content-Type': 'application/json' } });
  }

  delete<J = K>(params?: HttpParams): Observable<T> {
    return this.httpService.delete<T>(this.url,{params});
  }

  put<J = K>(body?: K, params?: Array<any>, search?: Array<any>): Observable<T> {
    return this.httpService.put<T>(this.url, body);
  }
  configureUrl(url: string): void {
    this.url = url;
  }
  getUrl(pathVariables?:string[]){
    console.log('enter get uel' );
    let urlEnd:string=this.url;
    if(environment.mock){
      return this.url;
    }
    if(pathVariables){
        urlEnd+='/';
        pathVariables.forEach((pathv) => urlEnd+=pathv);
    }
    return urlEnd;
  }

}
