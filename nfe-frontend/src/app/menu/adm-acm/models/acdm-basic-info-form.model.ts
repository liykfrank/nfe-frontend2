import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { AgentFormModel } from './../../../shared/components/agent/models/agent-form-model';
import { AirlineFormModel } from './../../../shared/components/airline/models/airline-form.model';

export class AcdmBasicInfoFormModel extends ReactiveFormHandlerModel {
  basicInfoModelGroup: FormGroup;

  agent: AgentFormModel;
  airline: AirlineFormModel;

  constructor() {
    super();
  }

  createFormControls() {}

  createFormGroups() {
    this.airline = new AirlineFormModel();
    this.agent = new AgentFormModel(false, false, false, true);
  }

  createForm() {
    this.basicInfoModelGroup = new FormGroup({
      id: new FormControl(''),
      ticketDocumentNumber: new FormControl(''),
      isoCountryCode: new FormControl('', [Validators.required]),

      billingPeriod: new FormControl('', [Validators.required]),
      agent: this.agent.agentFormModelGroup,

      transactionCode: new FormControl('', [Validators.required]),
      airline: this.airline.airlineFormModelGroup,
      concernsIndicator: new FormControl('', [Validators.required]),
      currency: new FormGroup({
        code: new FormControl('', [Validators.required]),
        decimals: new FormControl('', [Validators.required]),
      }),
      taxOnCommissionType: new FormControl(''),
      statisticalCode: new FormControl('', [Validators.required]),

      netReporting: new FormControl(false)
    });
  }

  changeAgent(disabledVAT: boolean, disabledReg: boolean) {
    this.agent.agentFormModelGroup.reset();

    if (disabledVAT) {
      this.agent.agentVatNumber.enable();
    } else {
      this.agent.agentVatNumber.disable();
    }

    if (disabledReg) {
      this.agent.agentRegistrationNumber.enable();
    } else {
      this.agent.agentRegistrationNumber.disable();
    }
  }

  changeAirline(disabledVAT: boolean, disabledReg: boolean) {
    this.airline.airlineFormModelGroup.reset();

    if (disabledVAT) {
      this.airline.airlineVatNumber.enable();
    } else {
      this.airline.airlineVatNumber.disable();
    }

    if (disabledReg) {
      this.airline.airlineRegistrationNumber.enable();
    } else {
      this.airline.airlineRegistrationNumber.disable();
    }
  }

}
