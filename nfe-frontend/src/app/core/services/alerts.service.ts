import { Injectable, Injector } from '@angular/core';
import { AlertModel } from './../models/alert.model';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { AlertType } from '../models/alert-type.enum';
import { NwBaseAbstract } from '../../shared/base/nw-base-abstract';

@Injectable()
export class AlertsService extends NwBaseAbstract {

  private alert = new BehaviorSubject<AlertModel>(null);
  private onAccept = new BehaviorSubject<boolean>(null);

  constructor (injector: Injector) {
    super(injector);
  }

  public getAlert(): Observable<AlertModel> {
    return this.alert.asObservable();
  }
  
  public setAlert(elem: AlertModel): void {
    this.alert.next(elem);
  }

  public getAccept(): Observable<boolean> {
    return this.onAccept.asObservable();
  }

  public setAccept(value: boolean) {
    this.onAccept.next(value);
  }

  public setAlertTranslate(title: string, message: string, alert_type?: AlertType) {
    const alert: AlertModel = new AlertModel(this.translation.translate(title)
                                              , this.translation.translate(message)
                                              , alert_type);
    this.setAlert(alert);
  }

}
