import { Injectable } from '@angular/core';
import { TranslationService } from 'angular-l10n';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

import { AlertType } from '../enums/alert-type.enum';
import { AlertModel } from './../models/alert.model';

@Injectable()
export class AlertsService {
  private alert = new BehaviorSubject<AlertModel>(null);
  private onAccept = new Subject<boolean>();

  constructor(private _translationService: TranslationService) {}

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

  public setAlertTranslate(
    title: string,
    message: string,
    alert_type?: AlertType
  ) {
    const alert: AlertModel = new AlertModel(
      this._translationService.translate(title),
      this._translationService.translate(message),
      alert_type
    );
    
    this.setAlert(alert);
  }
}
