import { AlertModel } from './../../core/models/alert.model';
import { CurrencyService } from './../../shared/components/currency/services/currency.service';
import { environment } from './../../../environments/environment';
import { Component, HostListener, OnInit } from '@angular/core';
import { FormGroup, AbstractControl, FormArray } from '@angular/forms';

import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { Pill } from '../../shared/models/pill.model';
import { UtilsService } from '../../shared/services/utils.service';
import { Refund } from './models/api/refund.model';
import { BasicInfoRefundFormModel } from './models/basic-info-refund-form.model';
import { RefundConfigurationService } from './services/refund-configuration.service';
import { RefundIndirectService } from './services/refund-indirect.service';
import { ScreenType } from '../../shared/enums/screen-type.enum';
import { AbstractComponent } from '../../shared/base/abstract-component';
import { TranslationService } from 'angular-l10n';
import { DatePipe } from '@angular/common';
import { DecimalsFormatterPipe } from '../../shared/pipes/decimals-formatter.pipe';
import { AlertsService } from '../../core/services/alerts.service';
import { AlertType } from '../../core/enums/alert-type.enum';
import { RefundAmount } from './models/api/refund-amount.model';
import { RefundFormOfPaymentAmounts } from './models/api/refund-form-of-payment-amounts';

@Component({
  selector: 'bspl-refund',
  templateUrl: './refund.component.html',
  styleUrls: ['./refund.component.scss']
})
export class RefundComponent extends AbstractComponent implements OnInit{

  url = environment.basePath + environment.api.refund.refund_indirect;
  id_refund_model;
  screen = ScreenType.CREATE;

  private refundConfiguration;
  private user: User;
  elementsResumeBar: Object[];
  pills: Pill[];

  modelView = null;

  private isSticky = false;

  id_refund = 'refund-master-container';

  basicInfoRefundFormModel: FormGroup = new BasicInfoRefundFormModel().basicInfoRefundGroup;
  private detailsRefundFormModel: FormGroup;
  private formOfPaymentRefundFormModel: FormGroup;
  private amountRefundFormModel: FormGroup;

  private showMoreDetails = false;
  private currency;

  constructor(
    private userService: UserService,
    private refundConfigurationService: RefundConfigurationService,
    private _refundIndirectService: RefundIndirectService,
    private _translationService: TranslationService,
    private _utilsService: UtilsService,
    private _alertService: AlertsService,
    private _currencyService: CurrencyService
  ) {
    super();
    this.elementsResumeBar = this._initializeResumeBar();
    this.pills = this._initializePills();
    userService.getUser().subscribe(data => (this.user = data));
    this.refundConfigurationService.changeConfigurationByISO(this.user.isoc);
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.basicInfoRefundFormModel.valueChanges.subscribe(data => {

        (this.elementsResumeBar[1] as any).value = this._getBSP();
        (this.elementsResumeBar[2] as any).value = this._getAirlineCode();
        (this.elementsResumeBar[3] as any).value = this._getAgentCode();
      })
    );

