import { BasicInfoRefundFormModel } from './../../models/basic-info-refund-form.model';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { User } from '../../../../core/models/user.model';
import { UserService } from '../../../../core/services/user.service';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { RefundIsuePermissionService } from '../../services/refund-isue-permission.service';

@Component({
  selector: 'bspl-basic-info-refund',
  templateUrl: './basic-info-refund.component.html',
  styleUrls: ['./basic-info-refund.component.scss']
})

export class BasicInfoRefundComponent extends ReactiveFormHandler<BasicInfoRefundFormModel> implements OnInit {

  public refundConfiguration: RefundConfiguration;
  public user: User;

  private validDATA = false;

  constructor(
    private _refundConfigurationService: RefundConfigurationService,
    private _refundIsuePermissionService: RefundIsuePermissionService,
    private _userService: UserService
  ) {
    super();
  }

  ngOnInit() {

    this.subscriptions.push(
      this._refundConfigurationService.getConfiguration().subscribe(config => {
        this.refundConfiguration = config;
        this.model.basicInfoRefundGroup
          .get('isoCountryCode')
          .setValue(config.isoCountryCode);
      })
    );

    this.subscriptions.push(
      this._userService.getUser().subscribe(data => (this.user = data))
    );
  }

  private _onChangeAirline() {
    this._refundIsuePermissionService
      .getRefundIssuePermission('TEST_ISO', 'TEST_AIRLINE', 'TEST_AGENT')
      .subscribe(
        data => (this.validDATA = true),
        err => (this.validDATA = false)
      );
  }
}
