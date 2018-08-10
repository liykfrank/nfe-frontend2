import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { FormGroup, FormControl, FormArray, Validators } from '@angular/forms';
import { OnDestroy } from '@angular/core';

export class BasicInfoRefundFormModel extends ReactiveFormHandlerModel {
    basicInfoRefundGroup: FormGroup;
    agentContact: FormGroup;

    airlineCode: FormControl;                 // field airline code
    airlineRegistrationNumber: FormControl;   // forgot for the moment
    airlineVatNumber: FormControl;            // forgot for the moment
    airlineRemark: FormControl;               // forgot for the moment
    airlineContact: FormGroup;
    agentCode: FormControl;                   // field agent code
    isoCountryCode: FormControl;              // field country code agent
    agentRegistrationNumber: FormControl;     // field company registration number
    agentVatNumber: FormControl;              // field agent vat number

    constructor() {
      super();
    }

    public createFormControls() {
      this.airlineCode = new FormControl('DOM', [Validators.required]);
      this.airlineRegistrationNumber = new FormControl('DOM', [Validators.required]);
      this.airlineVatNumber = new FormControl('DOM', [Validators.required]);
      this.airlineRemark = new FormControl('DOM', [Validators.required]);
      this.agentCode = new FormControl('DOM', [Validators.required]);
      this.isoCountryCode = new FormControl('DOM', [Validators.required]);
      this.agentRegistrationNumber = new FormControl('DOM', [Validators.required]);
      this.agentVatNumber = new FormControl('DOM', [Validators.required]);
    }

    newContact(disable: boolean = false) {
      return new FormGroup({
        contactName: new FormControl({ value: '', disabled: disable }), // field issuing Agent
        email: new FormControl({ value: '', disabled: disable }), // Document number field 3 numbers
        phoneFaxNumber: new FormControl({ value: '', disabled: disable }) // field 11 numbers in Document number
      });
    }

    public createFormGroups() {
      this.airlineContact = this.newContact(true);
      this.agentContact = this.newContact(true);
    }

    public createForm() {
      this.basicInfoRefundGroup = new FormGroup({
        airlineCode: this.airlineCode,                               // field airline code
        airlineRegistrationNumber: this.airlineRegistrationNumber,   // forgot for the moment
        airlineVatNumber: this.airlineVatNumber,                     // forgot for the moment
        airlineRemark: this.airlineRemark,                           // forgot for the moment
        airlineContact: this.airlineContact,
        agentCode: this.agentCode,                                   // field agent code
        isoCountryCode: this.isoCountryCode,                         // field country code agent
        agentRegistrationNumber: this.agentRegistrationNumber,       // field company registration number
        agentVatNumber: this.agentVatNumber,                         // field agent vat number
        agentContact: this.agentContact
      });

    }

}
