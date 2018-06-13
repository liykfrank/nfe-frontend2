import { Injectable } from '@angular/core';
import { InputAmountServer } from '../models/input-amount-server.model';
import { TaxAmountServer } from '../models/tax-amount-server';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class AmountService {

  private validTaxes = new BehaviorSubject<boolean>(false);
  private agentCalculations = new BehaviorSubject<InputAmountServer>(null);
  private airlineCalculations = new BehaviorSubject<InputAmountServer>(null);
  private taxMiscellaneousFees = new BehaviorSubject<TaxAmountServer[]>([]);

  private total = new BehaviorSubject<number>(0);

  constructor() { }

  checkTaxes(): boolean {
    let ret = false;
    let agent = 0, airline = 0;

    for (let i of this.taxMiscellaneousFees.getValue()) {
      agent = agent + i.agentAmount;
      airline = airline + i.airlineAmount;
    }

    ret = (agent == this.agentCalculations.getValue().tax) && (airline == this.airlineCalculations.getValue().tax);
    this.validTaxes.next(ret);

    return ret;
  }

  getAgentCalculations(): Observable<InputAmountServer> {
    return this.agentCalculations.asObservable();
  }

  setAgentCalculations(agent: InputAmountServer): void {
    this.agentCalculations.next(agent);
  }

  getAirlineCalculations(): Observable<InputAmountServer> {
    return this.airlineCalculations.asObservable();
  }

  setAirlineCalculations(airline: InputAmountServer): void {
    this.airlineCalculations.next(airline);
  }

  getTaxMiscellaneousFees(): Observable<TaxAmountServer[]> {
    return this.taxMiscellaneousFees.asObservable();
  }

  setTaxMiscellaneousFees(taxes: TaxAmountServer[]) {
    this.taxMiscellaneousFees.next(taxes);
  }

  getValidTaxes(): Observable<boolean>{
    return this.validTaxes.asObservable();
  }

  setTotal(total: number): void {
    this.total.next(total);
  }

  getTotal(): Observable<number> {
    return this.total.asObservable();
  }

}
