import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';

import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { environment } from './../environments/environment';
import { AlertType } from './core/enums/alert-type.enum';
import { AlertsService } from './core/services/alerts.service';

@Injectable()
export class BsplInterceptor implements HttpInterceptor {
    // pepe12
  private jwtToken: string = environment.token;

  private countHttpPending = 0;

  constructor(private _alertsService: AlertsService) {}


  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    request = this._setTokenOnHeader(request);

    this.countHttpPending++;

    if (this.countHttpPending > 0) {
      // show spinner commit
    }

    return next.handle(request).do(
      (event: HttpEvent<any>) => {
        if (event instanceof HttpResponse) {
          this._checkRemoveSpinner();
        }
      },
      (error: any) => {
        if (error instanceof HttpErrorResponse) {
          // this._checkRemoveSpinner();
          if (error.status === 404 || error.status == 500) {
            this._alertsService.setAlertTranslate(
              'ERROR404.title',
              'ERROR404.desc',
              AlertType.ERROR
            );
          }
        }
      }
    );
  }

  private _checkRemoveSpinner() {
    this.countHttpPending--;
    if (this.countHttpPending == 0) {
      // hideSpinner;
    }
  }

  private _setTokenOnHeader(req: HttpRequest<any>): HttpRequest<any> {
    return req.clone({
      setHeaders: {
        Authorization: `Bearer ${this.jwtToken}`
      }
    });
  }
}
