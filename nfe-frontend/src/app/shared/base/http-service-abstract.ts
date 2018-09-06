import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { environment } from './../../../environments/environment';

export abstract class HttpServiceAbstract<T, K>  {

  constructor(protected httpService: HttpClient, protected url: string) {
  }

  get(params?: HttpParams, addUrl?: string[]): Observable<T> {
    const url = addUrl ? this.url.concat('/' + addUrl.join('/')) : this.url;
    return this.httpService.get<T>(url, {params: params});
  }

  getSingle<J>(params?: HttpParams): Observable<J> {
    return this.httpService.get<J>(this.url, {params: params});
  }

  getXML() {
    return this.httpService.get(this.url, {responseType: 'text'});
  }

  post(body: K, params?: Array<URLSearchParams>, addUrl?: string[]): Observable<T> {
    const url = addUrl ? this.url.concat('/' + addUrl.join('/')) : this.url;
    return this.httpService.post<T>(this.url, JSON.stringify(body), { headers: { 'Content-Type': 'application/json' } });
  }

  // tslint:disable-next-line:no-shadowed-variable
  postSingle<K>(body, headers?: HttpHeaders): Observable<K> {
    return this.httpService.post<K>(this.url, body, {headers: headers});
  }

  postXML(body: K, params?: Array<URLSearchParams>, search?: Array<URLSearchParams>) {
    return this.httpService.post(this.url, body);
  }

  put(body?: K, params?: Array<any>, search?: Array<any>): Observable<T> {
    return this.httpService.put<T>(this.url, body);
  }

  delete(params?: HttpParams): Observable<T> {
    return this.httpService.delete<T>(this.url, {params});
  }

  getFile(params?: HttpParams): Observable<Blob> {
    return this.httpService
      .get(this.url, {
        params: params,
        responseType: 'blob'
      });
  }

  configureUrl(url: string): void {
    this.url = url;
  }

  getUrl(pathVariables?: string[]) {
    let urlEnd: string = this.url;
    // if (environment.mock) {
    //   return this.url;
    // }
    if (pathVariables) {
        pathVariables.forEach((pathv) => urlEnd += '/' + pathv);
    }
    return urlEnd;
  }

}
