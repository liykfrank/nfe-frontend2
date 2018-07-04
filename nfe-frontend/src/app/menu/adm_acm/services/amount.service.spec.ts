import { TestBed, inject } from '@angular/core/testing';

import { AmountService } from './amount.service';
import { InputAmountServer } from '../models/input-amount-server.model';
import { TaxAmountServer } from '../models/tax-amount-server';
import { Acdm } from '../models/acdm.model';

describe('AmountService', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AmountService]
    });
  });

  it('should be created', inject([AmountService], (service: AmountService) => {
    expect(service).toBeTruthy();
  }));

  it('AgentCalculations', inject([AmountService], (service: AmountService) => {
    expect(service.getAgentCalculations()).toBeTruthy();
  }));

  it('getAirlineCalculations', inject([AmountService], (service: AmountService) => {
    expect(service.getAirlineCalculations()).toBeTruthy();
  }));

  it('getTaxMiscellaneousFees', inject([AmountService], (service: AmountService) => {
    expect(service.getTaxMiscellaneousFees()).toBeTruthy();
  }));

  it('getValidTaxes', inject([AmountService], (service: AmountService) => {
    expect(service.getValidTaxes()).toBeTruthy();
  }));

  it('getTotal', inject([AmountService], (service: AmountService) => {
    expect(service.getTotal()).toBeTruthy();
  }));

  it('setAgentCalculations', inject([AmountService], (service: AmountService) => {
    let observe;
    service.getAgentCalculations().subscribe(data => observe = data);

    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    service.setAgentCalculations(input);
    expect(input.commission == observe.commission).toBe(true);
    expect(input.fare == observe.fare).toBe(true);
    expect(input.spam == observe.spam).toBe(true);
    expect(input.tax == observe.tax).toBe(true);
    expect(input.taxOnCommission == observe.taxOnCommission).toBe(true);
  }));

  it('setAirlineCalculations', inject([AmountService], (service: AmountService) => {
    let observe;
    service.getAirlineCalculations().subscribe(data => observe = data);

    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    service.setAirlineCalculations(input);
    expect(input.commission == observe.commission).toBe(true);
    expect(input.fare == observe.fare).toBe(true);
    expect(input.spam == observe.spam).toBe(true);
    expect(input.tax == observe.tax).toBe(true);
    expect(input.taxOnCommission == observe.taxOnCommission).toBe(true);
  }));

  it('setTaxMiscellaneousFees', inject([AmountService], (service: AmountService) => {
    let observe;
    service.getTaxMiscellaneousFees().subscribe(data => observe = data);

    const tax: TaxAmountServer = {type: 'AB', agentAmount: 5, airlineAmount: 5};
    service.setTaxMiscellaneousFees([tax]);
    expect(observe.length).toBe(1);
    service.setTaxMiscellaneousFees([tax, tax]);
    expect(observe.length).toBe(2);
  }));

  it('setTotal & checkTotal, check type ADMA/ACMA', inject([AmountService], (service: AmountService) => {
    const calc = new InputAmountServer();
    calc.fare = 1;
    service.setAgentCalculations(calc);
    service.setAirlineCalculations(calc);

    let observe;
    service.getTotal().subscribe(data => observe = data);
    expect(service.checkTotal('ADMA')).toBe(false, 'first test');

    let total = 1;
    service.setTotal(total);
    expect(total == observe).toBe(true);
    expect(service.checkTotal('ADMA')).toBe(true, 'first test');

    total = -1;
    service.setTotal(total);
    expect(service.checkTotal('ADMA')).toBe(false, 'first test');
  }));

  it('copyToAdcm', inject([AmountService], (service: AmountService) => {
    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    service.setAirlineCalculations(input);
    service.setAgentCalculations(input);

    const tax: TaxAmountServer = {type: 'AB', agentAmount: 5, airlineAmount: 5};
    service.setTaxMiscellaneousFees([tax]);

    const adcm: Acdm = new Acdm();
    service.copyToAdcm(adcm);

    expect(adcm.agentCalculations == input).toBe(true, 'agentCalculations');
    expect(adcm.airlineCalculations == input).toBe(true, 'airlineCalculations');
    //expect(adcm.taxMiscellaneousFees[0].type == tax[0].type).toBe(true, 'taxMiscellaneousFees');
    expect(adcm.amountPaidByCustomer == 0).toBe(true, 'amountPaidByCustomer');
  }));

  it('copyFromAdcm', inject([AmountService], (service: AmountService) => {
    let observeTotal, observeAgentCalculations, observeAirlineCalculations, observeTaxMiscellaneousFees;
    service.getAgentCalculations().subscribe(data => observeAgentCalculations = data);
    service.getAirlineCalculations().subscribe(data => observeAirlineCalculations = data);
    service.getTaxMiscellaneousFees().subscribe(data => observeTaxMiscellaneousFees = data);
    service.getTotal().subscribe(data => observeTotal = data);

    const adcm: Acdm = new Acdm();

    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    const tax: TaxAmountServer = {type: 'AB', agentAmount: 5, airlineAmount: 5};

    const total = 3;

    adcm.agentCalculations = input;
    adcm.airlineCalculations = input;
    adcm.taxMiscellaneousFees = [tax];
    adcm.amountPaidByCustomer = total;

    service.copyFromAdcm(adcm);

    expect(observeTotal == total).toBe(true, 'total error');
    expect(observeAgentCalculations == input).toBe(true, 'agent error');
    expect(observeAirlineCalculations == input).toBe(true, 'airline error');
    //expect(observeTaxMiscellaneousFees == [tax]).toBe(true, 'taxes error');
  }));

  it('checkTaxes first case, no taxes', inject([AmountService], (service: AmountService) => {
    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    service.setAirlineCalculations(input);
    service.setAgentCalculations(input);

    expect(service.checkTaxes()).toBe(false);
  }));

  it('checkTaxes second case, one tax', inject([AmountService], (service: AmountService) => {
    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    service.setAirlineCalculations(input);
    service.setAgentCalculations(input);

    const tax: TaxAmountServer = {type: 'AB', agentAmount: 5, airlineAmount: 5};
    service.setTaxMiscellaneousFees([tax]);

    expect(service.checkTaxes()).toBe(true);
  }));

  it('checkTaxes third case, more than one tax', inject([AmountService], (service: AmountService) => {
    const input = new InputAmountServer();
    input.commission = 5;
    input.fare = 5;
    input.spam = 5;
    input.tax = 5;
    input.taxOnCommission = 5;

    service.setAirlineCalculations(input);
    service.setAgentCalculations(input);

    const tax: TaxAmountServer = {type: 'AB', agentAmount: 2.5, airlineAmount: 2.5};
    service.setTaxMiscellaneousFees([tax, tax]);

    expect(service.checkTaxes()).toBe(true);
  }));

});
