import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SharedModule } from '../../../../shared/shared.module';
import { Configuration } from '../../models/configuration.model';
import { InputAmountServer } from '../../models/input-amount-server.model';
import { AdmAcmService } from '../../services/adm-acm.service';
import { AmountService } from '../../services/amount.service';
import { AmountComponent } from './amount.component';

xdescribe('AmountComponent', () => {
  let component: AmountComponent;
  let fixture: ComponentFixture<AmountComponent>;

  const conf = new Configuration();
  conf.agentVatNumberEnabled = false;
  conf.airlineVatNumberEnabled = false;
  conf.companyRegistrationNumberEnabled = false;
  conf.cpPermittedForConcerningIssue = false;
  conf.cpPermittedForConcerningRefund = false;
  conf.defaultStat = '';
  conf.freeStat = true;
  conf.isoCountryCode = '';
  conf.maxNumberOfRelatedDocuments = -1;
  conf.mfPermittedForConcerningIssue = false;
  conf.mfPermittedForConcerningRefund = false;
  conf.nridAndSpamEnabled = false;
  conf.taxOnCommissionEnabled = false;
  conf.taxOnCommissionSign = -1;

  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService', [
    'getConfiguration',
    'getCurrency',
    'getSubtype',
    'getSpdr',
    'getSpan'
  ]);
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService', [
    'getTotal',
    'getValidTaxes',
    'getAgentCalculations',
    'getAirlineCalculations',
    'getTaxMiscellaneousFees',
    'setTaxMiscellaneousFees',
    'setAgentCalculations',
    'setAirlineCalculations',
    'setTotal'
  ]);

  _AdmAcmService.getConfiguration.and.returnValue(Observable.of(conf));
  _AdmAcmService.getCurrency.and.returnValue(
    Observable.of({ name: 'AA', numDecimals: 2 })
  );
  _AdmAcmService.getSpan.and.returnValue(Observable.of(false));
  _AdmAcmService.getSubtype.and.returnValue(Observable.of('ADMA'));
  _AdmAcmService.getSpdr.and.returnValue(Observable.of('I'));

  _AmountService.getTotal.and.returnValue(Observable.of({}));
  _AmountService.getValidTaxes.and.returnValue(Observable.of({}));
  _AmountService.getAgentCalculations.and.returnValue(
    Observable.of(new InputAmountServer())
  );
  _AmountService.getAirlineCalculations.and.returnValue(
    Observable.of(new InputAmountServer())
  );
  _AmountService.getTaxMiscellaneousFees.and.returnValue(Observable.of([]));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      providers: [
        { provide: AdmAcmService, useValue: _AdmAcmService },
        { provide: AmountService, useValue: _AmountService }
      ],
      declarations: [AmountComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AmountComponent);
    component = fixture.componentInstance;
    component.isADM = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('validateName', () => {
    component.taxes[0].name = 'AA';
    component.taxes[0].agentValue = 1;
    component.validateName(0);
    expect(component.taxes[0].dif != 0).toBe(true);
  });

  it('simpleCalculateFare', () => {
    _AmountService.setAirlineCalculations.calls.reset();
    _AmountService.setAirlineCalculations.and.returnValue(Observable.of({}));

    component.simpleCalculateFare();
    expect(_AmountService.setAirlineCalculations.calls.count()).toBe(1);
  });

  it('simpleCalculateFare ACM', () => {
    component.isADM = false;
    _AmountService.setAgentCalculations.calls.reset();
    _AmountService.setAgentCalculations.and.returnValue(Observable.of({}));

    component.simpleCalculateFare();
    expect(_AmountService.setAgentCalculations.calls.count()).toBe(1);
  });

  it('calculateTotalOnAmount', () => {
    const agent = new InputAmountServer();
    agent.fare = 5;
    agent.tax = 3;

    const airline = new InputAmountServer();
    airline.fare = 4;
    airline.tax = 4;

    _AmountService.setAirlineCalculations.calls.reset();
    _AmountService.setAgentCalculations.calls.reset();

    _AmountService.setAirlineCalculations.and.returnValue(
      Observable.of(airline)
    );
    _AmountService.setAgentCalculations.and.returnValue(Observable.of(agent));

    component.calculateTotalOnAmount();
    expect(_AmountService.setAirlineCalculations.calls.count()).toBe(1);
    expect(_AmountService.setAgentCalculations.calls.count()).toBe(1);
  });

  it('calculateTotalOnAmount 2', () => {
    component.isADM = false;
    const agent = new InputAmountServer();
    agent.fare = 5;
    agent.tax = 3;

    const airline = new InputAmountServer();
    airline.fare = 4;
    airline.tax = 4;

    _AmountService.setAirlineCalculations.calls.reset();
    _AmountService.setAgentCalculations.calls.reset();

    _AmountService.setAirlineCalculations.and.returnValue(
      Observable.of(airline)
    );
    _AmountService.setAgentCalculations.and.returnValue(Observable.of(agent));

    component.calculateTotalOnAmount();
    expect(_AmountService.setAirlineCalculations.calls.count()).toBe(1);
    expect(_AmountService.setAgentCalculations.calls.count()).toBe(1);
  });

  it('removeTax', () => {
    component.taxes[0].name = 'AA';
    component.removeTax(0);
    expect(component.taxes[0].name == '').toBe(true);
  });
});
