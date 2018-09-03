import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { GLOBALS } from '../../../shared/constants/globals';

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
    this.passenger = new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.PASSSENGER), Validators.required]);
    this.issueReason = new FormControl('', [Validators.required]);
    this.airlineCodeRelatedDocument = new FormControl('', [
      Validators.pattern(GLOBALS.PATTERNS.AIRLINE),
      Validators.required,
      Validators.minLength(3)
    ]);
    this.dateOfIssueRelatedDocument = new FormControl('', [
      Validators.required
    ]);
    this.waiverCode = new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.WAIVER_CODE)]);
    this.exchange = new FormControl(false);
    this.originalAgentCode = new FormControl({ value: null, disabled: true });
    this.originalAirlineCode = new FormControl({ value: null, disabled: true }, [Validators.pattern(GLOBALS.PATTERNS.AIRLINE)]);
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
    }
  }

  newCNJ() {
    return new FormGroup({
      relatedTicketDocumentNumber: new FormControl('', {updateOn: 'blur', validators: [Validators.minLength(10)]}),
      relatedTicketCoupon1: new FormControl(false),
      relatedTicketCoupon2: new FormControl(false),
      relatedTicketCoupon3: new FormControl(false),
      relatedTicketCoupon4: new FormControl(false)
    });
  }

}
