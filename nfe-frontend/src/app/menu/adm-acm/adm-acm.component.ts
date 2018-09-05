import { DatePipe } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslationService } from 'angular-l10n';

import { environment } from '../../../environments/environment';
import { AlertType } from '../../core/enums/alert-type.enum';
import { AlertModel } from '../../core/models/alert.model';
import { AbstractComponent } from '../../shared/base/abstract-component';
import { ROUTES } from '../../shared/constants/routes';
import { Pill } from '../../shared/models/pill.model';
import { AlertsService } from './../../core/services/alerts.service';
import { ScreenType } from './../../shared/enums/screen-type.enum';
import { DecimalsFormatterPipe } from './../../shared/pipes/decimals-formatter.pipe';
import { UtilsService } from './../../shared/services/utils.service';
import { AcdmAmountForm } from './models/acdm-amount-form.model';
import { AcdmBasicInfoFormModel } from './models/acdm-basic-info-form.model';
import { AcdmDetailsForm } from './models/acdm-details-form.model';
import { Acdm } from './models/acdm.model';
import { Country } from './models/country.model';
import { InputAmountServer } from './models/input-amount-server.model';
import { TaxAmountServer } from './models/tax-amount-server';
import { AcdmConfigurationService } from './services/acdm-configuration.service';
import { AcdmsService } from './services/acdms.service';
import { CountryService } from './services/country.service';
import { AgentFormModel } from '../../shared/components/agent/models/agent-form-model';

@Component({
  selector: 'bspl-adm-acm',
  templateUrl: './adm-acm.component.html',
  styleUrls: ['./adm-acm.component.scss'],
  providers: []
})
export class AdmAcmComponent extends AbstractComponent implements OnInit {
  url = environment.basePath + environment.api.adm_acm.acdm;
  screen = ScreenType.CREATE;

  elementsResumeBar: Object[];
  pills: Pill[];
  countryList: Country[];

  private acdmConfiguration;

  private isSticky = false;

  id_acdm = 'acdm-master-container';

  isAdm: boolean;

  ticketDocuments = [];

  id_acdm_model: number;
  basicInfoFormModel: AcdmBasicInfoFormModel = new AcdmBasicInfoFormModel();
  amountFormModel: AcdmAmountForm = new AcdmAmountForm();
  detailsFormModel: AcdmDetailsForm = new AcdmDetailsForm();

  constructor(
    protected router: Router,
    private acdmConfigurationService: AcdmConfigurationService,
    private _countryService: CountryService,
    private _utils: UtilsService,
    private _acdmsService: AcdmsService,
    private _alertService: AlertsService,
    private _translationService: TranslationService
  ) {
    super();

    this.isAdm = router.routerState.snapshot.url === ROUTES.ADM_ISSUE.URL;
    this.elementsResumeBar = this._initializeResumeBar();
    this.pills = this._initializePills();

    this._countryService.get().subscribe(countries => {
      this.countryList = countries;
    });
  }

