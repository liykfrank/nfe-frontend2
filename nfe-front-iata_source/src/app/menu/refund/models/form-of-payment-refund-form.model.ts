import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';

export class FormOfPaymentRefundFormModel extends ReactiveFormHandlerModel {
  formOfPaymentRefundGroup: FormGroup;
  currency: FormGroup;
  formOfPaymentAmounts: FormArray;
  type: FormControl;
  amount: FormControl;
  creditEPSubTotal: FormControl;
  totalAmount: FormControl;
  code: FormControl;
  decimals: FormControl;
  tourCode: FormControl;
  customerFileReference: FormControl;
  settlementAuthorisationCode: FormControl;


  constructor() {
    super();
  }

  createFormControls() {
    this.type = new FormControl('', [Validators.required]);
    this.amount = new FormControl('');
    this.creditEPSubTotal = new FormControl('0.00', [Validators.required]);
    this.totalAmount = new FormControl('0.00', [Validators.required]);
    this.code = new FormControl('', [Validators.required]);
    this.decimals = new FormControl('', [Validators.required]);
    this.tourCode = new FormControl('');
    this.customerFileReference = new FormControl('');
    this.settlementAuthorisationCode = new FormControl('', [Validators.required]);
  }

  createFormGroups() {
    this.formOfPaymentAmounts = new FormArray([]);
    this.currency = new FormGroup({
      code: this.code,
      decimals: this.decimals
    });
  }

  createForm() {
    if (!this.formOfPaymentRefundGroup) {
      this.formOfPaymentRefundGroup = new FormGroup({
        formOfPaymentAmounts: this.formOfPaymentAmounts,
        type: this.type,
        amount: this.amount,
        creditEPSubTotal: this.creditEPSubTotal,
        totalAmount: this.totalAmount,
        currency: this.currency,
        tourCode: this.tourCode,
        customerFileReference: this.customerFileReference,
        settlementAuthorisationCode: this.settlementAuthorisationCode
      });
    }
  }

  getFormOfPaymentAmount(position = null): FormArray {
    return position == null ?
    this.formOfPaymentRefundGroup.controls['formOfPaymentAmounts']['controls'] :
    this.formOfPaymentRefundGroup.controls['formOfPaymentAmounts']['controls'][position];
  }



  getFormOfPaymentAmountControlsElem(position: number) {
    console.log(this.getFormOfPaymentAmount(position).controls['vendorCode']);
    return this.getFormOfPaymentAmount(position).controls;
  }

  addCreditOrEPAmount(amountValue: string, fopTypeText: string): void {
    this.formOfPaymentAmounts.push(
      new FormGroup({
        amount: new FormControl(amountValue, []),
        number: new FormControl(),
        type: new FormControl(fopTypeText, []),
        vendorCode: new FormControl()
      })
    );
    console.log(this.formOfPaymentAmounts.length);
  }

  addCashAmount(amountValue: string, fopTypeText: string): void {
    this.formOfPaymentAmounts.push(
      new FormGroup({
        type: new FormControl(fopTypeText, []),
        amount: new FormControl(amountValue, [])
      })
    );

    console.log(this.formOfPaymentAmounts.length);
  }

  deleteAmountFormArrayElem(i: number): void {
    this.formOfPaymentAmounts.removeAt(i);
  }
  
}
