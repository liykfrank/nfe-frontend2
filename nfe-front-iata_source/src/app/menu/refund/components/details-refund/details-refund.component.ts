import { Component, Input } from '@angular/core';

import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { Alignment } from '../../../../shared/enums/alignment.enum';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { DetailsRefundFormModel } from '../../models/details-refund-form.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';

@Component({
  selector: 'bspl-details-refund',
  templateUrl: './details-refund.component.html',
  styleUrls: ['./details-refund.component.scss']
})
export class DetailsRefundComponent extends ReactiveFormHandler {

  refundConfiguration: RefundConfiguration;

  public detailsRefundFormModel: DetailsRefundFormModel = new DetailsRefundFormModel();
  public reasonsAlignment = Alignment.HORIZONTAL;
  public type = EnvironmentType.REFUND_INDIRECT;

  constructor(private refundConfigurationService: RefundConfigurationService) {
    super();
    this.subscriptions.push(
      this.refundConfigurationService
        .getConfiguration()
        .subscribe(config => (this.refundConfiguration = config))
    );
    this.subscribe(this.detailsRefundFormModel.detailsRefundGroup);
  }

}
