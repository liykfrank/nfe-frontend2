import { Observable } from 'rxjs';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AlertsComponent } from './alerts.component';

import { AlertsService } from '../../services/alerts.service';
import { SharedModule } from '../../../shared/shared.module';

import { AlertModel } from '../../models/alert.model';
import { AlertType } from './../../models/alert-type.enum';

describe('AlertsComponent', () => {
  let component: AlertsComponent;
  let fixture: ComponentFixture<AlertsComponent>;

  const alert: AlertModel = new AlertModel('TEXT', 'TEXT', AlertType.ERROR);
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['getAlert']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, BrowserAnimationsModule ],
      providers: [
        {provide: AlertsService, useValue: _AlertsService}
      ],
      declarations: [ AlertsComponent ]
    })
    .compileComponents();
  }));

  _AlertsService.getAlert.and.returnValue(Observable.of(alert));

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
