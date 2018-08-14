import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ReactiveFormHandlerModel } from '../../../shared/base/reactive-form-handler-model';
import { AgentFormModel } from './../../../shared/components/agent/models/agent-form-model';
import { AirlineFormModel } from './../../../shared/components/airline/models/airline-form.model';

export class AcdmBasicInfoFormModel extends ReactiveFormHandlerModel {
  private _basicInfoModelGroup: FormGroup;

  constructor() {
    super();
  }

  createFormControls() {}

  createFormGroups() {
    this._basicInfoModelGroup = this._newBasicInfoForm();
  }

  createForm() {}

  get basicInfoFormModelGroup(): FormGroup {
    if (!this._basicInfoModelGroup) {
      this._basicInfoModelGroup = this._newBasicInfoForm();
    }
    return this._basicInfoModelGroup;
  }

  private _newBasicInfoForm() {
    return new FormGroup({
      id: new FormControl(''),
      ticketDocumentNumber: new FormControl(''),
      isoCountryCode: new FormControl('', [Validators.required]),

      billingPeriod: new FormControl('', [Validators.required]),
      agent: new AgentFormModel().agentFormModelGroup,

      transactionCode: new FormControl('', [Validators.required]),
      airline: new AirlineFormModel().airlineFormModelGroup,
      concernsIndicator: new FormControl('', [Validators.required]),
      currency: new FormGroup({
        code: new FormControl('', [Validators.required]),
        decimals: new FormControl('', [Validators.required]),
      }),
      taxOnCommissionType: new FormControl('', [Validators.required]),
      statisticalCode: new FormControl('', [Validators.required, Validators.pattern('[DI][A-Z]{2}$')]),

      netReporting: new FormControl(false)
    });
  }
}
