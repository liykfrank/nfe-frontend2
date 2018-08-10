import { AlertModel } from './../models/alert.model';
import { TestBed, inject } from '@angular/core/testing';
import { AlertsService } from './alerts.service';
import { HttpClientModule } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../shared/base/conf/l10n.config';
import { AlertType } from '../enums/alert-type.enum';

describe('AlertsService', () => {

  const elem: AlertModel = new AlertModel('', '', AlertType.INFO);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [AlertsService]
    });
  });

  it('should be created', inject([AlertsService], (service: AlertsService) => {
    expect(service).toBeTruthy();
  }));

  it('getAlert', inject([AlertsService], (service: AlertsService) => {
    expect(service.getAlert()).toBeTruthy();
  }));

  it('getAccept', inject([AlertsService], (service: AlertsService) => {
    expect(service.getAccept()).toBeTruthy();
  }));

  it('setAlert', inject([AlertsService], (service: AlertsService) => {
    let alert;
    service.getAlert().subscribe(data => alert = data);
    service.setAlert(elem);
    expect(alert.alert_type == elem.alert_type).toBe(true);
  }));

  it('setAlert', inject([AlertsService], (service: AlertsService) => {
    let alert;
    service.getAlert().subscribe(data => alert = data);

    service.setAlertTranslate('', '', AlertType.INFO);
    expect(alert.alert_type == elem.alert_type).toBe(true);
  }));




});
