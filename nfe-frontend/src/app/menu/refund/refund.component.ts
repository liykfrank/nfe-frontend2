import { Component, HostListener } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';

import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { Pill } from '../../shared/models/pill.model';
import { UtilsService } from '../../shared/services/utils.service';
import { Refund } from './models/api/refund.model';
import { BasicInfoRefundFormModel } from './models/basic-info-refund-form.model';
import { RefundConfigurationService } from './services/refund-configuration.service';
import { RefundIndirectService } from './services/refund-indirect.service';

@Component({
  selector: 'bspl-refund',
  templateUrl: './refund.component.html',
  styleUrls: ['./refund.component.scss']
})
export class RefundComponent {
  private refundConfiguration;
  private user: User;
  elementsResumeBar: Object[];
  pills: Pill[];

  modelView = null;
  url = null;

  private isSticky = false;

  id_refund = 'refund-master-container';

  basicInfoRefundFormModel: FormGroup = new BasicInfoRefundFormModel()
    .basicInfoRefundGroup;
  private detailsRefundFormModel: FormGroup;
  private formOfPaymentRefundFormModel: FormGroup;
  private amountRefundFormModel: FormGroup;

  private request: Object;

  constructor(
    private userService: UserService,
    private refundConfigurationService: RefundConfigurationService,
    private _refundIndirectService: RefundIndirectService,
    private _utilsService: UtilsService
  ) {
    this.elementsResumeBar = this._initializeResumeBar();
    this.pills = this._initializePills();
    userService.getUser().subscribe(data => (this.user = data));
    this.refundConfigurationService.changeConfigurationByISO(this.user.isoc);
  }

  onReturnBasicInfo(basicInfoRefundFormModel: FormGroup) {
    this.basicInfoRefundFormModel = basicInfoRefundFormModel;
  }

  onReturnDetails(detailsRefundFormModel: FormGroup) {
    this.detailsRefundFormModel = detailsRefundFormModel;
  }

  onReturnFormOfPayment(formOfPaymentRefundFormModel: FormGroup) {
    this.formOfPaymentRefundFormModel = formOfPaymentRefundFormModel;
  }

  onReturnAmount(amountRefundFormModel: FormGroup) {
    this.amountRefundFormModel = amountRefundFormModel;
  }

  onReturnSave() {
    const refund = this._buildRequestRefundIndirect();
    refund.status = 'DRAFT';
    this._postRefundIndirect(refund);
  }

  onReturnIssue() {
/*
    if (
      !this.basicInfoRefundFormModel.valid ||
      !this.detailsRefundFormModel.valid ||
      !this.formOfPaymentRefundFormModel.valid
    ) {
      this.basicInfoRefundFormModel.validator;// = true;
      return null;
    } */
    if (!this.basicInfoRefundFormModel.valid) {
      this.basicInfoRefundFormModel.markAsDirty({onlySelf: false});
      return;
    }


    const refund = this._buildRequestRefundIndirect();
    refund.status = 'PENDING';
    this._postRefundIndirect(refund);
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
      window.pageYOffset >= document.getElementById(this.id_refund).offsetTop;
  }

  private _initializeResumeBar() {
    return (this.elementsResumeBar = [
      { title: 'REFUNDS.RESUME.status', value: null },
      { title: 'REFUNDS.RESUME.bsp', value: null },
      { title: 'REFUNDS.RESUME.airline_code', value: null },
      { title: 'REFUNDS.RESUME.agent_code', value: null },
      { title: 'REFUNDS.RESUME.refund_number', value: null },
      { title: 'REFUNDS.RESUME.issue_date', value: null },
      { title: 'REFUNDS.RESUME.refund_passenger', value: '0,000,000.00 EUR' }
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

  private _postRefundIndirect(refund: Refund) {
    console.log('on post');
    console.log(refund);

    if (refund) {
      this._refundIndirectService.post(refund).subscribe(
        data => {
          console.log('on OK');
          console.log(data);
        },
        err => {
          console.log(err);
          if (err.error.validationErrors) {
            const forms = [
              this.basicInfoRefundFormModel,
              this.detailsRefundFormModel,
              this.formOfPaymentRefundFormModel
              /* ,this.amountRefundFormModel */
            ];

            const errors = this._utilsService.setBackErrorsOnForms(forms, err.error.validationErrors);
            console.log(errors);
          }
        }
      );
    } else {
      console.log('no pasaras');
    }
  }

  private _buildRequestRefundIndirect(): Refund {
    const refund = new Refund();

    // BASIC INFO
    const agent = this.basicInfoRefundFormModel.get('agent');
    refund.agentCode =
      agent.get('agentCode').value + agent.get('agentControlDigit').value;
    refund.agentContact = agent.get('agentContact').value;
    refund.agentRegistrationNumber = agent.get('agentRegistrationNumber').value;
    refund.agentVatNumber = agent.get('agentVatNumber').value;

    const airline = this.basicInfoRefundFormModel.get('airline');
    refund.airlineCode = airline.get('airlineCode').value;
    refund.airlineContact = airline.get('airlineContact').value;
    refund.airlineRegistrationNumber = airline.get(
      'airlineRegistrationNumber'
    ).value;
    refund.airlineVatNumber = airline.get('airlineVatNumber').value;

    // EXTRAS
    refund.isoCountryCode = this.basicInfoRefundFormModel.get(
      'isoCountryCode'
    ).value;
    refund.dateOfIssue = new Date().toISOString();

    // DETAILS
    refund.statisticalCode = this.detailsRefundFormModel.get(
      'statisticalCode'
    ).value;
    refund.passenger = this.detailsRefundFormModel.get('passenger').value;
    refund.issueReason = this.detailsRefundFormModel.get('issueReason').value;
    refund.airlineCodeRelatedDocument = this.detailsRefundFormModel.get(
      'airlineCodeRelatedDocument'
    ).value;
    refund.dateOfIssueRelatedDocument = this.detailsRefundFormModel.get(
      'dateOfIssueRelatedDocument'
    ).value;
    refund.waiverCode = this.detailsRefundFormModel.get('waiverCode').value;
    refund.relatedDocument = this.detailsRefundFormModel.get(
      'relatedDocument'
    ).value;
    refund.conjunctions = this.detailsRefundFormModel.get('conjunctions').value;
    refund.exchange = this.detailsRefundFormModel.get('exchange').value;
    refund.originalIssue = this.detailsRefundFormModel.get(
      'originalIssue'
    ).value;

    // FORM OF PAYMENT
    refund.formOfPaymentAmounts = this.formOfPaymentRefundFormModel.get(
      'formOfPaymentAmounts'
    ).value;
    refund.tourCode = this.formOfPaymentRefundFormModel.get('tourCode').value;
    refund.currency = this.formOfPaymentRefundFormModel.get('currency').value;
    refund.customerFileReference = this.formOfPaymentRefundFormModel.get(
      'customerFileReference'
    ).value;
    refund.settlementAuthorisationCode = this.formOfPaymentRefundFormModel.get(
      'settlementAuthorisationCode'
    ).value;

    // AMOUNT
    // refund.partialRefund = this.amountRefundFormModel.get('partialRefund').value;
    // refund.netReporting = false; //TODO: this.amountRefundFormModel.get('netReporting').value;
    // refund.amounts = this.amountRefundFormModel.get('amounts').value;
    // refund.taxMiscellaneousFees = this.amountRefundFormModel.get('taxMiscellaneousFees').value;
    // }

    return refund;
  }
}
