import { Component, HostListener } from '@angular/core';

import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { Pill } from '../../shared/models/pill.model';
import { AmountRefundFormModel } from './models/amount-refund-form.model';
import { BasicInfoRefundFormModel } from './models/basic-info-refund-form.model';
import { DetailsRefundFormModel } from './models/details-refund-form.model';
import { FormOfPaymentRefundFormModel } from './models/form-of-payment-refund-form.model';
import { RefundConfigurationService } from './services/refund-configuration.service';
import { RefundConfiguration } from './models/refund-configuration.model';

@Component({
  selector: 'bspl-refund',
  templateUrl: './refund.component.html',
  styleUrls: ['./refund.component.scss']
})
export class RefundComponent {

  private refundConfiguration;
  private user: User;
  private elementsResumeBar: Object[];
  private pills: Pill[];

  modelView = null;
  url = null;

  private isSticky = false;

  private id_refund = 'refund-master-container';

  private basicInfoRefundFormModel: BasicInfoRefundFormModel;
  private detailsRefundFormModel: DetailsRefundFormModel;
  private formOfPaymentRefundFormModel: FormOfPaymentRefundFormModel;
  private amountRefundFormModel: AmountRefundFormModel;

  private request: Object;

  constructor(
    private userService: UserService,
    private refundConfigurationService: RefundConfigurationService
  ) {
    this.elementsResumeBar = this._initializeResumeBar();
    this.pills = this._initializePills();
    userService.getUser().subscribe(data => this.user = data);
    this.refundConfigurationService.changeConfigurationByISO(this.user.isoc);
  }

  onReturnBasicInfo(basicInfoRefundFormModel: BasicInfoRefundFormModel) {
    this.basicInfoRefundFormModel = basicInfoRefundFormModel;
    this.buildRequest();
  }

  onReturnDetails(detailsRefundFormModel: DetailsRefundFormModel) {
    this.detailsRefundFormModel = detailsRefundFormModel;
  }

  onReturnFormOfPayment(
    formOfPaymentRefundFormModel: FormOfPaymentRefundFormModel
  ) {
    this.formOfPaymentRefundFormModel = formOfPaymentRefundFormModel;
  }

  onReturnAmount(amountRefundFormModel: AmountRefundFormModel) {
    this.amountRefundFormModel = amountRefundFormModel;
  }

  buildRequest() {
    // console.log(this.basicInfoRefundFormModel.basicInfoRefundGroup.controls);
    /*
    this.request = Object.assign(
      this.basicInfoRefundFormModel.basicInfoRefundGroup.getRawValue()
    );
    */
  }

  checkClassSticky() {
    return this.isSticky ? 'sticky' : '';
  }

  checkSticky() {
    return this.isSticky;
  }

  onReturnRefreshPills(pills: Pill[]) {
    this.pills = pills;
  }

  collapsablePill($event, index: number) {
    this.pills[index].collapsable = $event;
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isSticky =
      window.pageYOffset >=
      document.getElementById(this.id_refund).offsetTop;
  }

  private _initializeResumeBar() {
    return (this.elementsResumeBar = [
      { title: 'REFUNDS.RESUME.status', value: null },
      { title: 'REFUNDS.RESUME.bsp', value: null },
      { title: 'REFUNDS.RESUME.airline_code', value: null },
      { title: 'REFUNDS.RESUME.agent_code', value: null },
      { title: 'REFUNDS.RESUME.refund_number', value: null },
      { title: 'REFUNDS.RESUME.issue_date', value: null },
      { title: 'REFUNDS.RESUME.refund_passenger', value: null },
      { title: 'REFUNDS.RESUME.total', value: '0,000,000.00 EUR' }
    ]);
  }

  private _initializePills() {
    return (this.pills = [
      new Pill('REFUNDS.BASIC_INFO.title', 'REFUNDS.BASIC_INFO.title'),
      new Pill('REFUNDS.DETAILS.title', 'REFUNDS.DETAILS.title'),
      new Pill(
        'REFUNDS.FORM_OF_PAYMENT.title',
        'REFUNDS.FORM_OF_PAYMENT.title'
      ),
      new Pill('REFUNDS.AMOUNT.title', 'REFUNDS.AMOUNT.title')
    ]);
  }
}
