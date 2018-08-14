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
    this.airline = new AirlineFormModel();
    this.agent = new AgentFormModel(true);
  }

  public createForm() {
    this.basicInfoRefundGroup = new FormGroup({
      agent: this.agent.agentFormModelGroup,
      airline: this.airline.airlineFormModelGroup,
      isoCountryCode: new FormControl('', [Validators.required])
    });
  }
}
