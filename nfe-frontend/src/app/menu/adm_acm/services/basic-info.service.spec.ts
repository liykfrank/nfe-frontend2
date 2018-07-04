import { TestBed, inject } from '@angular/core/testing';

import { BasicInfoService } from './basic-info.service';
import { TranslationService } from 'angular-l10n';
import { CompanyService } from './resources/company.service';
import { CountryService } from './resources/country.service';
import { TocaService } from './resources/toca.service';
import { CurrencyService } from './resources/currency.service';
import { PeriodService } from './resources/period.service';
import { AgentService } from './resources/agent.service';
import { Observable } from 'rxjs/Observable';
import { BasicInfoModel } from '../models/basic-info.model';
import { Acdm } from '../models/acdm.model';
import { AlertsService } from '../../../core/services/alerts.service';
import { Configuration } from '../models/configuration.model';

describe('BasicInfoService', () => {
  const _AlertsService = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate']);

  const _TranslationService = jasmine.createSpyObj<TranslationService>('TranslationService', ['translate']);
  const _AgentService = jasmine.createSpyObj<AgentService>('AgentService', ['getAgentWithCode']);
  const _CompanyService = jasmine.createSpyObj<CompanyService>('CompanyService', ['getFromServerAirlineCountryAirlineCode']);
  const _CountryService = jasmine.createSpyObj<CountryService>('CountryService', ['get', 'getUrl', 'configureUrl']);
  const _TocaService = jasmine.createSpyObj<TocaService>('TocaService', ['getToca', 'getTocaWithISO']);
  const _CurrencyService = jasmine.createSpyObj<CurrencyService>('CurrencyService', ['getCurrency', 'getCurrencyWithISO']);
  const _PeriodService = jasmine.createSpyObj<PeriodService>('PeriodService', ['getPeriod', 'getPeriodWithISO']);

  _CountryService.get.and.returnValue(Observable.of({}));

  _TocaService.getToca.and.returnValue(Observable.of({}));
  _CurrencyService.getCurrency.and.returnValue(Observable.of({}));
  _PeriodService.getPeriod.and.returnValue(Observable.of({}));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BasicInfoService,
        {provide: AlertsService, useValue: _AlertsService},
        {provide: TranslationService, useValue: _TranslationService},
        {provide: CompanyService, useValue: _CompanyService},
        {provide: AgentService, useValue: _AgentService},
        {provide: CountryService, useValue: _CountryService},
        {provide: TocaService, useValue: _TocaService},
        {provide: CurrencyService, useValue: _CurrencyService},
        {provide: PeriodService, useValue: _PeriodService}
      ]
    });
  });

  it('should be created', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service).toBeTruthy();
  }));

  it('getSubTypeList', inject([BasicInfoService], (service: BasicInfoService) => {
    const listTrue = service.getSubTypeList(true);
    const listFalse = service.getSubTypeList(false);

    expect(listTrue != listFalse).toBe(true);
  }));

  it('getSPDRCombo', inject([BasicInfoService], (service: BasicInfoService) => {
    _TranslationService.translate.and.returnValue(Observable.of('translate'));

    expect(service.getSPDRCombo()).toBeTruthy();
  }));

  it('getStatList', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getStatList()).toBeTruthy();
  }));

  it('getBasicInfo', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getBasicInfo()).toBeTruthy();
  }));

  it('getToca', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getToca()).toBeTruthy();
  }));

  it('getCurrency', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getCurrency()).toBeTruthy();
  }));

  it('getPeriod', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getPeriod()).toBeTruthy();
  }));

  it('getCountries', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getCountries()).toBeTruthy();
  }));

  it('getValidBasicInfo', inject([BasicInfoService], (service: BasicInfoService) => {
    expect(service.getValidBasicInfo()).toBeTruthy();
  }));

  it('setValidBasicInfo', inject([BasicInfoService], (service: BasicInfoService) => {
    let aux;
    service.getValidBasicInfo().subscribe(data => aux = data);

    service.setValidBasicInfo(false)
    expect(aux == false).toBe(true);
  }));






  it('setBasicInfo', inject([BasicInfoService], (service: BasicInfoService) => {
    let observe;
    service.getBasicInfo().subscribe(data => observe = data);

    const biModel: BasicInfoModel = new BasicInfoModel();
    biModel.isoCountryCode = 'ABC';
    biModel.concernsIndicator = 'ABC';
    biModel.tocaType = 'ABC';

    service.setBasicInfo(biModel);
    expect(biModel.isoCountryCode == observe.isoCountryCode).toBe(true);
    expect(biModel.concernsIndicator == observe.concernsIndicator).toBe(true);
    expect(biModel.tocaType == observe.tocaType).toBe(true);
  }));

  it('copyToAdcm', inject([BasicInfoService], (service: BasicInfoService) => {
    const biModel: BasicInfoModel = new BasicInfoModel();
    biModel.isoCountryCode = 'ABC';
    biModel.billingPeriod = 1;
    biModel.agent.iataCode = 'ABC';
    biModel.agentRegistrationNumber = 'ABC';
    biModel.agentVatNumber = 'ABC';
    biModel.transactionCode = 'ABC';
    biModel.company.airlineCode = 'ABC';
    biModel.airlineRegistrationNumber = 'ABC';
    biModel.airlineVatNumber = 'ABC';
    biModel.airlineContact = {contactName: 'name', email: 'user@email.com', phoneFaxNumber: '99'} ;
    biModel.concernsIndicator = 'ABC';
    biModel.tocaType = 'ABC';
    biModel.currency = {code: 'AAA', decimals: 2};
    biModel.netReporting = false;
    biModel.stat = 'DOM';

    service.setBasicInfo(biModel);

    const acdm: Acdm = new Acdm();
    service.copyToAdcm(acdm);

    expect(acdm.isoCountryCode == biModel.isoCountryCode).toBe(true);
    expect(acdm.billingPeriod == biModel.billingPeriod).toBe(true);
    expect(acdm.agentCode == biModel.agent.iataCode).toBe(true);
    expect(acdm.agentRegistrationNumber == biModel.agentRegistrationNumber).toBe(true);
    expect(acdm.agentVatNumber == biModel.agentVatNumber).toBe(true);
    expect(acdm.transactionCode == biModel.transactionCode).toBe(true);
    expect(acdm.airlineCode == biModel.company.airlineCode).toBe(true);
    expect(acdm.airlineRegistrationNumber == biModel.airlineRegistrationNumber).toBe(true);
    expect(acdm.airlineVatNumber == biModel.airlineVatNumber).toBe(true);
    expect(acdm.airlineContact == biModel.airlineContact).toBe(true);
    expect(acdm.concernsIndicator == biModel.concernsIndicator).toBe(true);
    expect(acdm.taxOnCommissionType == biModel.tocaType).toBe(true);
    expect(acdm.currency == biModel.currency).toBe(true);
    expect(acdm.netReporting == biModel.netReporting).toBe(true);
    expect(acdm.statisticalCode == biModel.stat).toBe(true);
  }));

  it('copyFromAdcm with agent', inject([BasicInfoService], (service: BasicInfoService) => {
    _AgentService.getAgentWithCode.calls.reset();
    _CompanyService.getFromServerAirlineCountryAirlineCode.calls.reset();

    _AgentService.getAgentWithCode.and.returnValue(Observable.of({iataCode: 'ABC'}));
    _CompanyService.getFromServerAirlineCountryAirlineCode.and.returnValue(Observable.of({airlineCode: 'ABC'}));

    let biModel: BasicInfoModel;
    service.getBasicInfo().subscribe(data => biModel = data);

    const acdm: Acdm = new Acdm();
    acdm.isoCountryCode = 'ABC';
    acdm.billingPeriod = 1;
    acdm.agentCode = 'ABC';
    acdm.agentRegistrationNumber = 'ABC';
    acdm.agentVatNumber = 'ABC';
    acdm.transactionCode = 'ABC';
    acdm.airlineCode = 'ABC';
    acdm.airlineRegistrationNumber = 'ABC';
    acdm.airlineVatNumber = 'ABC';
    acdm.airlineContact = {contactName: 'name', email: 'user@email.com', phoneFaxNumber: '99'} ;
    acdm.concernsIndicator = 'ABC';
    acdm.taxOnCommissionType = 'ABC';
    acdm.currency = {code: 'AAA', decimals: 2};
    acdm.netReporting = false;
    acdm.statisticalCode = 'DOM';

    service.copyFromAdcm(acdm);

    expect(_AgentService.getAgentWithCode.calls.count()).toBe(1);
    expect(_CompanyService.getFromServerAirlineCountryAirlineCode.calls.count()).toBe(1);

    expect(acdm.isoCountryCode == biModel.isoCountryCode).toBe(true, 'isoCountryCode error');
    expect(acdm.billingPeriod == biModel.billingPeriod).toBe(true, 'billingPeriod error');
    expect(acdm.agentRegistrationNumber == biModel.agentRegistrationNumber).toBe(true, 'agentRegistrationNumber error');
    expect(acdm.agentVatNumber == biModel.agentVatNumber).toBe(true, 'agentVatNumber error');
    expect(acdm.transactionCode == biModel.transactionCode).toBe(true, 'transactionCode error');
    expect(acdm.airlineRegistrationNumber == biModel.airlineRegistrationNumber).toBe(true, 'airlineRegistrationNumber error');
    expect(acdm.airlineVatNumber == biModel.airlineVatNumber).toBe(true, 'airlineVatNumber error');
    expect(acdm.airlineContact == biModel.airlineContact).toBe(true, 'airlineContact error');
    expect(acdm.concernsIndicator == biModel.concernsIndicator).toBe(true, 'concernsIndicator error');
    expect(acdm.taxOnCommissionType == biModel.tocaType).toBe(true, 'taxOnCommissionType error');
    expect(acdm.currency == biModel.currency).toBe(true, 'currency error');
    expect(acdm.netReporting == biModel.netReporting).toBe(true, 'netReporting error');
    expect(acdm.statisticalCode == biModel.stat).toBe(true, 'statisticalCode error');
  }));

  it('getTocaAndCurrencies, no ISO', inject([BasicInfoService], (service: BasicInfoService) => {
    _AlertsService.setAlertTranslate.calls.reset();
    _AlertsService.setAlertTranslate.and.returnValue(Observable.of({}));
    service.getTocaAndCurrencies('');
    expect(_AlertsService.setAlertTranslate.calls.count()).toBe(1);
  }));

  it('getTocaAndCurrencies, no ISO', inject([BasicInfoService], (service: BasicInfoService) => {
    _TocaService.getTocaWithISO.and.returnValue(Observable.of({}));
    _CurrencyService.getCurrencyWithISO.and.returnValue(Observable.of({}));
    _PeriodService.getPeriodWithISO.and.returnValue(Observable.of({}));

    _TocaService.getTocaWithISO.calls.reset();
    _CurrencyService.getCurrencyWithISO.calls.reset();
    _PeriodService.getPeriodWithISO.calls.reset();

    service.getTocaAndCurrencies('AAA');

    expect(_TocaService.getTocaWithISO.calls.count()).toBe(1);
    expect(_CurrencyService.getCurrencyWithISO.calls.count()).toBe(1);
    expect(_PeriodService.getPeriodWithISO.calls.count()).toBe(1);
  }));

  it('checkBasicInfo, only new', inject([BasicInfoService], (service: BasicInfoService) => {
    const conf = new Configuration();
    conf.airlineVatNumberEnabled = true;
    conf.companyRegistrationNumberEnabled = true;
    conf.agentVatNumberEnabled = true;

    const ret = service.checkBasicInfo(conf);
    expect(ret.length > 0).toBe(true);
  }));

});
