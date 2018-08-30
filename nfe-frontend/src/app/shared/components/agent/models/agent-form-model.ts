import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';
import { GLOBALS } from '../../../constants/globals';
import { ContactFormModel } from './../../contact/models/contact-form-model';

export class AgentFormModel extends ReactiveFormHandlerModel {
  agentFormModelGroup: FormGroup;
  agentContact: ContactFormModel;

  agentCode: FormControl; // field agent code
  agentControlDigit: FormControl; // field last digit in iatacode
  agentRegistrationNumber: FormControl; // field company registration number
  agentVatNumber: FormControl; // field agent vat number

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
      this.agentCode.disable();
      this.agentControlDigit.disable();
    }

    if (this.defaultDisabled || this.disabledReg) {
      this.agentRegistrationNumber.disable();
    }

    if (this.defaultDisabled || this.disabledVAT) {
      this.agentVatNumber.disable();
    }

    if (this.defaultDisabled || this.disableContact) {
      this.agentContact.contactFormModelGroup.disable();
    }
  }

  createFormControls() {
    this.agentCode = new FormControl('', {
      validators: [
        Validators.pattern(GLOBALS.PATTERNS.AGENT),
        Validators.required,
        Validators.minLength(7)
      ],
      updateOn: 'blur'
    });
    this.agentControlDigit = new FormControl('', [Validators.required]);
    this.agentRegistrationNumber = new FormControl('');
    this.agentVatNumber = new FormControl('');
  }

  createFormGroups() {
    this.agentContact = new ContactFormModel();
  }

  createForm() {
    this.agentFormModelGroup = new FormGroup({
      agentCode: this.agentCode,
      agentControlDigit: this.agentControlDigit,
      agentRegistrationNumber: this.agentRegistrationNumber,
      agentVatNumber: this.agentVatNumber,
      agentContact: this.agentContact.contactFormModelGroup
    });
  }
}
