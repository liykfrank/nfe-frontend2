import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { GLOBALS } from '../../../shared/constants/globals';

export class FormOfPaymentRefundFormModel extends ReactiveFormHandlerModel {
  formOfPaymentRefundGroup: FormGroup;
  selectAmount: FormGroup;
  formOfPaymentAmounts: FormArray;
  type: FormControl;
  amount: FormControl;
  creditEPSubTotal: FormControl;
  totalAmount: FormControl;
  tourCode: FormControl;
  customerFileReference: FormControl;
  settlementAuthorisationCode: FormControl;

  constructor() {
    super();
  }

  createFormControls() {
    this.type = new FormControl('', []);
    this.amount = new FormControl('');
    this.creditEPSubTotal = new FormControl({ value: 0, disabled: true }, []);
    this.totalAmount = new FormControl({ value: 0, disabled: true }, [
      Validators.required
    ]);
    this.tourCode = new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.TOUR_CODE)]);
    this.customerFileReference = new FormControl('');
    this.settlementAuthorisationCode = new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.ELECTRONIC_TICKET_AUTH)]);
  }

  createFormGroups() {
    this.formOfPaymentAmounts = new FormArray([]);
    this.selectAmount = new FormGroup({
      amount: new FormControl(0),
      number: new FormControl({ value: '', disabled: true }, [Validators.required]),
      vendorCode: new FormControl({ value: '', disabled: true }, [Validators.required])
    });
  }

  createForm() {
    this.formOfPaymentRefundGroup = new FormGroup({
      formOfPaymentAmounts: this.formOfPaymentAmounts,
      type: this.type,
      creditEPSubTotal: this.creditEPSubTotal,
      totalAmount: this.totalAmount,
      tourCode: this.tourCode,
      customerFileReference: this.customerFileReference,
      settlementAuthorisationCode: this.settlementAuthorisationCode,
    });
  }
}
