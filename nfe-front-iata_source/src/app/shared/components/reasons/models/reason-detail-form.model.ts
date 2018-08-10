import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';

export class ReasonDetailForm extends ReactiveFormHandlerModel {
  reasonDetailsForm: FormGroup;
  issueReason: FormControl;

  constructor() {
    super();
  }

  createFormControls() {
    this.issueReason = new FormControl('', [Validators.required]);
  }

  createFormGroups() {}

  createForm() {
    this.reasonDetailsForm = new FormGroup({
      issueReason: this.issueReason
    });
  }
}
