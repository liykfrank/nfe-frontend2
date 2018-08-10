import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { User } from '../../../../core/models/user.model';
import { UserService } from '../../../../core/services/user.service';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { BasicInfoRefundFormModel } from '../../models/basic-info-refund-form.model';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { RefundIsuePermissionService } from '../../services/refund-isue-permission.service';

@Component({
  selector: 'bspl-basic-info-refund',
  templateUrl: './basic-info-refund.component.html',
  styleUrls: ['./basic-info-refund.component.scss']
})
export class BasicInfoRefundComponent extends ReactiveFormHandler implements OnInit {

  public refundConfiguration: RefundConfiguration;
  public user: User;
  private basicInfoRefundFormModel: BasicInfoRefundFormModel = new BasicInfoRefundFormModel();
  private validDATA = false;

  constructor(
    private _refundConfigurationService: RefundConfigurationService,
    private _refundIsuePermissionService: RefundIsuePermissionService,
    private _userService: UserService
  ) {
    super();
    this.subscribe(this.basicInfoRefundFormModel.basicInfoRefundGroup);

    this.subscriptions.push(
      this._refundConfigurationService
        .getConfiguration()
        .subscribe(config => (this.refundConfiguration = config))
    );
    this.subscriptions.push(
      this._userService.getUser().subscribe(data => (this.user = data))
    );
  }

  ngOnInit() {}

  onReturnAgentFormModel(agentFormGroup: FormGroup) {
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('agentCode')
      .setValue(
        agentFormGroup.controls.agentCode.value +
          agentFormGroup.controls.agentControlDigit.value
      );
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('agentRegistrationNumber')
      .setValue(agentFormGroup.controls.agentRegistrationNumber.value);
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('agentVatNumber')
      .setValue(agentFormGroup.controls.agentVatNumber.value);
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('agentContact')
      .setValue(agentFormGroup.controls.agentContact.value);
  }

  onReturnAirlineFormModel(airlineFormGroup: FormGroup) {
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('airlineCode')
      .setValue(airlineFormGroup.controls.airlineCode.value);
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('airlineRegistrationNumber')
      .setValue(airlineFormGroup.controls.airlineRegistrationNumber.value);
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('airlineVatNumber')
      .setValue(airlineFormGroup.controls.airlineVatNumber.value);
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('airlineRemark')
      .setValue(airlineFormGroup.controls.airlineRemark.value);
    this.basicInfoRefundFormModel.basicInfoRefundGroup
      .get('airlineContact')
      .setValue(airlineFormGroup.controls.airlineContact.value);
  }

  onChangeAirline() {
    this._refundIsuePermissionService
      .getRefundIssuePermission('TEST_ISO', 'TEST_AIRLINE', 'TEST_AGENT')
      .subscribe(
        data => (this.validDATA = true),
        err => (this.validDATA = false)
      );
  }
}
