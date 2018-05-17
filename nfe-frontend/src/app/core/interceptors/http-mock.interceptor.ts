import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Log } from 'ng2-logger';
import { Logger } from 'ng2-logger/src/logger';
import { Observable } from 'rxjs/Observable';
import { NwBaseAbstract } from '../../shared/base/nw-base-abstract';

const log: Logger<HTTPMock> = Log.create('HTTPMock');

@Injectable()
export class HTTPMock extends NwBaseAbstract  implements HttpInterceptor {

  constructor(injector : Injector ) {
    super(injector);
   }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    if (req.method === 'POST' && req.url.endsWith('json')) {
      const changedReq = req.clone({ method: 'GET' });
      log.info('intercept', req, changedReq);

      return next.handle(changedReq);
    }

    return next.handle(req);
  }
}
// tslint:disable-next-line:variable-name
export const HTTPMockInterceptorProvider = {
  multi: true,
  provide: HTTP_INTERCEPTORS,
  useClass: HTTPMock
};