  ngOnInit() {
    this.subscriptions.push(
      this.acdmConfigurationService.getConfiguration().subscribe(data => {
        this.basicInfoFormModel.changeAgent(data.agentVatNumberEnabled, data.companyRegistrationNumberEnabled);
        this.basicInfoFormModel.changeAirline(data.airlineVatNumberEnabled, data.companyRegistrationNumberEnabled);
      })
    );
    this.subscriptions.push(
      this.basicInfoFormModel.basicInfoModelGroup.valueChanges.subscribe(() => {
        (this.elementsResumeBar[1] as any).value = this._getBSP();
        (this.elementsResumeBar[2] as any).value = this._getAgentCode();
        (this.elementsResumeBar[3] as any).title = this._getSubtype();
        (this.elementsResumeBar[3] as any).value = this._getType();
      })
    );

    this.subscriptions.push(
      this.amountFormModel.amountPaidByCustomer.valueChanges.subscribe(val => {
        let show;

        if (val > 0) {
         show = this._getAmount();
        } else {
          show = '-';
        }

        (this.elementsResumeBar[5] as any).value = show;
      })
    );

    this.subscriptions.push(
      this.detailsFormModel.acdmDetailsFormGroup.valueChanges.subscribe(() => {
        this.ticketDocuments = this.detailsFormModel.acdmDetailsFormGroup.get('relatedTicketDocuments').value;
      })
    );
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

  onRemoveTicket(value) {
    (this.detailsFormModel.acdmDetailsFormGroup.get('relatedTicketDocuments') as FormArray).removeAt(value);
    this.ticketDocuments = this.detailsFormModel.acdmDetailsFormGroup.get('relatedTicketDocuments').value;
  }

  onReturnSave() {}

  onReturnIssue() {
    const arrTaxes = this.amountFormModel.taxMiscellaneousFees.controls;
    const count = arrTaxes.length;
    const val =
      count > 0
        ? this.amountFormModel.amountModelGroup
            .get('taxMiscellaneousFees')
            .get((count - 1).toString())
            .get('type').value
        : null;

    if (!val || val.length == 0) {
      this.amountFormModel.taxMiscellaneousFees.removeAt(count - 1);
    }

    if (
      !this.basicInfoFormModel.basicInfoModelGroup.valid ||
      !this.amountFormModel.amountModelGroup.valid ||
      !this.detailsFormModel.acdmDetailsFormGroup.valid
    ) {
      const forms = [
        this.basicInfoFormModel.basicInfoModelGroup,
        this.amountFormModel.amountModelGroup,
        this.detailsFormModel.acdmDetailsFormGroup
      ];
      this._utils.touchAllForms(forms);
      this._focusErrorAfterAlert();
      this._alertService.setAlertTranslate(
        'error',
        'ADM_ACM.error',
        AlertType.ERROR
      );

      if (arrTaxes.length == 0) {
        arrTaxes.push(new AcdmAmountForm()._taxFormModelGroup());
      }

      return;
    }

    if (arrTaxes.length == 0) {
      arrTaxes.push(new AcdmAmountForm()._taxFormModelGroup());
    }

    if (this.amountFormModel.amountModelGroup.get('amountPaidByCustomer').value <= 0) {
      this._focusErrorAfterAlert();
      this._alertService.setAlertTranslate(
        'ADM_ACM.AMOUNT.title',
        'ADM_ACM.AMOUNT.total_error',
        AlertType.ERROR
      );
      return;
    }

    if (!this._checkTaxes()) {
      this._focusErrorAfterAlert();
      this._alertService.setAlertTranslate(
        'ADM_ACM.AMOUNT.title',
        'ADM_ACM.AMOUNT.tax_error',
        AlertType.ERROR
      );
      return;
    }

    if (!this._checkToca()) {
      this._focusErrorAfterAlert();
      this._alertService.setAlertTranslate(
        'ADM_ACM.AMOUNT.title',
        'ADM_ACM.AMOUNT.toca_error',
        AlertType.ERROR
      );
      return;
    }

    const acdm = this._createModel();

    this._postAcdm(acdm);
  }

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.isSticky =
      window.pageYOffset >= document.getElementById(this.id_acdm).offsetTop;
  }

