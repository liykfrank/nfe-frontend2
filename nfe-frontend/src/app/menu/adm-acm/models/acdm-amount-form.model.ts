import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { GLOBALS } from '../../../shared/constants/globals';

export class AcdmAmountForm extends ReactiveFormHandlerModel {
  amountModelGroup: FormGroup;
  agentCalculations: FormGroup;
  airlineCalculations: FormGroup;

  taxMiscellaneousFees: FormArray;

  amountPaidByCustomer: FormControl;

  constructor() {
    super();
  }

  createFormControls() {
    this.amountPaidByCustomer = new FormControl(0);
  }

  createFormGroups() {
    this.agentCalculations = this._inputAmount();
    this.airlineCalculations = this._inputAmount();

    this.taxMiscellaneousFees = new FormArray([this._taxFormModelGroup()]);

  }

  createForm() {
    this.amountModelGroup = new FormGroup({
      agentCalculations: this.agentCalculations,
      airlineCalculations: this.airlineCalculations,
      taxMiscellaneousFees: this.taxMiscellaneousFees,
      amountPaidByCustomer: this.amountPaidByCustomer
    });
  }

  private _inputAmount(): FormGroup {
    return new FormGroup({
      commission: new FormControl({value: 0}),
      fare: new FormControl({value: 0}),
      spam: new FormControl({value: 0}),
      tax: new FormControl({value: 0}),
      taxOnCommission: new FormControl({value: 0})
    });
  }

  public _taxFormModelGroup(): FormGroup {
    return new FormGroup({
      type: new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.TAX)]),
      agentAmount: new FormControl(0),
      airlineAmount: new FormControl(0)
    });
  }

}