    this._currencyService.getCurrencyState().subscribe(data => this.currency = data);
  }

  onReturnDetails(detailsRefundFormModel: FormGroup) {
    this.detailsRefundFormModel = detailsRefundFormModel;
  }

  onReturnFormOfPayment(formOfPaymentRefundFormModel: FormGroup) {
    this.formOfPaymentRefundFormModel = formOfPaymentRefundFormModel;

    (this.elementsResumeBar[6] as any).value = this._getAmount();
  }

  onReturnAmount(amountRefundFormModel: FormGroup) {
    this.amountRefundFormModel = amountRefundFormModel;
  }

  onClickMoreDetails(event) {
    this.showMoreDetails = event;
  }

  onReturnSave() {
    const refund = this._buildRequestRefundIndirect();
    refund.status = 'DRAFT';
    this._postRefundIndirect(refund);
  }

  onReturnIssue() {
    const testBasicInfo = this.basicInfoRefundFormModel ? this.basicInfoRefundFormModel.valid : true;
    const testDetails = this.detailsRefundFormModel ? this.detailsRefundFormModel.valid : true;
    const testFOP = this.formOfPaymentRefundFormModel ? this.formOfPaymentRefundFormModel.valid : true;
    const testAmount = this.amountRefundFormModel ? this.amountRefundFormModel.valid : true;

    if (!testBasicInfo || !testDetails || !testFOP || !testAmount) {

      const forms = [
        this.basicInfoRefundFormModel,
        this.detailsRefundFormModel,
        this.formOfPaymentRefundFormModel,
        this.amountRefundFormModel
      ]

      this._utilsService.touchAllForms(forms.filter(x => x != null));

      this._alertService.setAlertTranslate('error', 'REFUNDS.error', AlertType.ERROR);
      return;
    }

    if ((this.formOfPaymentRefundFormModel.get('formOfPaymentAmounts') as FormArray).length == 0) {
      this._alertService.setAlertTranslate('error', 'REFUNDS.error_min_fop', AlertType.ERROR);
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
    return this.isSticky && !this.showMoreDetails;
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
      { title: 'REFUNDS.RESUME.status', value: this._getStatus() },
      { title: 'REFUNDS.RESUME.bsp', value: this._getBSP() },
      { title: 'REFUNDS.RESUME.airline_code', value: this._getAirlineCode() },
      { title: 'REFUNDS.RESUME.agent_code', value: this._getAgentCode() },
      { title: 'REFUNDS.RESUME.refund_number', value: null },
      { title: 'REFUNDS.RESUME.issue_date', value: new DatePipe('en').transform(new Date(), 'dd/MM/yyyy') },
      { title: 'REFUNDS.RESUME.refund_passenger', value: '0,000,000.00 EUR' }
    ]);
  }

  private _getStatus() {
    let ret;

    if (this.screen == ScreenType.CREATE) {
      ret = this._translationService.translate('STATUS.NEW');
    } else if (this.screen == ScreenType.DETAIL) {
      ret = this._translationService.translate('STATUS.DETAIL');
    } else {
      ret = '-';
    }

    return ret;
  }

  private _getBSP() {
    const bsp = this.basicInfoRefundFormModel
      ? this.basicInfoRefundFormModel.get('isoCountryCode')
      : null;
    let ret;

    if (bsp && bsp.value.length > 0) {
      ret = bsp.value;
    } else {
      ret = '-';
    }

    return ret;
  }

  private _getAirlineCode() {
    let ret;

    if (this.basicInfoRefundFormModel && this.basicInfoRefundFormModel.get('airline')) {
      const airline = this.basicInfoRefundFormModel.get('airline').get('airlineCode').value;

      if (airline && airline.length > 0) {
        ret = airline;
      } else {
        ret = '-';
      }
    } else {
      ret = '-';
    }

    return ret;
  }

  private _getAgentCode() {
    let ret;

    if (this.basicInfoRefundFormModel && this.basicInfoRefundFormModel.get('agent')) {
      const agent = this.basicInfoRefundFormModel.get('agent').get('agentCode').value;
      const check = this.basicInfoRefundFormModel
        .get('agent')
        .get('agentControlDigit').value;

      if (agent && agent.length > 0 && check && check.length > 0) {
        ret = agent + check;
      } else {
        ret = '-';
      }
    } else {
      ret = '-';
    }

    return ret;
  }

  private _getAmount() {
    let retCurrency = '-';

    if (this.currency) {
      retCurrency = this.currency.get('name').value;
    }

    const total = this.formOfPaymentRefundFormModel
      ? this.formOfPaymentRefundFormModel.get('totalAmount')
      : null;
    let retTotal = '-';

    if (total && this.currency) {
      retTotal = new DecimalsFormatterPipe().transform(
        total.value,
        this.currency.get('numDecimals').value
      );
    }

    return retTotal + ' ' + retCurrency;
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

    if (refund) {
      this._refundIndirectService.post(refund).subscribe(
        data => {

          const txt = '0'.repeat(10 - data.id.toString().length) + data.id.toString();
          const alert =
            new AlertModel(
              this._translationService.translate('REFUNDS.title'),
              this._translationService.translate('REFUNDS.OK') + ' ' + txt,
              AlertType.INFO);

          this._alertService.setAlert(alert);
          this.id_refund_model = data.id;
          this.screen = ScreenType.DETAIL;
        },
        err => {

          if (err.error.validationErrors) {
            const forms = [
              this.basicInfoRefundFormModel,
              this.detailsRefundFormModel,
              this.formOfPaymentRefundFormModel,
              this.amountRefundFormModel
            ];

            const errors = this._utilsService.setBackErrorsOnForms(
              forms.filter(x => x != null),
              err.error.validationErrors
            );

            console.log(errors);

            let msg = '';
            for (const aux of errors) {
              msg += '\n' + aux.message;
            }

            const alert = new AlertModel(
              this._translationService.translate('error'),
              msg == '' ? this._translationService.translate('REFUNDS.error') : msg,
              AlertType.ERROR)
            this._alertService.setAlert(alert);
          }
        }
      );
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
    refund.conjunctions = this.detailsRefundFormModel.get('conjunctions').value.
                            filter(x => x.relatedTicketDocumentNumber && x.relatedTicketDocumentNumber.length == 10);
    refund.exchange = this.detailsRefundFormModel.get('exchange').value;

    if (refund.exchange) {
      refund.originalIssue = this.detailsRefundFormModel.get(
        'originalIssue'
      ).value;
    } else {
      refund.originalIssue = null;
    }

    // FORM OF PAYMENT
    refund.formOfPaymentAmounts = this._fopFormat(this.formOfPaymentRefundFormModel.get(
      'formOfPaymentAmounts'
    ).value);
    refund.tourCode = this.formOfPaymentRefundFormModel.get('tourCode').value;
    refund.currency.code = this.currency.name;
    refund.currency.decimals = this.currency.numDecimals;
    refund.customerFileReference = this.formOfPaymentRefundFormModel.get(
      'customerFileReference'
    ).value;
    refund.settlementAuthorisationCode = this.formOfPaymentRefundFormModel.get(
      'settlementAuthorisationCode'
    ).value;

    // AMOUNT
    refund.partialRefund = this.amountRefundFormModel.get('partialRefund').value;
    refund.netReporting = this.amountRefundFormModel.get('netRemit').value;
    refund.amounts = this._refundAmount(this.amountRefundFormModel.get('amounts').value);
    const taxArr = this.amountRefundFormModel.get('amounts').get('taxMiscellaneousFees');
    refund.taxMiscellaneousFees = taxArr ? taxArr.value : [];

    return refund;
  }

  private _refundAmount(val: RefundAmount) {
    val.cancellationPenalty = val.cancellationPenalty ? val.cancellationPenalty : 0;
    val.commissionAmount = val.commissionAmount ? val.commissionAmount : 0;
    val.commissionOnCpAndMfAmount = val.commissionOnCpAndMfAmount ? val.commissionOnCpAndMfAmount : 0;
    val.commissionOnCpAndMfRate = val.commissionOnCpAndMfRate ? val.commissionOnCpAndMfRate : 0;
    val.commissionRate = val.commissionRate ? val.commissionRate : 0;
    val.grossFare = val.grossFare ? val.grossFare : 0;
    val.lessGrossFareUsed = val.lessGrossFareUsed ? val.lessGrossFareUsed : 0;
    val.miscellaneousFee = val.miscellaneousFee ? val.miscellaneousFee : 0;
    val.spam = val.spam ? val.spam : 0;
    val.refundToPassenger = val.refundToPassenger ? val.refundToPassenger : 0;
    val.tax = val.tax ? val.tax : 0;
    val.taxOnCancellationPenalty = val.taxOnCancellationPenalty ? val.taxOnCancellationPenalty : 0;
    val.taxOnMiscellaneousFee = val.taxOnMiscellaneousFee ? val.taxOnMiscellaneousFee : 0;

    return val;
  }

  private _fopFormat(fopList: RefundFormOfPaymentAmounts[]) {
    for (const aux of fopList) {
      aux.amount = aux.amount && (aux as any).amount != '' ? aux.amount : 0;
    }

    return fopList;
  }
}
