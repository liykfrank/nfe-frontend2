import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { HttpServiceAbstract } from '../../shared/base/http-service-abstract';
import { User } from '../models/user.model';
import { environment } from './../../../environments/environment';

@Injectable()
export class UserService extends HttpServiceAbstract<User, Object> {

  private _user: User;

  constructor(private _http: HttpClient) {
    super(_http, environment.api.user.user);
  }

  initializeUser(): Promise<any> {
    return new Promise((resolve, reject) => {
      this.get().subscribe(data => {
        this._user = data;
        resolve(true);
      });
    });
  }

  getUser(): Observable<User> {
    return Observable.of(this._user);
  }
}
