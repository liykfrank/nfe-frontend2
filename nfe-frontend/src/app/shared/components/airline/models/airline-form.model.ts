import { ReactiveFormHandlerModel } from '../../../base/reactive-form-handler-model';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ContactFormModel } from '../../contact/models/contact-form-model';

export class AirlineFormModel extends ReactiveFormHandlerModel {

  airlineFormModelGroup: FormGroup;
  airlineContact: FormGroup;

  airlineCode: FormControl; // field airline code
  airlineRegistrationNumber: FormControl; // forgot for the moment
  airlineVatNumber: FormControl; // forgot for the moment
  airlineRemark: FormControl; // forgot for the moment

  constructor() {
    super();
  }

  createFormControls() {
    this.airlineCode = new FormControl('', { validators : [Validators.pattern('[A-Z0-9]{3}$')], updateOn: 'blur' } );
    this.airlineRegistrationNumber = new FormControl('', [Validators.required]);
    this.airlineVatNumber = new FormControl('', [Validators.required]);
    this.airlineRemark = new FormControl('');
  }

  createFormGroups() {
    this.airlineContact = new ContactFormModel().contactFormModelGroup;
  }

  createForm() {
    this.airlineFormModelGroup = new FormGroup({
      airlineCode: this.airlineCode,
      airlineRegistrationNumber: this.airlineRegistrationNumber,
      airlineVatNumber: this.airlineVatNumber,
      airlineRemark: this.airlineRemark,
      airlineContact: this.airlineContact
    });
  }

}


