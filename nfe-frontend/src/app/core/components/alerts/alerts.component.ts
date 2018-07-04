import { Component, OnInit } from '@angular/core';
import { AlertsService } from './../../services/alerts.service';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss']
})
export class AlertsComponent implements OnInit {

  display = false;
  title: string;
  message: string;
  alert_type: string;
  show_accept_button: boolean;
  show_cancel_button: boolean;
  private style_class = '';
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
        this.title = alert.title;
        this.message = alert.message;
        this.alert_type = alert.alert_type;

        this.style_class = 'alert-' + alert.alert_type;

        switch (alert.alert_type) {
          case 'error':
            this.image_type = 'error';
            break;
          case 'warning':
            this.image_type = 'warning_exclamation';
            break;
          case 'info':
            this.image_type = 'success';
            break;
        }

        this.show_accept_button = true;
        this.show_cancel_button = false;

        this.display = true;
      }
    });
  }

  onClickAccept() {
    this.display = false;
  }

  onClickCancel() {
    this.display = false;
  }

  checkCancelButton() {
    return this.show_cancel_button;
  }

  checkAcceptButton() {
    return this.show_accept_button;
  }
}
