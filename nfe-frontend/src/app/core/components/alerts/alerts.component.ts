import { Component, OnInit, Output } from '@angular/core';
import { AlertsService } from './../../services/alerts.service';
import { AlertModel } from './../../models/alert.model';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss']
})
export class AlertsComponent implements OnInit {

  private alert: AlertModel;
  display = false;
  private style_class = '';
  private show_accept_button: boolean;
  private show_cancel_button: boolean;
  image_type: string;

  constructor(
    private _AlertsService: AlertsService
  ) { }

  // this._AlertsService.setAlert({
  //   title: 'TITLE DASHBOARD',
  //   message: 'MENSAJE DASHBOARD',
  //   alert_type: 'ALERT'
  // });

  ngOnInit() {
    this._AlertsService.getAlert().subscribe(alert => {
      if (alert) {
        this.alert = alert;

        this.style_class = 'alert-' + alert.alert_type;

        this.show_accept_button = true;
        this.show_cancel_button = false;

        switch (alert.alert_type) {
          case 'error':
            this.image_type = 'error';
            break;
          case 'confirm':
            this.image_type = 'confirm';
            this.show_accept_button = true;
            this.show_cancel_button = true;
            break;
          case 'warning':
            this.image_type = 'warning_exclamation';
            this.show_accept_button = true;
            break;
          case 'info':
            this.image_type = 'success';
            break;
        }

        this.display = true;
      }
    });
  }

  onClickAccept() {
    this._AlertsService.setAccept(true);
    this.display = false;
  }

  onClickCancel() {
    this._AlertsService.setAccept(false);
    this.display = false;
  }

  checkCancelButton() {
    return this.show_cancel_button;
  }

  checkAcceptButton() {
    return this.show_accept_button;
  }
}
