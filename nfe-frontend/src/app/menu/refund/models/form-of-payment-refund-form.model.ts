import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';

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
    this.creditEPSubTotal = new FormControl({value: 0, disabled: true}, []);
    this.totalAmount = new FormControl({value: 0, disabled: true}, [Validators.required]);
    this.tourCode = new FormControl('', []);
    this.customerFileReference = new FormControl('');
    this.settlementAuthorisationCode = new FormControl('', []);
  }

  createFormGroups() {
    this.formOfPaymentAmounts = new FormArray([]);
    this.selectAmount = new FormGroup({
      amount: new FormControl(0),
      number: new FormControl('', [Validators.required]),
      vendorCode: new FormControl('', [Validators.required])
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
      settlementAuthorisationCode: this.settlementAuthorisationCode
    });
  }

  getFormOfPaymentAmount(position = null): FormArray {
    return position == null ?
    this.formOfPaymentRefundGroup.controls['formOfPaymentAmounts']['controls'] :
    this.formOfPaymentRefundGroup.controls['formOfPaymentAmounts']['controls'][position];
  }

  getFormOfPaymentAmountControlsElem(position: number) {
    return this.getFormOfPaymentAmount(position).controls;
  }

  addCreditOrEPAmount(amountValue: string, fopTypeText: string, vendorCode: string, number: number): void {
    this.formOfPaymentAmounts.push(
      new FormGroup({
        type: new FormControl(fopTypeText, []),
        amount: new FormControl(amountValue, [] ),
        vendorCode: new FormControl(vendorCode, [Validators.required] ),
        number: new FormControl(number, [Validators.required]),
      })
    );
  }

  addCashAmount(amountValue: string, fopTypeText: string): void {
    this.formOfPaymentAmounts.push(
      new FormGroup({
        type: new FormControl(fopTypeText, []),
        amount: new FormControl(amountValue, [])
      })
    );
  }

  deleteAmountFormArrayElem(i: number): void {
    this.formOfPaymentAmounts.removeAt(i);
  }

}
