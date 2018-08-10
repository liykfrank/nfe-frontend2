import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { InputAmountServer } from '../models/input-amount-server.model';
import { TaxAmountServer } from '../models/tax-amount-server';
import { Acdm } from './../models/acdm.model';

@Injectable()
export class AmountService {
 /*  private $validTaxes = new BehaviorSubject<boolean>(false);
  private $agentCalculations = new BehaviorSubject<InputAmountServer>(null);
  private $airlineCalculations = new BehaviorSubject<InputAmountServer>(null);
  private $taxMiscellaneousFees = new BehaviorSubject<TaxAmountServer[]>([]);

  private $total = new BehaviorSubject<number>(0);
  private subtype: string;

  constructor() {}

  public checkTaxes(): boolean {
    let ret = false;
    let agent = 0,
      airline = 0;

    for (const i of this.$taxMiscellaneousFees.getValue()) {
      agent = agent + +i.agentAmount;
      airline = airline + +i.airlineAmount;
    }
    ret =
      agent == this.$agentCalculations.getValue().tax &&
      airline == this.$airlineCalculations.getValue().tax;
    this.$validTaxes.next(ret);

    return ret;
  }

  public checkTotal(type: string): boolean {
    const agent = this.$agentCalculations.getValue();
    const airline = this.$airlineCalculations.getValue();
    const total = this.$total.getValue();

    let ret = true;
    if (type == 'ACMD' || type == 'ADMD') {
      ret = ret && (agent.fare > 0 || airline.fare > 0);
    }
    return total > 0 && ret;
  }

  public copyToAdcm(adcm: Acdm): void {
    adcm.agentCalculations = this.$agentCalculations.getValue();
    adcm.airlineCalculations = this.$airlineCalculations.getValue();
    adcm.taxMiscellaneousFees = this.$taxMiscellaneousFees.getValue();
    adcm.amountPaidByCustomer = this.$total.getValue();
  }

  public copyFromAdcm(adcm: Acdm): void {
    this.$agentCalculations.next(adcm.agentCalculations);
    this.$airlineCalculations.next(adcm.airlineCalculations);
    this.$taxMiscellaneousFees.next(adcm.taxMiscellaneousFees);
    this.$total.next(adcm.amountPaidByCustomer);
  }

  public getValidTaxes(): Observable<boolean> {
    return this.$validTaxes.asObservable();
  }

  public getAgentCalculations(): Observable<InputAmountServer> {
    return this.$agentCalculations.asObservable();
  }

  public setAgentCalculations(agent: InputAmountServer): void {
    this.$agentCalculations.next(agent);
  }

  public getAirlineCalculations(): Observable<InputAmountServer> {
    return this.$airlineCalculations.asObservable();
  }

  public setAirlineCalculations(airline: InputAmountServer): void {
    this.$airlineCalculations.next(airline);
  }

  public getTaxMiscellaneousFees(): Observable<TaxAmountServer[]> {
    return this.$taxMiscellaneousFees.asObservable();
  }

  public setTaxMiscellaneousFees(taxes: TaxAmountServer[]) {
    this.$taxMiscellaneousFees.next(taxes);
  }

  public getTotal(): Observable<number> {
    return this.$total.asObservable();
  }

  public setTotal(total: number): void {
    this.$total.next(total);
  } */
}
