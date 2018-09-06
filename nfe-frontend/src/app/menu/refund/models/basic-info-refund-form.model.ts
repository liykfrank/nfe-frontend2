import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { AirlineFormModel } from '../../../shared/components/airline/models/airline-form.model';
import { AgentFormModel } from './../../../shared/components/agent/models/agent-form-model';

export class BasicInfoRefundFormModel extends ReactiveFormHandlerModel {

  airline: AirlineFormModel;
  agent: AgentFormModel;
  basicInfoRefundGroup: FormGroup;

  constructor() {
    super();
  }

  public createFormControls() {}

  public createFormGroups() {
    this.airline = new AirlineFormModel(false, false, false, true);
    this.agent = new AgentFormModel(true);
  }

  public createForm() {
    this.basicInfoRefundGroup = new FormGroup({
      airline: this.airline.airlineFormModelGroup,
      agent: this.agent.agentFormModelGroup,
      isoCountryCode: new FormControl('', [Validators.required])
    });
  }

  changeAgent(disabledVAT: boolean, disabledReg: boolean) {
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
