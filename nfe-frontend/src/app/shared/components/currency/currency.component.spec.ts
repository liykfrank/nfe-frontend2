import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyComponent } from './currency.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { CurrencyService } from './services/currency.service';
import { of } from 'rxjs/observable/of';
import { AlertsService } from '../../../core/services/alerts.service';

describe('CurrencyComponent', () => {
  let component: CurrencyComponent;
  let fixture: ComponentFixture<CurrencyComponent>;

  const currencyServiceStub = jasmine.createSpyObj<CurrencyService>('CurrencyService', ['setCurrencyState']);
  currencyServiceStub.setCurrencyState.and.returnValue(of());

  const alertsServiceStub = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);
  alertsServiceStub.setAlertTranslate.and.returnValue(of());

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CurrencyComponent ],
      schemas: [ NO_ERRORS_SCHEMA ],
      providers: [
          { provide: CurrencyService, useValue: currencyServiceStub },
          { provide: AlertsService, useValue: alertsServiceStub }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CurrencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
