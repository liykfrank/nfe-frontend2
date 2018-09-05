import { DatePipe } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { TranslationService } from 'angular-l10n';

import { AlertType } from '../../core/enums/alert-type.enum';
import { User } from '../../core/models/user.model';
import { AlertsService } from '../../core/services/alerts.service';
import { UserService } from '../../core/services/user.service';
import { AbstractComponent } from '../../shared/base/abstract-component';
import { ScreenType } from '../../shared/enums/screen-type.enum';
import { Pill } from '../../shared/models/pill.model';
import { DecimalsFormatterPipe } from '../../shared/pipes/decimals-formatter.pipe';
import { UtilsService } from '../../shared/services/utils.service';
import { environment } from './../../../environments/environment';
import { AlertModel } from './../../core/models/alert.model';
import { CurrencyService } from './../../shared/components/currency/services/currency.service';
import { AmountRefundFormModel } from './models/amount-refund-form.model';
import { RefundAmount } from './models/api/refund-amount.model';
import { RefundFormOfPaymentAmounts } from './models/api/refund-form-of-payment-amounts';
import { Refund } from './models/api/refund.model';
import { BasicInfoRefundFormModel } from './models/basic-info-refund-form.model';
import { DetailsRefundFormModel } from './models/details-refund-form.model';
import { FormOfPaymentRefundFormModel } from './models/form-of-payment-refund-form.model';
import { RefundConfigurationService } from './services/refund-configuration.service';
import { RefundIndirectService } from './services/refund-indirect.service';
import { Validators } from '@angular/forms';
import { GLOBALS } from '../../shared/constants/globals';

@Component({
  selector: 'bspl-refund',
  templateUrl: './refund.component.html',
  styleUrls: ['./refund.component.scss']
})
export class RefundComponent extends AbstractComponent implements OnInit {
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

