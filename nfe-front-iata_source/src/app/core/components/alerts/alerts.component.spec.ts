import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { DialogModule } from 'primeng/dialog';
import { GrowlModule } from 'primeng/primeng';
import { Observable } from 'rxjs';

import { AlertType } from '../../enums/alert-type.enum';
import { AlertModel } from '../../models/alert.model';
import { AlertsService } from '../../services/alerts.service';
import { AlertsComponent } from './alerts.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('AlertsComponent', () => {
  let component: AlertsComponent;
  let fixture: ComponentFixture<AlertsComponent>;

  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', [
    'getAlert',
    'setAccept',
    'getAccept'
  ]);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserAnimationsModule,
        GrowlModule,
        DialogModule
      ],
      providers: [{ provide: AlertsService, useValue: _AlertsService }],
      declarations: [AlertsComponent]
    }).compileComponents();
  }));

  const alert: AlertModel = new AlertModel('TEXT', 'TEXT', AlertType.ERROR);
  _AlertsService.getAlert.and.returnValue(Observable.of(alert));
  _AlertsService.setAccept.and.callThrough();
  _AlertsService.getAccept.and.returnValue(Observable.of(true));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create ERROR', () => {
    expect(component).toBeTruthy();
  });

  it('should create WARN', () => {
    alert.alert_type = AlertType.WARN;
    _AlertsService.getAlert.and.returnValue(Observable.of(alert));
    expect(component).toBeTruthy();
  });

  it('should create INFO', () => {
    alert.alert_type = AlertType.INFO;
    _AlertsService.getAlert.and.returnValue(Observable.of(alert));
    expect(component).toBeTruthy();
  });

  it('should create CONFIRM', () => {
    alert.alert_type = AlertType.CONFIRM;
    _AlertsService.getAlert.and.returnValue(Observable.of(alert));
    expect(component).toBeTruthy();
  });

  it('onClickAccept', () => {
    expect(component.display).toBe(true);
    component.onClickAccept();
    expect(component.display).toBe(false);
  });

  it('onClickCancel', () => {
    expect(component.display).toBe(true);
    component.onClickCancel();
    expect(component.display).toBe(false);
  });
});
