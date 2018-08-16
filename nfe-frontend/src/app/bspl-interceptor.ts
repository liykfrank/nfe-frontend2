import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { AlertsService } from './core/services/alerts.service';
import { AlertType } from './core/enums/alert-type.enum';


@Injectable()
export class BsplInterceptor implements HttpInterceptor {

    private countHttpPending = 0;

     constructor(private _alertsService: AlertsService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        this.countHttpPending++;

        if ( this.countHttpPending > 0) {
            // show spinner commit
        }
        return next.handle(request).do((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
                this.checkRemoveSpinner();
            }
          }, (error: any) => {
            if (error instanceof HttpErrorResponse) {
               // this.checkRemoveSpinner();
               if(error.status === 404){
                this._alertsService.setAlertTranslate('ERROR404.title', 'ERROR404.desc', AlertType.ERROR);
               }
            }
          });
    }

    checkRemoveSpinner() {
        this.countHttpPending--;
        if (this.countHttpPending == 0) {
            // hideSpinner;
        }
    }


}
