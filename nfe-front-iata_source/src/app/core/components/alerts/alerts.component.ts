import { Component, OnInit } from '@angular/core';

import { AlertModel } from './../../models/alert.model';
import { AlertsService } from './../../services/alerts.service';

@Component({
  selector: 'bspl-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss']
})
export class AlertsComponent implements OnInit {
  alert: AlertModel;
  display = false;
  style_class = '';
  image_type: string;

  private show_accept_button: boolean;
  private show_cancel_button: boolean;

  constructor(private _AlertsService: AlertsService) {}

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
