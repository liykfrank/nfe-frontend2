import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';

export class CurrencyFormModel extends ReactiveFormHandlerModel {
  currencyFormModelGroup: FormGroup;
  code: FormControl;
  decimals: FormControl;

  constructor() {
    super();
  }

  createFormControls() {
    this.code = new FormControl('', [Validators.required]);
    this.decimals = new FormControl(0, [Validators.required]);
  }
  createFormGroups() {}
  createForm() {
    this.currencyFormModelGroup = new FormGroup({
      code: this.code,
      decimals: this.decimals
    });
  }
}
