import { ReasonDetailForm } from './../../../shared/components/reasons/models/reason-detail-form.model';
import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { FormGroup, FormControl, FormArray, Validators } from '@angular/forms';
import { OnDestroy } from '@angular/core';

export class DetailsRefundFormModel extends ReactiveFormHandlerModel {
  detailsRefundGroup: FormGroup;
  originalIssue: FormGroup;
  releatedDocument: FormGroup;

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
    this.issueReason = new ReasonDetailForm().issueReason;
    this.airlineCodeRelatedDocument = new FormControl('', [Validators.required]);
    this.dateOfIssueRelatedDocument = new FormControl('', [Validators.required]);
    this.waiverCode = new FormControl('', [Validators.required]);
    this.exchange = new FormControl(false, [Validators.required]);
    this.originalAgentCode = new FormControl('', [Validators.required]);
    this.originalAirlineCode = new FormControl({value: null, disabled: true});
    this.originalDateOfIssue = new FormControl({value: null, disabled: true});
    this.originalLocationCityCode = new FormControl('', [Validators.required, Validators.minLength(3)]);
    this.originalTicketDocumentNumber = new FormControl({value: null, disabled: true});
  }

  createFormGroups() {
    this.releatedDocument = this.newCNJ();
    this.conjunctions = new FormArray([this.newCNJ()]);
    this.originalIssue = new FormGroup({
      originalAgentCode: this.originalAgentCode,
      originalAirlineCode: this.originalAirlineCode,
      originalDateOfIssue: this.originalDateOfIssue,
      originalLocationCityCode: this.originalLocationCityCode,
      originalTicketDocumentNumber: this.originalTicketDocumentNumber
    });
    this.releatedDocument.get('relatedTicketDocumentNumber').valueChanges.subscribe((value) => {
      for (let i = 0; i < this.detailsRefundGroup.controls['conjunctions']['controls'].length; i++) {
        this.detailsRefundGroup.controls['conjunctions']['controls'][i].get('relatedTicketDocumentNumber').
        setValue(parseInt(value, 0) + i + (1));
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
          releatedDocument: this.releatedDocument,
          dateOfIssueRelatedDocument: this.dateOfIssueRelatedDocument,
          waiverCode: this.waiverCode,
          conjunctions: this.conjunctions,
          exchange: this.exchange,
          originalIssue: this.originalIssue,
      });
      this.detailsRefundGroup.get('exchange').valueChanges.subscribe(() => {
        this.enableOriginalIssueDetails();
      });
    }
  }


  getFormArray(complete: boolean = false): FormArray {
    return complete ? this.detailsRefundGroup.controls['conjunctions'] : this.detailsRefundGroup.controls['conjunctions']['controls'];
  }

  getFormGroupInFormArray(position): FormGroup {
    return (this.detailsRefundGroup.get('conjunctions') as FormGroup).controls[position] as FormGroup;
  }

  newCNJ() {
    let valueNextCNJ: number;
     valueNextCNJ = (this.releatedDocument && this.releatedDocument.get('relatedTicketDocumentNumber'))
     ? parseInt(this.releatedDocument.get('relatedTicketDocumentNumber').value, 0) : 0;

     valueNextCNJ += (this.detailsRefundGroup) ?  this.detailsRefundGroup.controls['conjunctions']['controls'].length + 1 : 0;

    return new FormGroup({
      relatedTicketDocumentNumber: new FormControl(valueNextCNJ , [Validators.required]),
      relatedTicketCoupon1: new FormControl(false, [Validators.required]),
      relatedTicketCoupon2: new FormControl(false, [Validators.required]),
      relatedTicketCoupon3: new FormControl(false, [Validators.required]),
      relatedTicketCoupon4: new FormControl(false, [Validators.required]),

    });
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

  removeCNJ() {
    this.getFormArray(true).removeAt(this.getFormArray.length - 1);

    const value = this.releatedDocument.get('relatedTicketDocumentNumber').value;

    for (let i = 0; i < this.detailsRefundGroup.controls['conjunctions']['controls'].length; i++) {
      this.detailsRefundGroup.controls['conjunctions']['controls'][i].get('relatedTicketDocumentNumber').
      setValue(parseInt(value, 0) + i + (1));
    }
  }

  getStateExChange() {
    return this.detailsRefundGroup.controls['exchange'].value;
  }
}
