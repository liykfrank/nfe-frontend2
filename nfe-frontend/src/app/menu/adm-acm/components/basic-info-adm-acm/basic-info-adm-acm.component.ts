import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TranslationService } from 'angular-l10n';

import { AlertType } from '../../../../core/enums/alert-type.enum';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';
import { CurrencyPost } from '../../../../shared/components/currency/models/currency-post.model';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { KeyValue } from '../../../../shared/models/key.value.model';
import { Country } from '../../models/country.model';
import { TocaType } from '../../models/toca-type.model';
import { AcdmConfigurationService } from '../../services/acdm-configuration.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { CurrencyService } from './../../../../shared/components/currency/services/currency.service';
import { GLOBALS } from './../../../../shared/constants/globals';
import { AcdmBasicInfoFormModel } from './../../models/acdm-basic-info-form.model';
import { AdmAcmConfiguration } from './../../models/adm-acm-configuration.model';
import { PeriodService } from './../../services/period.service';
import { TocaService } from './../../services/toca.service';

@Component({
  selector: 'bspl-basic-info-adm-acm',
  templateUrl: './basic-info-adm-acm.component.html',
  styleUrls: ['./basic-info-adm-acm.component.scss']
})
export class BasicInfoAdmAcmComponent
  extends ReactiveFormHandler<AcdmBasicInfoFormModel>
  implements OnInit, OnChanges {

  basicInfoFormModelGroup: FormGroup;

  private listPeriods: number[] = [];

  @Input()
  isAdm = true;
  @Input()
  countryList: Country[];

  configuration: AdmAcmConfiguration;

  type = EnvironmentType.ACDM;

  typeList: string[] = [];
  forList: string[] = GLOBALS.ACDM.FOR;
  tocaList: TocaType[] = [];
  currencyList: Currency[] = [];

  periodNumber: number[] = [];
  periodMonth: KeyValue[] = [];
  periodYear: number[] = [];
  private selectPeriodNumber: number;
  private selectPeriodMonth: number;
  private selectPeriodYear: number;

  private lastISO: string;
  private countryChange = false;

  constructor(
    private _acdmConfigurationService: AcdmConfigurationService,
    private _periodService: PeriodService,
    private _tocaService: TocaService,
    private _basicInfoService: BasicInfoService,
    private _currencyService: CurrencyService,
    private _alertsService: AlertsService,
    private _translationService: TranslationService
  ) {
    super();

    this.typeList = this.isAdm ? GLOBALS.ACDM.ADM : GLOBALS.ACDM.ACM;
  }

  ngOnInit() {
    this.basicInfoFormModelGroup = this.model.basicInfoModelGroup;

    this.subscriptions.push(
      this._acdmConfigurationService.getConfiguration().subscribe(data => {
        this.configuration = data;

        this.basicInfoFormModelGroup.get('netReporting').setValue(false);

        if (!data.taxOnCommissionEnabled) {
          this._basicInfoService.setToca('');
        }
      })
    );

    this.subscriptions.push(
      this.basicInfoFormModelGroup
        .get('transactionCode')
        .valueChanges.subscribe(transactionCode => {
          this._basicInfoService.setSubType(transactionCode);
        })
    );

    this.subscriptions.push(
      this.basicInfoFormModelGroup
        .get('netReporting')
        .valueChanges.subscribe(showSpam => {
          this._basicInfoService.setShowSpam(showSpam);
        })
    );

    this.subscriptions.push(
      this.basicInfoFormModelGroup
        .get('concernsIndicator')
        .valueChanges.subscribe(concernsIndicator => {
          this._basicInfoService.setConcernsIndicator(concernsIndicator);
        })
    );

    this.subscriptions.push(
      this._currencyService.getCurrencyState().subscribe(data => {
        const currencyModel = this.basicInfoFormModelGroup.get('currency');

        currencyModel.get('code').setValue(data.name);
        currencyModel.get('decimals').setValue(data.numDecimals);

        const currency: CurrencyPost = new CurrencyPost();
        currency.code = data.name;
        currency.decimals = data.numDecimals;

        this._basicInfoService.setCurrency(currency);
      })
    );

    this.subscriptions.push(
      this.basicInfoFormModelGroup
        .get('taxOnCommissionType')
        .valueChanges.subscribe(data => {
          this._basicInfoService.setToca(data);
        })
    );

    // Create subscription on countries
    this.subscriptions.push(
      this.basicInfoFormModelGroup
        .get('isoCountryCode')
        .valueChanges.subscribe(data => {
          const iso = this.basicInfoFormModelGroup.get('isoCountryCode').value;

          if (
            this.countryChange &&
            this.configuration &&
            iso != this.configuration.isoCountryCode
          ) {
            const subscription = this._alertsService
              .getAccept()
              .subscribe(accept => {
                if (accept) {
                  this._changeCountry(iso);
                  this.lastISO = iso;
                } else {
                  this.basicInfoFormModelGroup
                    .get('isoCountryCode')
                    .setValue(this.lastISO);
                }
                subscription.unsubscribe();
              });

            this._alertsService.setAlertTranslate(
              'ADM_ACM.BASIC_INFO.COUNTRY.title',
              'ADM_ACM.BASIC_INFO.COUNTRY.text',
              AlertType.CONFIRM
            );
          }
        })
    );

    this.basicInfoFormModelGroup
      .get('transactionCode')
      .setValue(this.typeList[0]);
    this.basicInfoFormModelGroup
      .get('concernsIndicator')
      .setValue(this.forList[0]);

    this.basicInfoFormModelGroup.get('id').disable();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.countryList && this.countryList) {
      // Initialize all
      this.basicInfoFormModelGroup
        .get('isoCountryCode')
        .setValue(this.countryList[0].isoCountryCode);
      this._changeCountry(this.countryList[0].isoCountryCode);
      this.lastISO = this.countryList[0].isoCountryCode;
      this.countryChange = true;

      this.basicInfoFormModelGroup.get('isoCountryCode').markAsTouched();
    }
  }

  setPeriod(event) {
    if (event) {
      const val = event.srcElement.value;
      switch (event.srcElement.name) {
        case 'selectPeriodNumber':
          this.selectPeriodNumber = val;
          break;

        case 'selectPeriodMonth':
          this.selectPeriodMonth = val;
          break;

        case 'selectPeriodYear':
          this.selectPeriodYear = val;
          break;

        default:
          break;
      }
    }

    if (
      this.selectPeriodNumber &&
      this.selectPeriodMonth &&
      this.selectPeriodYear
    ) {
      const period = Number(
        this.selectPeriodYear.toString() +
          ((this.selectPeriodMonth.toString().length == 1 ? '0' : '') +
            this.selectPeriodMonth.toString()) +
          this.selectPeriodMonth.toString()
      );

      this.basicInfoFormModelGroup.get('billingPeriod').setValue(period);
    } else {
      this._filterPeriods();
    }
  }

  private _changeCountry(iso) {
    this._acdmConfigurationService.getConfigurationByISO(iso);
    this._periodService
      .getPeriodWithISO(iso)
      .subscribe(periods => this._createPeriods(periods.values));
    this._tocaService.getTocaWithISO(iso).subscribe(toca => {
      this.tocaList = [new TocaType()].concat(toca);
      this.basicInfoFormModelGroup
        .get('taxOnCommissionType')
        .setValue(this.tocaList[0].code);
      if (this.tocaList.length == 1) {
        this.basicInfoFormModelGroup.get('taxOnCommissionType').disable();
      } else {
        this.basicInfoFormModelGroup.get('taxOnCommissionType').enable();
      }
    });
  }

  private _createPeriods(list: number[]) {
    this.listPeriods = list;

    this.periodNumber = [];
    this.periodMonth = [];
    this.periodYear = [];

    this.selectPeriodNumber = null;
    this.selectPeriodMonth = null;
    this.selectPeriodYear = null;

    const months: number[] = [];
    for (const x of list) {
      this.periodYear.push(Number(x.toString().substr(0, 4)));
      months.push(Number(x.toString().substr(4, 2)));
    }

    this.periodYear = Array.from(new Set(this.periodYear));

    if (this.periodYear.length > 0) {
      this.selectPeriodYear = this.periodYear[0];
    }

    for (const x of Array.from(new Set(months))) {
      this.periodMonth.push({
        code: x.toString(),
        description: this._translationService.translate('MONTHS.' + x)
      });
    }

    if (this.periodMonth.length > 0) {
      this.selectPeriodMonth = Number(this.periodMonth[0].code);
    }

    this.setPeriod(null);
  }

  private _filterPeriods() {
    let listPeriodsAux = this.listPeriods;

    if (this.selectPeriodYear) {
      listPeriodsAux = listPeriodsAux.filter(
        x => Number(x.toString().substr(0, 4)) == this.selectPeriodYear
      );
    }

    if (this.selectPeriodMonth) {
      listPeriodsAux = listPeriodsAux.filter(
        x => Number(x.toString().substr(4, 2)) == Number(this.selectPeriodMonth)
      );
    }

    this.periodNumber = Array.from(
      new Set(
        listPeriodsAux.map(x =>
          Number(x.toString().substr(6, x.toString().length - 1))
        )
      )
    );

    if (this.periodNumber.length > 0) {
      this.selectPeriodNumber = this.periodNumber[0];
      this.setPeriod(null);
    }
  }

  private showTOCA() {
    return this.configuration.taxOnCommissionEnabled;
  }

  private showNRID() {
    return this.configuration.nridAndSpamEnabled;
  }
}