  basicInfoRefundFormModel: BasicInfoRefundFormModel = new BasicInfoRefundFormModel();
  detailsRefundFormModel: DetailsRefundFormModel = new DetailsRefundFormModel();
  formOfPaymentRefundFormModel: FormOfPaymentRefundFormModel = new FormOfPaymentRefundFormModel();
  amountRefundFormModel: AmountRefundFormModel = new AmountRefundFormModel();

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
  }

  ngOnInit(): void {
    this.refundConfigurationService.changeConfigurationByISO(this.user.isoc);

    this.subscriptions.push(
      this.refundConfigurationService.getConfiguration().subscribe(data => {
        this.basicInfoRefundFormModel.changeAgent(
          data.agentVatNumberEnabled,
          data.companyRegistrationNumberEnabled
        );
        this.basicInfoRefundFormModel.changeAirline(
          data.airlineVatNumberEnabled,
          data.companyRegistrationNumberEnabled
        );
      })
    );

    this.subscriptions.push(
      this.basicInfoRefundFormModel.basicInfoRefundGroup.valueChanges.subscribe(
        data => {
          (this.elementsResumeBar[1] as any).value = this._getBSP();

          const agent = this.basicInfoRefundFormModel.agent;
          if (
            agent.agentCode.value.length == 7 &&
            agent.agentControlDigit.value.length == 1
          ) {
            (this.elementsResumeBar[3] as any).value = this._getAgentCode();
          } else {
            (this.elementsResumeBar[3] as any).value = '-';
          }

          const airline = this.basicInfoRefundFormModel.airline;
          if (airline.airlineCode.value.length == 3) {
            (this.elementsResumeBar[2] as any).value = this._getAirlineCode();
          } else {
            (this.elementsResumeBar[2] as any).value = '-';
          }
        }
      )
    );

    this.subscriptions.push(
      this.formOfPaymentRefundFormModel.totalAmount.valueChanges.subscribe(
        () => {
          (this.elementsResumeBar[6] as any).value = this._getAmount();
        }
      )
    );

    this._currencyService
      .getCurrencyState()
      .subscribe(data => (this.currency = data));
  }

  onClickMoreDetails(event) {
    this.showMoreDetails = event;
  }

  onReturnSave() {
    this.detailsRefundFormModel.passenger.setValidators([Validators.pattern(GLOBALS.PATTERNS.PASSSENGER)]);
    this.detailsRefundFormModel.issueReason.setValidators([]);

    const forms = [
      this.basicInfoRefundFormModel.basicInfoRefundGroup,
      this.detailsRefundFormModel.detailsRefundGroup,
      this.formOfPaymentRefundFormModel.formOfPaymentRefundGroup,
      this.amountRefundFormModel.amountRefundGroup
    ];
    this._utilsService.untouchAllForms(forms);

    const refund = this._buildRequestRefundIndirect();
    refund.status = 'DRAFT';
    this._postRefundIndirect(refund);
  }

  onReturnIssue() {
    this.detailsRefundFormModel.passenger.setValidators([Validators.pattern(GLOBALS.PATTERNS.PASSSENGER), Validators.required, Validators.minLength(1)]);
    this.detailsRefundFormModel.issueReason.setValidators([Validators.required, Validators.minLength(1)]);

    const testBasicInfo = this.basicInfoRefundFormModel.basicInfoRefundGroup
      .valid;
    const testDetails = this.detailsRefundFormModel.detailsRefundGroup.valid;
    const testFOP = this.formOfPaymentRefundFormModel.formOfPaymentRefundGroup
      .valid;
    const testAmount = this.amountRefundFormModel.amountRefundGroup.valid;

    if (!testBasicInfo || !testDetails || !testFOP || !testAmount) {
      const forms = [
        this.basicInfoRefundFormModel.basicInfoRefundGroup,
        this.detailsRefundFormModel.detailsRefundGroup,
        this.formOfPaymentRefundFormModel.formOfPaymentRefundGroup,
        this.amountRefundFormModel.amountRefundGroup
      ];

      this._utilsService.touchAllForms(forms);

      this._focusErrorAfterAlert();
      this._alertService.setAlertTranslate(
        'error',
        'REFUNDS.error',
        AlertType.ERROR
      );
      return;
    }

    if (this.formOfPaymentRefundFormModel.formOfPaymentAmounts.length == 0) {
      this._focusErrorAfterAlert();
      this._alertService.setAlertTranslate(
        'error',
        'REFUNDS.error_min_fop',
        AlertType.ERROR
      );
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
      {
        title: 'REFUNDS.RESUME.issue_date',
        value: new DatePipe('en').transform(new Date(), 'dd/MM/yyyy')
      },
      { title: 'REFUNDS.RESUME.refund_passenger', value: '-' }
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
      ? this.basicInfoRefundFormModel.basicInfoRefundGroup.get('isoCountryCode')
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

    const airline = this.basicInfoRefundFormModel.airline.airlineCode.value;

    if (airline && airline.length > 0) {
      ret = airline;
    } else {
      ret = '-';
    }

    return ret;
  }

  private _getAgentCode() {
    let ret;

    const agent = this.basicInfoRefundFormModel.agent.agentCode.value;
    const check = this.basicInfoRefundFormModel.agent.agentControlDigit.value;

    ret = agent + check;

    return ret;
  }

  private _getAmount() {
    let retCurrency = '-';

    if (this.currency) {
      retCurrency = this.currency.name;
    }

    const total = this.formOfPaymentRefundFormModel
      ? this.formOfPaymentRefundFormModel.totalAmount
      : null;
    let retTotal = '-';

    if (total && this.currency) {
      retTotal = new DecimalsFormatterPipe().transform(
        total.value,
        this.currency.numDecimals
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
          const txt =
            '0'.repeat(10 - data.id.toString().length) + data.id.toString();
          const alert = new AlertModel(
            this._translationService.translate('REFUNDS.title'),
            this._translationService.translate('REFUNDS.OK') + ' ' + txt,
            AlertType.INFO
          );

          this._alertService.setAlert(alert);
          this.id_refund_model = data.id;
          this.screen = ScreenType.DETAIL;
        },
        err => {
          if (err.error.validationErrors) {
            const forms = [
              this.basicInfoRefundFormModel.basicInfoRefundGroup,
              this.detailsRefundFormModel.detailsRefundGroup,
              this.formOfPaymentRefundFormModel.formOfPaymentRefundGroup,
              this.amountRefundFormModel.amountRefundGroup
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
              msg == ''
                ? this._translationService.translate('REFUNDS.error')
                : msg,
              AlertType.ERROR
            );
            this._focusErrorAfterAlert();
            this._alertService.setAlert(alert);
          }
        }
      );
    }
  }

  private _focusErrorAfterAlert() {
    const subscription = this._alertService
      .getDismiss()
      .subscribe(() => {
        this.setFocus();
        subscription.unsubscribe();
      });
  }

  private _buildRequestRefundIndirect(): Refund {
    const refund = new Refund();

    // BASIC INFO
    const agent = this.basicInfoRefundFormModel.agent.agentFormModelGroup;
    refund.agentCode =
      agent.get('agentCode').value + agent.get('agentControlDigit').value;
    refund.agentContact = agent.get('agentContact').value;
    refund.agentRegistrationNumber = agent.get('agentRegistrationNumber').value;
    refund.agentVatNumber = agent.get('agentVatNumber').value;

    const airline = this.basicInfoRefundFormModel.airline.airlineFormModelGroup;
    refund.airlineCode = airline.get('airlineCode').value;
    refund.airlineContact = airline.get('airlineContact').value;
    refund.airlineRegistrationNumber = airline.get(
      'airlineRegistrationNumber'
    ).value;
    refund.airlineVatNumber = airline.get('airlineVatNumber').value;

    // EXTRAS
    refund.isoCountryCode = this.basicInfoRefundFormModel.basicInfoRefundGroup.get(
      'isoCountryCode'
    ).value;
    refund.dateOfIssue = new Date().toISOString();

    // DETAILS
    refund.statisticalCode = this.detailsRefundFormModel.statisticalCode.value;
    refund.passenger = this.detailsRefundFormModel.passenger.value;
    refund.issueReason = this.detailsRefundFormModel.issueReason.value;
    refund.airlineCodeRelatedDocument = this.detailsRefundFormModel.airlineCodeRelatedDocument.value;
    refund.dateOfIssueRelatedDocument = this.detailsRefundFormModel.dateOfIssueRelatedDocument.value;
    refund.waiverCode = this.detailsRefundFormModel.waiverCode.value;
    refund.relatedDocument = this.detailsRefundFormModel.relatedDocument.value;
    refund.conjunctions = this.detailsRefundFormModel.conjunctions.value.filter(
      x =>
        x.relatedTicketDocumentNumber &&
        x.relatedTicketDocumentNumber.length == 10
    );
    refund.exchange = this.detailsRefundFormModel.exchange.value;

    if (refund.exchange) {
      refund.originalIssue = this.detailsRefundFormModel.originalIssue.value;
    } else {
      refund.originalIssue = null;
    }

    // FORM OF PAYMENT
    refund.formOfPaymentAmounts = this._fopFormat(
      this.formOfPaymentRefundFormModel.formOfPaymentAmounts.value
    );
    refund.tourCode = this.formOfPaymentRefundFormModel.tourCode.value;
    refund.currency.code = this.currency.name;
    refund.currency.decimals = this.currency.numDecimals;
    refund.customerFileReference = this.formOfPaymentRefundFormModel.customerFileReference.value;
    refund.settlementAuthorisationCode = this.formOfPaymentRefundFormModel.settlementAuthorisationCode.value;

    // AMOUNT
    refund.partialRefund = this.amountRefundFormModel.partialRefund.value;
    refund.netReporting = this.amountRefundFormModel.netRemit.value;
    refund.amounts = this._refundAmount(
      this.amountRefundFormModel.amounts.value
    );
    const taxArr = this.amountRefundFormModel.taxMiscellaneousFees.value.filter(
      x => x.type != ''
    );
    refund.taxMiscellaneousFees = taxArr ? taxArr : [];

    return refund;
  }

  private _refundAmount(val: RefundAmount) {
    val.cancellationPenalty = val.cancellationPenalty
      ? val.cancellationPenalty
      : 0;
    val.commissionAmount = val.commissionAmount ? val.commissionAmount : 0;
    val.commissionOnCpAndMfAmount = val.commissionOnCpAndMfAmount
      ? val.commissionOnCpAndMfAmount
      : 0;
    val.commissionOnCpAndMfRate = val.commissionOnCpAndMfRate
      ? val.commissionOnCpAndMfRate
      : 0;
    val.commissionRate = val.commissionRate ? val.commissionRate : 0;
    val.grossFare = val.grossFare ? val.grossFare : 0;
    val.lessGrossFareUsed = val.lessGrossFareUsed ? val.lessGrossFareUsed : 0;
    val.miscellaneousFee = val.miscellaneousFee ? val.miscellaneousFee : 0;
    val.spam = val.spam ? val.spam : 0;
    val.refundToPassenger = val.refundToPassenger ? val.refundToPassenger : 0;
    val.tax = val.tax ? val.tax : 0;
    val.taxOnCancellationPenalty = val.taxOnCancellationPenalty
      ? val.taxOnCancellationPenalty
      : 0;
    val.taxOnMiscellaneousFee = val.taxOnMiscellaneousFee
      ? val.taxOnMiscellaneousFee
      : 0;

    return val;
  }

  private _fopFormat(fopList: RefundFormOfPaymentAmounts[]) {
    for (const aux of fopList) {
      aux.amount = aux.amount && (aux as any).amount != '' ? aux.amount : 0;
    }

    return fopList;
  }
}
