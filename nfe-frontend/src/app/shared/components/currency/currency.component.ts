import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import { FormGroup } from '@angular/forms';

import { EnvironmentType } from '../../enums/environment-type.enum';
import { ReactiveFormHandler } from './../../base/reactive-form-handler';
import { CurrencyFormModel } from './models/currency-form.model';
import { Currency } from './models/currency.model';
import { CurrencyService } from './services/currency.service';

@Component({
  selector: 'bspl-currency',
  templateUrl: './currency.component.html',
  styleUrls: ['./currency.component.scss']
})
export class CurrencyComponent extends ReactiveFormHandler
  implements OnInit, OnChanges {
  currencyFormModelGroup: FormGroup = new CurrencyFormModel()
    .currencyFormModelGroup;

  @Input() iso: string;
  @Input() type: EnvironmentType;

  currencyList: Currency[] = [];

  constructor(private _currencyService: CurrencyService) {
    super();
  }

  ngOnInit() {
    if (!this.type) {
      throw new Error('Obligatorio Campo type en componente');
    } else {
      this._findReasonList();
    }

    this.subscribe(this.currencyFormModelGroup);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.iso) {
      this._findReasonList();
    }
  }

  onChange(event) {
    const value = this.currencyList[event.srcElement.value];
    this._setModel(value.name, value.numDecimals);
  }

  private _setModel(code: string, decimals: number) {
    this.currencyFormModelGroup.get('code').setValue(code);
    this.currencyFormModelGroup.get('decimals').setValue(decimals);
  }

  private _findReasonList() {
    if (this.iso && this.iso.length > 0) {
      this._currencyService.getWithISO(this.type, this.iso).subscribe(data => {
        this.currencyList = data[0].currencies.filter(
          x => new Date(x.expirationDate).getTime() >= new Date().getTime()
        );
        this._setModel(
          data[0].currencies[0].name,
          data[0].currencies[0].numDecimals
        );
      });
    }
  }
}
