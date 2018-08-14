import {Component, Input, OnInit, SimpleChanges, OnChanges} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { EnvironmentType } from '../../enums/environment-type.enum';
import { ReactiveFormHandler } from './../../base/reactive-form-handler';
import { Currency } from './models/currency.model';
import { CurrencyService } from './services/currency.service';

@Component({
  selector: 'bspl-currency',
  templateUrl: './currency.component.html',
  styleUrls: ['./currency.component.scss']
})
export class CurrencyComponent implements OnInit, OnChanges {


  @Input() iso: string;
  @Input() type: EnvironmentType;
  @Input() defaultCurrency: string;
  currencyList: Currency[] = [];
  currencySelected: Currency;

  constructor(private _currencyService: CurrencyService) {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.iso.currentValue) {
      this._findCurrencyList(this.iso, this.defaultCurrency);
    }
  }


  onChangeCurrency(currency) {
    this._currencyService.setCurrencyState(currency);
  }

  private _findCurrencyList(iso: string, currencyDefault: string) {

      this._currencyService.getWithISO(this.type, iso).subscribe(data => {

        this.currencyList = data[0].currencies.filter(
          x => new Date(x.expirationDate).getTime() >= new Date().getTime());

        const currencyFound = this.currencyList.find( currency => currency.name === currencyDefault);
        this.currencySelected = currencyFound ? currencyFound : data[0].currencies[0];
        this._currencyService.setCurrencyState(this.currencySelected);
      });

  }

}
