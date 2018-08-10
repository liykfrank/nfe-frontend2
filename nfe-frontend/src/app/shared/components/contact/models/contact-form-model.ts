import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { OnDestroy } from '@angular/core';

export class ContactFormModel extends ReactiveFormHandlerModel {
  contactFormModelGroup: FormGroup;

  contactName: FormControl; // field issuing Agent
  email: FormControl; // Document number field 3 numbers
  phoneFaxNumber: FormControl; // field 11 numbers in Document number

  constructor() {
    super();
  }

  createFormControls() {
    this.contactName = new FormControl({value: '', disabled: false}, [Validators.required]);
    this.email = new FormControl('', [Validators.pattern('[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$'), Validators.required]);
    this.phoneFaxNumber = new FormControl({value: '', disabled: false}, [Validators.required]);
  }

  createFormGroups() {}

  createForm() {
    this.contactFormModelGroup = new FormGroup({
      contactName: this.contactName,
      email: this.email,
      phoneFaxNumber: this.phoneFaxNumber
    });
  }

}
