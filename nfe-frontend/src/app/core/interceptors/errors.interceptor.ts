import {
  HTTP_INTERCEPTORS,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { tap } from 'rxjs/operators';

import { NwBaseAbstract } from '../../shared/base/nw-base-abstract';

@Injectable()
export class ErrorsInterceptor extends NwBaseAbstract implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(
        tap(
          (next => { this.log.info('event from interceptor', next); }),
          (error => {
            this.log.info('event from interceptor error ', error);
            if (error instanceof HttpErrorResponse) {
              switch (error.status) {
                case 404:
                  this.log.info('error 404', error);
                 /*  this.alertService.addAlert({

                    closable: true,
                    detail: 'alerts.httperror.response404',
                    severity: NweAlertSeverity.Error,
                    type: NweAlertType.Modal

                  }); */
                  break;
                case 400:
                  this.log.info('error 400', error);
                  /* this.alertService.addAlert({

                    closable: true,
                    detail: 'alerts.httperror.response400',
                    severity: NweAlertSeverity.Error,
                    type: NweAlertType.Modal

                  }); */
                  break;
                default:
                  break;
              }
            }
          })
        )
      );
  }
  /* constructor(protected alertService: NweAlertsService) {
    super();

  } */

}

export const errorsInterceptorProvider = {
  multi: true,
  provide: HTTP_INTERCEPTORS,
  useClass: ErrorsInterceptor
};
