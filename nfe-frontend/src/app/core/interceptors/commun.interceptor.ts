import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Log } from 'ng2-logger';
import { Logger } from 'ng2-logger/src/logger';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../environments/environment';
import { NwBaseAbstract } from '../../shared/base/nw-base-abstract';

const log: Logger<CommunInterceptor> = Log.create('CommunInterceptor');

@Injectable()
export class CommunInterceptor extends NwBaseAbstract implements HttpInterceptor {

  constructor(injector : Injector ) {
    super(injector);
   }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    //const cookieLang: string = this.cookieService.get('cookieLang');
    //const lang = cookieLang ? cookieLang : 'es_ES';

   /*  if (req.url && req.url.startsWith(environment.basePath)) {

      const headers = req.headers;
      headers.append('x-user-language', lang);
      headers.append('x-frame-channel', 'EMP');
      headers.append('x-alias', 'CRIPTOPE');
      log.info('headers', headers);

      const changedReq = req.clone({ headers });
      log.info('intercept', req, changedReq);

      return next.handle(changedReq);
    } */

    return next.handle(req);
  }
}

export const communInterceptorProvider = {
  multi: true,
  provide: HTTP_INTERCEPTORS,
  useClass: CommunInterceptor
};
