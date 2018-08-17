import { Component, Input, OnInit } from '@angular/core';

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
export class DetailsRefundComponent extends ReactiveFormHandler implements OnInit {

  public refundConfiguration: RefundConfiguration;
  public user: User;
  public countCuponsSelected: number;
  public detailsRefundFormModel: DetailsRefundFormModel = new DetailsRefundFormModel();
  public type = EnvironmentType.REFUND_INDIRECT;

  constructor(private _refundConfigurationService: RefundConfigurationService,
              private _userService: UserService) {
    super();
    this.subscribe(this.detailsRefundFormModel.detailsRefundGroup);

    this._refundConfigurationService.getConfiguration().subscribe(config => this.refundConfiguration = config);
    this._userService.getUser().subscribe(data => this.user = data);
    this._refundConfigurationService.getCountCuponsState().subscribe(count => this.countCuponsSelected = count );
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.detailsRefundFormModel.conjunctions.valueChanges.subscribe(data => {
        let i = 0;
        for (const aux of data) {
          let txt = aux.relatedTicketDocumentNumber;
          if (txt && txt.length < 10) {
            txt = '0'.repeat(10 - txt.length) + txt;
            this.detailsRefundFormModel.conjunctions.get(i.toString()).get('relatedTicketDocumentNumber').setValue(txt, {emitEvent: false});
          }
          i++;
        }
      })
    );

    this.subscriptions.push(
      this.detailsRefundFormModel.detailsRefundGroup.get('relatedDocument').get('relatedTicketDocumentNumber').valueChanges.subscribe(data => {
        if (data && data.length < 10) {
          let txt = '0'.repeat(10 - data.length) + data;
          this.detailsRefundFormModel.detailsRefundGroup.get('relatedDocument').get('relatedTicketDocumentNumber').setValue(txt, {emitEvent: false});
        }
      })
    );
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
