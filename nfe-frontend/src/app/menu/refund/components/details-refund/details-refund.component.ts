import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { User } from '../../../../core/models/user.model';
import { UserService } from '../../../../core/services/user.service';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { GLOBALS } from './../../../../shared/constants/globals';
import { DetailsRefundFormModel } from './../../models/details-refund-form.model';

@Component({
  selector: 'bspl-details-refund',
  templateUrl: './details-refund.component.html',
  styleUrls: ['./details-refund.component.scss']
})
export class DetailsRefundComponent
  extends ReactiveFormHandler<DetailsRefundFormModel>
  implements OnInit {
  public detailsRefundGroup: FormGroup;

  public refundConfiguration: RefundConfiguration;
  public user: User;
  public countCuponsSelected: number;
  public type = EnvironmentType.REFUND_INDIRECT;

  public PT_WAIVERCODE = GLOBALS.HTML_PATTERN.WAIVER_CODE;
  public PT_TEXT = GLOBALS.HTML_PATTERN.TEXT;
  public PT_NUMBER = GLOBALS.HTML_PATTERN.NUMERIC;
  public PT_AIRLINE = GLOBALS.HTML_PATTERN.ALPHANUMERIC_UPPERCASE;
  public PT_PASSSENGER = GLOBALS.HTML_PATTERN.PASSSENGER;

  constructor(
    private _refundConfigurationService: RefundConfigurationService,
    private _userService: UserService
  ) {
    super();
  }

  ngOnInit(): void {
    this.detailsRefundGroup = this.model.detailsRefundGroup;

    this.subscriptions.push(
      this._refundConfigurationService
        .getConfiguration()
        .subscribe(config => (this.refundConfiguration = config))
    );

    this._userService.getUser().subscribe(data => (this.user = data));

    this.subscriptions.push(
      this._refundConfigurationService
        .getCountCuponsState()
        .subscribe(count => (this.countCuponsSelected = count))
    );

    this.subscriptions.push(
      this.model.conjunctions.valueChanges.subscribe(data => {
        let i = 0;
        for (const aux of data) {
          let txt = aux.relatedTicketDocumentNumber;

          if (txt && txt.length < 10) {
            txt = '0'.repeat(10 - txt.length) + txt;
            this.model.conjunctions
              .get(i.toString())
              .get('relatedTicketDocumentNumber')
              .setValue(txt, { emitEvent: false });
          }
          i++;
        }

        this._countCoupons();
      })
    );

    this.subscriptions.push(
      this.model.relatedDocument
        .get('relatedTicketDocumentNumber')
        .valueChanges
        .filter(() => this.model.relatedDocument.get('relatedTicketDocumentNumber').value )
        .subscribe(data => {

          if ( data && data.length < 10) {
            const txt = '0'.repeat(10 - data.length) + data;
            this.model.relatedDocument
              .get('relatedTicketDocumentNumber')
              .setValue(txt, { emitEvent: false });
          }

          this._countCoupons();
        })
    );

    this.subscriptions.push(
      this.model.exchange.valueChanges.subscribe(value => {
        this._enableOriginalIssueDetails(value);
      })
    );
  }

  showTrashCNG() {
    return this.model.conjunctions.controls.length != 1;
  }

  showAddCNJ(pos: number) {
    const retCNJ =
      this.refundConfiguration.maxConjunctions == -1 ||
      this.model.conjunctions.controls.length <
        this.refundConfiguration.maxConjunctions;

    return retCNJ && pos == this.model.conjunctions.controls.length - 1;
  }

  disableAddCNJ() {
    const list = this.model.conjunctions.controls.filter(
      x => x.get('relatedTicketDocumentNumber').value == ''
    );
    return list.length > 0;
  }

  onSelectorChange(value) {
    const newValue = this.model.issueReason.value + value + '\n';
    this.model.issueReason.setValue(newValue);
  }

  removeCNJ(pos: number) {
    this.model.conjunctions.removeAt(pos);
  }

  addCNJ() {
    this.model.conjunctions.push(this.model.newCNJ());
  }

  showIssueReasonError() {
    const elem = this.detailsRefundGroup.get('issueReason');

    return (elem.touched || elem.dirty) && elem.errors;
  }

  private _enableOriginalIssueDetails(value: boolean) {
    if (value) {
      this.detailsRefundGroup.controls.originalIssue.enable();
    } else {
      this.detailsRefundGroup.controls.originalIssue.disable();
      this.detailsRefundGroup.controls.originalIssue.reset();
    }
  }

  private _countCoupons() {
    let count = 0;

    for (const aux of this.model.conjunctions.controls) {
      if (aux.get('relatedTicketDocumentNumber').value.length > 0) {
        if (aux.get('relatedTicketCoupon1').value) {
          count++;
        }
        if (aux.get('relatedTicketCoupon2').value) {
          count++;
        }
        if (aux.get('relatedTicketCoupon3').value) {
          count++;
        }
        if (aux.get('relatedTicketCoupon4').value) {
          count++;
        }
      }
    }

    const relatedDocument = this.model.relatedDocument;
    if (relatedDocument.get('relatedTicketDocumentNumber').value.length > 0) {
      if (relatedDocument.get('relatedTicketCoupon1').value) {
        count++;
      }
      if (relatedDocument.get('relatedTicketCoupon2').value) {
        count++;
      }
      if (relatedDocument.get('relatedTicketCoupon3').value) {
        count++;
      }
      if (relatedDocument.get('relatedTicketCoupon4').value) {
        count++;
      }
    }

    this._refundConfigurationService.setCountCuponsState(count);
  }
}
