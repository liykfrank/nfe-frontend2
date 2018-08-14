import { Component, Input } from '@angular/core';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { DetailsRefundFormModel } from '../../models/details-refund-form.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { User } from '../../../../core/models/user.model';
import { UserService } from '../../../../core/services/user.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'bspl-details-refund',
  templateUrl: './details-refund.component.html',
  styleUrls: ['./details-refund.component.scss']
})
export class DetailsRefundComponent extends ReactiveFormHandler {

  public refundConfiguration: RefundConfiguration;
  public user: User;
  public countCuponsSelected: number;
  public detailsRefundFormModel: DetailsRefundFormModel = new DetailsRefundFormModel();

  constructor(private _refundConfigurationService: RefundConfigurationService,
              private _userService: UserService) {
    super();
    this.subscribe(this.detailsRefundFormModel.detailsRefundGroup);

    this._refundConfigurationService.getConfiguration().subscribe(config => this.refundConfiguration = config);
    this._userService.getUser().subscribe(data => this.user = data);
    this._refundConfigurationService.getCountCuponsState().subscribe(count => this.countCuponsSelected = count );

  }

  showHideMaxCNJ(pos: number): boolean {
    let ret = pos == this.detailsRefundFormModel.getFormArray().length - 1;
    ret =
      ret &&
      (this.refundConfiguration.maxConjunctions == -1 ||
        this.detailsRefundFormModel.getFormArray().length <
          this.refundConfiguration.maxConjunctions);
    return ret;
  }

  onSelectorChange(value) {
    const newValue = this.detailsRefundFormModel.issueReason.value + value + '\n';
    this.detailsRefundFormModel.issueReason.setValue(newValue);
  }

  onChangeCheckCupon(value) {
    (value ) ?  this._refundConfigurationService.setCountCuponsState(1) :  this._refundConfigurationService.setCountCuponsState(-1);
  }

  onRemoveAllCuponsFrom(cnj: FormControl) {
    let count = 0;
    if (cnj.get('relatedTicketCoupon1').value) {
      count++;
    } if (cnj.get('relatedTicketCoupon2').value) {
      count++;
    } if (cnj.get('relatedTicketCoupon3').value) {
      count++;
    } if (cnj.get('relatedTicketCoupon4').value) {
      count++;
    }
    this._refundConfigurationService.setCountCuponsState(-count);
  }


}
