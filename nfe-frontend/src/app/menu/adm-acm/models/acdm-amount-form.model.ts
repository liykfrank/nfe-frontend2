import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';

export class AcdmAmountForm extends ReactiveFormHandlerModel {
  _amountModelGroup: FormGroup;

  constructor() {
    super();
  }

  createFormControls() {}

  createFormGroups() {
    this._amountModelGroup = new FormGroup({
      agentCalculations: this._inputAmount(),
      airlineCalculations: this._inputAmount(),
      taxMiscellaneousFees: new FormArray([this._taxFormModelGroup()]),
      amountPaidByCustomer: new FormControl(0)
    });
  }

  createForm() {}

  private _inputAmount(): FormGroup {
    return new FormGroup({
      commission: new FormControl(0),
      fare: new FormControl(0),
      spam: new FormControl(0),
      tax: new FormControl(0),
      taxOnCommission: new FormControl(0)
    });
  }

  public _taxFormModelGroup(): FormGroup {
    return new FormGroup({
      type: new FormControl('', [
        Validators.pattern(
          /^(([O][ABC][A-Z0-9./-]{0,6})|([A-Z0-9]{2})|([X][F](([A-Z]{3})([0-9]{1,3})?)?))$/
        )
      ]),
      agentAmount: new FormControl(0),
      airlineAmount: new FormControl(0)
    });
  }

  addTax() {
    (this._amountModelGroup.get('taxMiscellaneousFees') as FormArray).push(this._taxFormModelGroup());
  }

  remove(pos: number) {
    (this._amountModelGroup.get('taxMiscellaneousFees') as FormArray).removeAt(pos);
  }
}
