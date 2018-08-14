import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { CurrencyPost } from '../../../shared/components/currency/models/currency-post.model';

@Injectable()
export class BasicInfoService {

  private $subType = new BehaviorSubject<string>('ADMA');
  private $currency = new BehaviorSubject<CurrencyPost>(new CurrencyPost());
  private $showSpam = new BehaviorSubject<boolean>(false);
  private $concernsIndicator = new BehaviorSubject<string>('I');

  public getSubType(): Observable<string> {
    return this.$subType.asObservable();
  }

  public setSubType(str: string): void {
    this.$subType.next(str);
  }

  public getCurrency(): Observable<CurrencyPost> {
    return this.$currency.asObservable();
  }

  public setCurrency(currency: CurrencyPost): void {
    this.$currency.next(currency);
  }

  public getShowSpam(): Observable<boolean> {
    return this.$showSpam.asObservable();
  }

  public setShowSpam(val: boolean): void {
    this.$showSpam.next(val);
  }

  public getConcernsIndicator(): Observable<string> {
    return this.$concernsIndicator.asObservable();
  }

  public setConcernsIndicator(str: string): void {
    this.$concernsIndicator.next(str);
  }
}
