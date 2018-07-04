import { environment } from './../../../environments/environment';
import { HttpClient, HttpParams, HttpResponse, HttpHeaders } from '@angular/common/http';
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

  get(params?: HttpParams, search?: Array<URLSearchParams>): Observable<T> {
    return this.httpService.get<T>(this.url, {params: params});
  }

  getSingle<J>(params?: HttpParams): Observable<J> {
    return this.httpService.get<J>(this.url, {params: params});
  }

  getXML() {
    return this.httpService.get(this.url, {responseType: 'text'});
  }

  post<K>(body: K, params?: Array<URLSearchParams>, search?: Array<URLSearchParams>): Observable<T> {
    return this.httpService.post<T>(this.url, JSON.stringify(body), { headers: { 'Content-Type': 'application/json' } });
  }

  postSingle<K>(body, headers?: HttpHeaders): Observable<K> {
    return this.httpService.post<K>(this.url, body, {headers: headers});
  }

  postXML<K>(body: K, params?: Array<URLSearchParams>, search?: Array<URLSearchParams>) {
    return this.httpService.post(this.url, body);
  }

  /*postXML<J = K>(body: K, params?: Array<URLSearchParams>, search?: Array<URLSearchParams>): Observable<T> {
    return this.httpService.post<T>(this.url, JSON.stringify(body)/* ,
      {
        headers: new HttpHeaders()
        .set('Content-Type', 'text/xml')
        //.append('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,PATCH,OPTIONS')
        //.append('Access-Control-Allow-Origin', '*')
        //.append('Access-Control-Allow-Headers', "Access-Control-Allow-Headers, Access-Control-Request-Method")
        } );
  }*/

  delete<K>(params?: HttpParams): Observable<T> {
    return this.httpService.delete<T>(this.url, {params});
  }

  put<K>(body?: K, params?: Array<any>, search?: Array<any>): Observable<T> {
    return this.httpService.put<T>(this.url, body);
  }
  
  configureUrl(url: string): void {
    this.url = url;
  }

  getUrl(pathVariables?: string[]) {
    let urlEnd: string = this.url;
    if (environment.mock) {
      return this.url;
    }
    if (pathVariables) {
        pathVariables.forEach((pathv) => urlEnd += '/' + pathv);
    }
    return urlEnd;
  }

}
