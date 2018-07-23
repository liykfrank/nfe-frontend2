import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Observable } from 'rxjs/Observable';

import { AlertsService } from '../../../../core/services/alerts.service';
import { SharedModule } from '../../../../shared/shared.module';
import { Configuration } from '../../models/configuration.model';
import { AdmAcmService } from '../../services/adm-acm.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { AgentService } from '../../services/resources/agent.service';
import { CompanyService } from '../../services/resources/company.service';
import { KeyValue } from './../../../../shared/models/key.value.model';
import { BasicInfoComponent } from './basic-info.component';
import { Contact } from '../../../../shared/models/contact.model';

describe('BasicInfoComponent', () => {
  let component: BasicInfoComponent;
  let fixture: ComponentFixture<BasicInfoComponent>;

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

  const keyValues: KeyValue[] = [{
    code: 'test',
    description: 'description test'
  }];

  const countries = [
    {'isoCountryCode': 'AA', 'name': 'Country AA'},
    {'isoCountryCode': 'BB', 'name': 'Country BB'},
    {'isoCountryCode': 'CC', 'name': 'Country CC'}
  ];

  const toca = [{'code': 'AAA', 'description': 'pppp', 'isoCountryCode': 'AA'}];
  const currency = [{'isoc': 'BB', 'currencies': [{'name': 'ZZZ', 'numDecimals': 0, 'expirationDate': '2017-01-01'}] }];
  const periods = {'isoc': 'AA', 'values': [2018071, 2018072]};

  const _CompanyService = jasmine.createSpyObj<CompanyService>('CompanyService',
    [
      'getAirlineCountryAirlineCode',
      'getFromServerAirlineCountryAirlineCode'
    ]);
  const _AgentService = jasmine.createSpyObj<AgentService>('AgentService', [
    'getAgent',
    'getAgentWithCode'
  ]);
  const _BISvc = jasmine.createSpyObj<BasicInfoService>('BasicInfoService',
    [
      'setBasicInfo',
      'getCountries',
      'getTocaAndCurrencies',
      'getToca',
      'getCurrency',
      'getPeriod',
      'getValidBasicInfo',
      'getSubTypeList',
      'getSPDRCombo',
      'getStatList',
      'setBasicInfo'
    ]);
  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService',
    [
      'getConfiguration',
      'findCountryConfiguration',
      'getErrors',
      'setCurrency',
      'setSubtype',
      'setSpdr',
      'setSpan'
    ]);

  const _AlertsService = jasmine.createSpyObj<AlertsService>(
    'AlertsService',
    ['setAlertTranslate']
  );


  _AdmAcmService.getConfiguration.and.returnValue(Observable.of(conf));
  _AdmAcmService.findCountryConfiguration.and.returnValue(Observable.of(conf));
  _AdmAcmService.getErrors.and.returnValue(Observable.of([]));
  _AdmAcmService.setSubtype.and.callThrough();
  _AdmAcmService.setSpdr.and.callThrough();

  _BISvc.getCountries.and.returnValue(Observable.of(countries));
  _BISvc.getTocaAndCurrencies.and.returnValue(Observable.of({}));
  _BISvc.getToca.and.returnValue(Observable.of(toca));
  _BISvc.getCurrency.and.returnValue(Observable.of(currency));
  _BISvc.getPeriod.and.returnValue(Observable.of(periods));
  _BISvc.getValidBasicInfo.and.returnValue(Observable.of(true));
  _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
  _BISvc.getValidBasicInfo.and.returnValue(Observable.of({}));
  _BISvc.getSubTypeList.and.returnValue([keyValues]);
  _BISvc.getSPDRCombo.and.returnValue([keyValues]);
  _BISvc.getStatList.and.returnValue([keyValues]);


  _CompanyService.getAirlineCountryAirlineCode.and.returnValue(Observable.of({
    address1: 'mi dirección 1',
    airlineCode: 'ABC',
    city: 'Ciudad1',
    country: 'Pais1',
    globalName: 'Name',
    isoCountryCode: 'NA',
    postalCode: '12345',
    taxNumber: 'taxNumber',
    toDate: 'Date'
  }));
   _CompanyService.getFromServerAirlineCountryAirlineCode.and.returnValue(Observable.of({}));
  _AgentService.getAgent.and.returnValue(Observable.of({
    billingCity: 'City1',
    billingCountry: 'Country1',
    billingPostalCode: '00000',
    billingStreet: 'Street1',
    defaultDate: '01/01/2018',
    iataCode: 'SS',
    isoCountryCode: 'SS',
    name: 'Agent',
    vatNumber: 'ABC123'
  }));
   _AgentService.getAgentWithCode.and.returnValue(Observable.of({}));

  _AlertsService.setAlertTranslate.and.returnValue(Observable.of({}));


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule, BrowserAnimationsModule ],
      declarations: [ BasicInfoComponent ],
      providers: [
        {provide: AlertsService, useValue: _AlertsService},
        {provide: CompanyService, useValue: _CompanyService},
        {provide: AgentService, useValue: _AgentService},
        {provide: BasicInfoService, useValue: _BISvc},
        {provide: AdmAcmService, useValue: _AdmAcmService}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicInfoComponent);
    component = fixture.componentInstance;
    component.isADM = true;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('setNetReporting', () => {
    _BISvc.setBasicInfo.calls.reset();
    _AdmAcmService.setSpan.calls.reset();

    _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
    _AdmAcmService.setSpan.and.returnValue(Observable.of({}));

    component.setNetReporting();

    expect(_AdmAcmService.setSpan.calls.count()).toBe(1, '_AdmAcmService.setSpan error');
    expect(_BISvc.setBasicInfo.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('onChangeAirlineCode', () => {
    _CompanyService.getFromServerAirlineCountryAirlineCode.calls.reset();
    _CompanyService.getFromServerAirlineCountryAirlineCode.and.returnValue(Observable.of({}));
    component.basicInfo.isoCountryCode = 'AA';
    component.onChangeAirlineCode('11111111');
    expect(_CompanyService.getFromServerAirlineCountryAirlineCode.calls.count()).toBe(1);
  });

  it('onChangeVatNumber', () => {
    component.configuration.airlineVatNumberEnabled = true;
    fixture.detectChanges();

    _BISvc.setBasicInfo.calls.reset();
    _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
    component.onChangeVatNumber('TEXT');
    expect(component.basicInfo.airlineVatNumber).toBe('TEXT');
    expect(_BISvc.setBasicInfo.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('onChangeCompanyReg', () => {
    component.configuration.companyRegistrationNumberEnabled = true;
    fixture.detectChanges();

    _BISvc.setBasicInfo.calls.reset();
    _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
    component.onChangeCompanyReg('TEXT');
    expect(component.basicInfo.airlineRegistrationNumber).toBe('TEXT');
    expect(_BISvc.setBasicInfo.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('onChangeContact', () => {
    component.configuration.companyRegistrationNumberEnabled = true;
    fixture.detectChanges();

    const contact: Contact = new Contact();
    contact.contactName = 'TEXT';
    contact.email = 'TEXT';
    contact.phoneFaxNumber = 'TEXT';

    _BISvc.setBasicInfo.calls.reset();
    _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
    component.onChangeContact(contact);
    expect(component.basicInfo.airlineContact.contactName).toBe('TEXT');
    expect(component.basicInfo.airlineContact.email).toBe('TEXT');
    expect(component.basicInfo.airlineContact.phoneFaxNumber).toBe('TEXT');
    expect(_BISvc.setBasicInfo.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('airlineMoreDetails/closeAirline', () => {
    component.airlineMoreDetails();
    expect(component.displayAirline).toBe(true);
    component.closeAirline();
    expect(component.displayAirline).toBe(false);
  });

  it('onChangeAgentCode', () => {
    _AgentService.getAgentWithCode.calls.reset();
    _AgentService.getAgentWithCode.and.returnValue(Observable.of({}));
    component.onChangeAgentCode('TEXT');
    expect(_AgentService.getAgentWithCode.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('onChangeAgentVatNumber', () => {
    component.configuration.agentVatNumberEnabled = true;
    fixture.detectChanges();

    _BISvc.setBasicInfo.calls.reset();
    _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
    component.onChangeAgentVatNumber('TEXT');
    expect(component.basicInfo.agentVatNumber).toBe('TEXT');
    expect(_BISvc.setBasicInfo.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('onChangeAgentCompanyReg', () => {
    component.configuration.companyRegistrationNumberEnabled = true;
    fixture.detectChanges();

    _BISvc.setBasicInfo.calls.reset();
    _BISvc.setBasicInfo.and.returnValue(Observable.of({}));
    component.onChangeAgentCompanyReg('TEXT');
    expect(component.basicInfo.agentRegistrationNumber).toBe('TEXT');
    expect(_BISvc.setBasicInfo.calls.count()).toBe(1, '_BISvc.setBasicInfo error');
  });

  it('agentMoreDetails/closeAgent', () => {
    component.agentMoreDetails();
    expect(component.displayAgent).toBe(true);
    component.closeAgent();
    expect(component.displayAgent).toBe(false);
  });

});
