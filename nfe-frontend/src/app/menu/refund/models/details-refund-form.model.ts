import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';

export class DetailsRefundFormModel extends ReactiveFormHandlerModel {
  detailsRefundGroup: FormGroup;
  originalIssue: FormGroup;
  relatedDocument: FormGroup;

  conjunctions: FormArray;

  statisticalCode: FormControl; // field radio buttons in Category
  passenger: FormControl; // field Passenger Name
  issueReason: FormControl; // field Detailed Reasons
  airlineCodeRelatedDocument: FormControl; // field 3 numbers in Document number
  dateOfIssueRelatedDocument: FormControl; // field Issue Date
  waiverCode: FormControl; // field Waiver Code
  exchange: FormControl; // field Exchange
  originalAgentCode: FormControl; // field issuing Agent
  originalAirlineCode: FormControl; // Document number field 3 numbers
  originalDateOfIssue: FormControl; // field date
  originalLocationCityCode: FormControl; // field city code
  originalTicketDocumentNumber: FormControl; // field 11 numbers in Document number

  constructor() {
    super();
  }

  createFormControls() {
    this.statisticalCode = new FormControl('DOM', [Validators.required]);
    this.passenger = new FormControl('', [Validators.required]);
    this.issueReason = new FormControl('', [Validators.required]);
    this.airlineCodeRelatedDocument = new FormControl('', [
      Validators.pattern('[a-zA-Z0-9]*$'),
      Validators.required,
      Validators.minLength(3)
    ]);
    this.dateOfIssueRelatedDocument = new FormControl('', [
      Validators.required
    ]);
    this.waiverCode = new FormControl('');
    this.exchange = new FormControl(false);
    this.originalAgentCode = new FormControl({ value: null, disabled: true });
    this.originalAirlineCode = new FormControl({ value: null, disabled: true });
    this.originalDateOfIssue = new FormControl({ value: null, disabled: true });
    this.originalLocationCityCode = new FormControl(
      { value: null, disabled: true },
      [Validators.minLength(3)]
    );
    this.originalTicketDocumentNumber = new FormControl({
      value: null,
      disabled: true
    });
  }

  createFormGroups() {
    this.relatedDocument = this.newCNJ();
    this.conjunctions = new FormArray([this.newCNJ()]);
    this.originalIssue = new FormGroup({
      originalAgentCode: this.originalAgentCode,
      originalAirlineCode: this.originalAirlineCode,
      originalDateOfIssue: this.originalDateOfIssue,
      originalLocationCityCode: this.originalLocationCityCode,
      originalTicketDocumentNumber: this.originalTicketDocumentNumber
    });
    this.relatedDocument
      .get('relatedTicketDocumentNumber')
      .valueChanges.subscribe(value => {
        if (value === '') {
          value = 0;
          this.relatedDocument
            .get('relatedTicketDocumentNumber')
            .setErrors({ required: true });
          this.relatedDocument
            .get('relatedTicketDocumentNumber')
            .markAsTouched();
        }
      });
  }

  createForm() {
    if (!this.detailsRefundGroup) {
      this.detailsRefundGroup = new FormGroup({
        statisticalCode: this.statisticalCode,
        passenger: this.passenger,
        issueReason: this.issueReason,
        airlineCodeRelatedDocument: this.airlineCodeRelatedDocument,
        relatedDocument: this.relatedDocument,
        dateOfIssueRelatedDocument: this.dateOfIssueRelatedDocument,
        waiverCode: this.waiverCode,
        conjunctions: this.conjunctions,
        exchange: this.exchange,
        originalIssue: this.originalIssue
      });
      this.detailsRefundGroup.get('exchange').valueChanges.subscribe(() => {
        this.enableOriginalIssueDetails();
      });
    }
  }

  getFormArray(complete: boolean = false): FormArray {
    return complete
      ? this.detailsRefundGroup.controls['conjunctions']
      : this.detailsRefundGroup.controls['conjunctions']['controls'];
  }

  getFormGroupInFormArray(position): FormGroup {
    return (this.detailsRefundGroup.get('conjunctions') as FormGroup).controls[
      position
    ] as FormGroup;
  }

  newCNJ() {
    return new FormGroup({
      relatedTicketDocumentNumber: new FormControl('', {updateOn: 'blur'}),
      relatedTicketCoupon1: new FormControl(false),
      relatedTicketCoupon2: new FormControl(false),
      relatedTicketCoupon3: new FormControl(false),
      relatedTicketCoupon4: new FormControl(false)
    });
  }

  getStateExChange() {
    return this.detailsRefundGroup.controls['exchange'].value;
  }

  enableOriginalIssueDetails() {
    if (this.detailsRefundGroup.controls.exchange.value) {
      this.detailsRefundGroup.controls.originalIssue.enable();
    } else {
      this.detailsRefundGroup.controls.originalIssue.disable();
      this.detailsRefundGroup.controls.originalIssue.reset();
    }
  }

  addCNJ() {
    this.conjunctions.push(this.newCNJ());
  }

  removeCNJ(i: number) {
    this.getFormArray(true).controls.splice(i, 1);
    const value = this.relatedDocument.get('relatedTicketDocumentNumber').value;
  }
}
