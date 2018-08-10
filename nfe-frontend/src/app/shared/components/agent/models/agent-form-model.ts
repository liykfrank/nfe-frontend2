import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';
import { ContactFormModel } from './../../contact/models/contact-form-model';
import { FormControl, FormGroup, Validators } from '@angular/forms';

export class AgentFormModel  extends ReactiveFormHandlerModel {

  agentFormModelGroup: FormGroup;
  agentContact: FormGroup;

  agentCode: FormControl; // field agent code
  agentControlDigit: FormControl; // field last digit in iatacode
  agentRegistrationNumber: FormControl; // field company registration number
  agentVatNumber: FormControl; // field agent vat number

  constructor() {
    super();
  }

  createFormControls() {
    this.agentCode = new FormControl('', [Validators.required]);
    this.agentControlDigit = new FormControl('', [Validators.required]);
    this.agentRegistrationNumber = new FormControl('', [Validators.required]);
    this.agentVatNumber = new FormControl('', [Validators.required]);
   }

  createFormGroups() {
    this.agentContact = new ContactFormModel().contactFormModelGroup;
  }

  createForm() {
    this.agentFormModelGroup = new FormGroup({
      agentCode: this.agentCode,
      agentControlDigit: this.agentControlDigit,
      agentRegistrationNumber: this.agentRegistrationNumber,
      agentVatNumber: this.agentVatNumber,
      agentContact: this.agentContact
    });
  }

}
