import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { GLOBALS } from '../../../shared/constants/globals';

export class AcdmDetailsForm extends ReactiveFormHandlerModel {
  acdmDetailsFormGroup: FormGroup;
  extra: FormGroup;
  relatedTicketDocuments: FormArray;
  ticketDocumentNumberToAdd: FormControl;

  constructor() {
    super();
  }

  createFormControls() {
    this.ticketDocumentNumberToAdd = new FormControl('');
  }

  createFormGroups() {
    this.relatedTicketDocuments = new FormArray([]);
  }

  createForm() {
    this.acdmDetailsFormGroup = new FormGroup({
      dateOfIssueRelatedDocument: new FormControl(null), // ISO Format 2018-06-13T11:20:01.736Z;
      passenger: new FormControl('', [Validators.pattern(GLOBALS.PATTERNS.PASSSENGER)]),
      relatedTicketDocuments: this.relatedTicketDocuments,
      reasonForMemoIssuanceCode: new FormControl(''),
      reasonForMemo: new FormControl('')
    });

    this.extra = new FormGroup({
      ticketDocumentNumberToAdd: this.ticketDocumentNumberToAdd
    });
  }

  addTicketDocument(relatedTicketDocumentNumber: string, checkDigit?: number) {
    this.relatedTicketDocuments.push(
      this._tickectDocument(relatedTicketDocumentNumber, checkDigit)
    );
  }

  removeTicketDocument(pos: number) {
    this.relatedTicketDocuments.removeAt(pos);
  }

  private _tickectDocument(
    relatedTicketDocumentNumber: string,
    checkDigit?: number
  ) {
    return new FormGroup({
      checkDigit: new FormControl(checkDigit),
      relatedTicketDocumentNumber: new FormControl(relatedTicketDocumentNumber)
    });
  }
}
