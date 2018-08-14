import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';

export class AcdmDetailsForm extends ReactiveFormHandlerModel {
  acdmDetailsFormGroup: FormGroup;

  constructor() {
    super();
  }

  createFormControls() {}

  createFormGroups() {}

  createForm() {
    this.acdmDetailsFormGroup = new FormGroup({
      dateOfIssueRelatedDocument: new FormControl(null, [Validators.required]), // ISO Format 2018-06-13T11:20:01.736Z;
      passenger: new FormControl('', [Validators.required]),
      relatedTicketDocuments: new FormArray([]),
      reasonForMemoIssuanceCode: new FormControl('', [Validators.required]),
      reasonForMemo: new FormControl('', [Validators.required]),
      ticketDocumentNumberToAdd: new FormControl('')
    });
  }

  addTicketDocument(relatedTicketDocumentNumber: string, checkDigit?: number) {
    (this.acdmDetailsFormGroup.get('relatedTicketDocuments') as FormArray).push(
      this._tickectDocument(relatedTicketDocumentNumber, checkDigit)
    );
  }

  removeTicketDocument(pos: number) {
    (this.acdmDetailsFormGroup.get('relatedTicketDocuments') as FormArray).removeAt(pos);
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
