import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ContactFormModel } from '../../contact/models/contact-form-model';
import { GLOBALS } from '../../../constants/globals';

export class AirlineFormModel extends ReactiveFormHandlerModel {
  airlineFormModelGroup: FormGroup;
  airlineContact: ContactFormModel;

  airlineCode: FormControl; // field airline code
  airlineRegistrationNumber: FormControl; // forgot for the moment
  airlineVatNumber: FormControl; // forgot for the moment
  airlineRemark: FormControl; // forgot for the moment

  private defaultDisabled: boolean = false;
  private disabledVAT: boolean = false;
  private disabledReg: boolean = false;
  private disableContact: boolean = false;

  constructor(
    defaultDisabled?: boolean,
    disabledVAT?: boolean,
    disabledReg?: boolean,
    disableContact?: boolean
  ) {
    super();

    this.defaultDisabled = defaultDisabled;
    this.disabledVAT = disabledVAT;
    this.disabledReg = disabledReg;
    this.disableContact = disableContact;


    if (this.defaultDisabled) {
      this.airlineCode.disable();
    }

    if (this.defaultDisabled || this.disabledReg) {
      this.airlineRegistrationNumber.disable();
    }

    if (this.defaultDisabled || this.disabledVAT) {
      this.airlineVatNumber.disable();
    }

    if (this.defaultDisabled || this.disableContact) {
      this.airlineContact.contactFormModelGroup.disable();
    }
  }

  createFormControls() {
    this.airlineCode = new FormControl('',
      {
        validators: [
          Validators.pattern(GLOBALS.PATTERNS.AIRLINE),
          Validators.required,
          Validators.minLength(3)
        ],
        updateOn: 'blur'
      }
    );
    this.airlineRegistrationNumber = new FormControl('');
    this.airlineVatNumber = new FormControl('');
    this.airlineRemark = new FormControl('');
  }

  createFormGroups() {
    this.airlineContact = new ContactFormModel();
  }

  createForm() {
    this.airlineFormModelGroup = new FormGroup({
      airlineCode: this.airlineCode,
      airlineRegistrationNumber: this.airlineRegistrationNumber,
      airlineVatNumber: this.airlineVatNumber,
      airlineRemark: this.airlineRemark,
      airlineContact: this.airlineContact.contactFormModelGroup
    });
  }
}