  private _initializeResumeBar() {
    return (this.elementsResumeBar = [
      { title: 'ADM_ACM.RESUME.status', value: this._getStatus() },
      { title: 'ADM_ACM.RESUME.bsp', value: this._getBSP() },
      { title: 'ADM_ACM.RESUME.agent_code', value: this._getAgentCode() },
      { title: 'ADM_ACM.RESUME.for', value: this._getType() },
      {
        title: 'ADM_ACM.RESUME.issue_date',
        value: new DatePipe('en').transform(new Date(), 'dd/MM/yyyy')
      },
      { title: 'ADM_ACM.RESUME.amount', value: '-' }
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
    const bsp = this.basicInfoFormModel.basicInfoModelGroup
      ? this.basicInfoFormModel.basicInfoModelGroup.get('isoCountryCode')
      : null;
    let ret;

    if (bsp && bsp.value.length > 0) {
      const pos = this.countryList.findIndex(
        x => x.isoCountryCode == bsp.value
      );
      ret = this.countryList[pos].name;
    } else {
      ret = '-';
    }

    return ret;
  }

  private _getAgentCode() {
    let ret;

    if (this.basicInfoFormModel.basicInfoModelGroup && this.basicInfoFormModel.basicInfoModelGroup.get('agent')) {
      const agent = this.basicInfoFormModel.basicInfoModelGroup.get('agent').get('agentCode').value;
      const check = this.basicInfoFormModel.basicInfoModelGroup
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

  private _getSubtype() {
    const bsp = this.basicInfoFormModel.basicInfoModelGroup
      ? this.basicInfoFormModel.basicInfoModelGroup.get('transactionCode')
      : null;
    let ret = 'ADM_ACM.RESUME.';

    if (bsp && bsp.value.length > 0) {
      ret += bsp.value;
    } else {
      ret += 'for';
    }

    return ret;
  }

  private _getType() {
    const bsp = this.basicInfoFormModel.basicInfoModelGroup
      ? this.basicInfoFormModel.basicInfoModelGroup.get('concernsIndicator')
      : null;
    let ret = '-';

    if (bsp && bsp.value.length > 0) {
      ret = this._translationService.translate(
        'ADM_ACM.BASIC_INFO.FORCombo.' + bsp.value
      );
    }

    return ret;
  }

  private _getAmount() {
    const currency = this.basicInfoFormModel.basicInfoModelGroup
      ? this.basicInfoFormModel.basicInfoModelGroup.get('currency').value
      : null;
    let retCurrency = '-';

    if (currency) {
      retCurrency = currency.code;
    }

    const total = this.amountFormModel.amountPaidByCustomer.value;
    let retTotal = '-';

    if (total >= 0 && currency.decimals >= 0) {
      const pipe = new DecimalsFormatterPipe();
      retTotal = pipe.transform(total, currency.decimals);
    }

    return retTotal + ' ' + (retCurrency && retCurrency.length > 0 ? retCurrency : '-');
  }

  private _initializePills() {
    return (this.pills = [
      new Pill('REFUNDS.BASIC_INFO.title', 'REFUNDS.BASIC_INFO.title'),
      new Pill('REFUNDS.AMOUNT.title', 'REFUNDS.AMOUNT.title'),
      new Pill('REFUNDS.DETAILS.title', 'REFUNDS.DETAILS.title')
    ]);
  }

  private _checkTaxes() {
    let agent = 0,
      airline = 0;

    for (const aux of this.amountFormModel.amountModelGroup.get('taxMiscellaneousFees').value) {
      if (aux && aux.type && aux.type.length > 0) {
        agent = agent + Number(aux.agentAmount);
        airline = airline + Number(aux.airlineAmount);
      }
    }

    let num = this.amountFormModel.agentCalculations.get('tax').value;
    const agentAmountTax = num ? Number(num) : 0;

    num = this.amountFormModel.airlineCalculations.get('tax').value;
    const airlineAmountTax = num ? Number(num) : 0;

    return agent == agentAmountTax && airline == airlineAmountTax;
  }

  private _checkToca() {
    let ret = true;
    const elem = this.basicInfoFormModel.basicInfoModelGroup.get('taxOnCommissionType').value;

    if (elem != '') {
      const agentTOCA = this.amountFormModel.agentCalculations.get('taxOnCommission').value;
      const airlineTOCA = this.amountFormModel.airlineCalculations.get('taxOnCommission').value;

      ret = agentTOCA != 0 || airlineTOCA != 0;

      if (agentTOCA == 0) {
        this.amountFormModel.agentCalculations.get('taxOnCommission').setErrors({
          customError: {
            invalid: true,
            message: ''
          }
        });
      }

      if (airlineTOCA == 0) {
        this.amountFormModel.airlineCalculations.get('taxOnCommission').setErrors({
          customError: {
            invalid: true,
            message: ''
          }
        });
      }
    }
    return ret;
  }

  private _createModel(): Acdm {
    const acdm = new Acdm();

    // Basic Info
    acdm.isoCountryCode = this.basicInfoFormModel.basicInfoModelGroup.get('isoCountryCode').value;
    acdm.billingPeriod = this.basicInfoFormModel.basicInfoModelGroup.get('billingPeriod').value;

    const AGENT = this.basicInfoFormModel.basicInfoModelGroup.get('agent');
    acdm.agentCode = AGENT.get('agentCode').value + AGENT.get('agentControlDigit').value;
    acdm.agentRegistrationNumber = AGENT.get('agentRegistrationNumber').value;
    acdm.agentVatNumber = AGENT.get('agentVatNumber').value;

    acdm.transactionCode = this.basicInfoFormModel.basicInfoModelGroup.get('transactionCode').value;
    acdm.airlineCode = this.basicInfoFormModel.basicInfoModelGroup
      .get('airline')
      .get('airlineCode').value;
    acdm.airlineRegistrationNumber = this.basicInfoFormModel.basicInfoModelGroup
      .get('airline')
      .get('airlineRegistrationNumber').value;
    acdm.airlineVatNumber = this.basicInfoFormModel.basicInfoModelGroup
      .get('airline')
      .get('airlineVatNumber').value;
    acdm.airlineContact = this.basicInfoFormModel.basicInfoModelGroup
      .get('airline')
      .get('airlineContact').value;

    acdm.concernsIndicator = this.basicInfoFormModel.basicInfoModelGroup.get(
      'concernsIndicator'
    ).value;
    acdm.taxOnCommissionType = this.basicInfoFormModel.basicInfoModelGroup.get(
      'taxOnCommissionType'
    ).value;
    acdm.currency = this.basicInfoFormModel.basicInfoModelGroup.get('currency').value;
    acdm.netReporting = this.basicInfoFormModel.basicInfoModelGroup.get('netReporting').value;
    acdm.statisticalCode = this.basicInfoFormModel.basicInfoModelGroup.get('statisticalCode').value;

    // Amount
    acdm.agentCalculations = this._checkAmountValues(this.amountFormModel.agentCalculations.value);
    acdm.airlineCalculations = this._checkAmountValues(this.amountFormModel.airlineCalculations.value);

    const aux = this.amountFormModel.taxMiscellaneousFees
      .value.filter(x => x.type != '');

    acdm.taxMiscellaneousFees = aux ? this._filterTaxes(aux) : [];
    acdm.amountPaidByCustomer = this.amountFormModel.amountModelGroup.get(
      'amountPaidByCustomer'
    ).value;

    // Details
    const date = this.detailsFormModel.acdmDetailsFormGroup.get(
      'dateOfIssueRelatedDocument'
    ).value;
    acdm.dateOfIssueRelatedDocument = date ? new Date(date).toISOString() : null;
    acdm.passenger = this.detailsFormModel.acdmDetailsFormGroup.get('passenger').value;
    acdm.relatedTicketDocuments = this.detailsFormModel.acdmDetailsFormGroup.get(
      'relatedTicketDocuments'
    ).value;
    acdm.reasonForMemoIssuanceCode = this.detailsFormModel.acdmDetailsFormGroup.get(
      'reasonForMemoIssuanceCode'
    ).value;
    acdm.reasonForMemo = this.detailsFormModel.acdmDetailsFormGroup.get('reasonForMemo').value;

    return acdm;
  }

  private _checkAmountValues(ret: InputAmountServer) {
    ret.commission = ret.commission ? ret.commission : 0;
    ret.fare = ret.fare ? ret.fare : 0;
    ret.spam = ret.spam ? ret.spam : 0;
    ret.tax = ret.tax ? ret.tax : 0;
    ret.taxOnCommission = ret.taxOnCommission ? ret.taxOnCommission : 0;

    return ret;
  }

  private _focusErrorAfterAlert() {
    const subscription = this._alertService
      .getDismiss()
      .subscribe(() => {
        this.setFocus();
        subscription.unsubscribe();
      });
  }

  private _filterTaxes(ret: TaxAmountServer[]) {
    for (const aux of ret) {
      aux.agentAmount = (aux as any).agentAmount != '' ? aux.agentAmount : 0;
      aux.airlineAmount = (aux as any).airlineAmount != '' ? aux.airlineAmount : 0;
    }

    return ret;
  }

  private _postAcdm(acdm: Acdm) {
    this._acdmsService.postAcdm(acdm).subscribe(
      data => {
        const txt = '0'.repeat(10 - data.id.toString().length) + data.id.toString();
        const alert =
          new AlertModel(
            this._translationService.translate(this.isAdm ? 'ADM_ACM.ADMTitle' : 'ADM_ACM.ACMTitle'),
            this._translationService.translate('ADM_ACM.OK') + ' ' + txt,
            AlertType.INFO);

        this._alertService.setAlert(alert);

        this.id_acdm_model = data.id;
        this.screen = ScreenType.DETAIL;
      },
      err => {
        const list = err.error.validationErrors;

        if (list && list.length == 1 && list[0].fieldName == 'regularized') {
          const subscription = this._alertService
          .getAccept()
          .subscribe(accept => {
            if (accept) {
              acdm.regularized = true;
              this._postAcdm(acdm);
            }

            subscription.unsubscribe();
          });

          this._alertService.setAlertTranslate(
            'ADM_ACM.' + (this.isAdm ? 'ADMTitle' : 'ACMTitle'),
            'ADM_ACM.regularized',
            AlertType.CONFIRM
          );
        } else if (list) {
          const forms = [this.basicInfoFormModel.basicInfoModelGroup, this.amountFormModel.amountModelGroup, this.detailsFormModel.acdmDetailsFormGroup];

          const errors = this._utils.setBackErrorsOnForms(
            forms.filter(x => x != null),
            err.error.validationErrors
          );

          console.log(errors);

          let msg = this._translationService.translate('error');
          for (const aux of errors) {
            msg += '\n' + aux.message;
          }

          const alert = new AlertModel(this._translationService.translate('error'), msg, AlertType.ERROR);
          this._focusErrorAfterAlert();
          this._alertService.setAlert(alert);
        }
      }
    );
  }
}
