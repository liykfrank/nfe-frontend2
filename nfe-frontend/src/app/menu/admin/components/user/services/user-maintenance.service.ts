import { HttpClient } from '@angular/common/http';
import { UserInterface, User } from './../models/api/user.model';
import { Injectable } from '@angular/core';
import { HttpServiceAbstract } from '../../../../../shared/base/http-service-abstract';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../../../../environments/environment';

@Injectable()
export class UserMaintenanceService extends HttpServiceAbstract<UserInterface, Object> {

  constructor(private http: HttpClient) {
    super(http, environment.basePath + environment.api.user.createUser);
  }

  createUser(user: User): Observable<UserInterface> {
    return this.post(user).map(res => {
      return res;
    });
  }
}
