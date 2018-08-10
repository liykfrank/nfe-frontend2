import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { TranslationService } from 'angular-l10n';

import { environment } from '../../../../../environments/environment';
import { AlertType } from '../../../../core/enums/alert-type.enum';
import { ReactiveFormHandler } from '../../../../shared/base/reactive-form-handler';

import { KeyValue } from '../../../../shared/models/key.value.model';
import { Country } from '../../models/country.model';
import { TocaType } from '../../models/toca-type.model';
import { AcdmConfigurationService } from '../../services/adm-acm-configuration.service';
import { CountryService } from '../../services/country.service';
import { AlertsService } from './../../../../core/services/alerts.service';

import { AcdmBasicInfoFormModel } from './../../models/acdm-basic-info-form.model';
import { AdmAcmConfiguration } from './../../models/adm-acm-configuration.model';
import { PeriodService } from './../../services/period.service';
import { TocaService } from './../../services/toca.service';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';

@Component({
  selector: 'bspl-basic-info-adm-acm',
  templateUrl: './basic-info-adm-acm.component.html',
  styleUrls: ['./basic-info-adm-acm.component.scss']
})
export class BasicInfoAdmAcmComponent extends ReactiveFormHandler implements OnInit {
  basicInfoFormModelGroup: FormGroup = new AcdmBasicInfoFormModel()
    .basicInfoFormModelGroup;
  private listPeriods: number[] = [];

  @Input() isAdm = true;

  configuration: AdmAcmConfiguration;

  type = EnvironmentType.ACDM;

  countryList: Country[];
  typeList: string[] = [];
  forList: string[] = ['I', 'R', 'X', 'E'];
  tocaList: TocaType[] = [];
  currencyList: Currency[] = [];

  periodNumber: number[] = [];
  periodMonth: KeyValue[] = [];
  periodYear: number[] = [];
  private selectPeriodNumber: number;
  private selectPeriodMonth: number;
  private selectPeriodYear: number;

  constructor(
    private _acdmConfigurationService: AcdmConfigurationService,
    private _countryService: CountryService,
    private _periodService: PeriodService,
    private _tocaService: TocaService,
    private _alertsService: AlertsService,
    private _translationService: TranslationService
  ) {
    super();

    this.subscribe(this.basicInfoFormModelGroup);

    this.subscriptions.push(
      this._acdmConfigurationService
        .getConfiguration()
        .subscribe(data => {
          this.configuration = data;
        })
    );

    if (this.isAdm) {
      this.typeList = ['ADMA', 'SPDR', 'ADMD'];
    } else {
      this.typeList = ['ACMA', 'SPCR', 'ACMD'];
    }
  }
  private lastISO: string;
  ngOnInit() {
    this._countryService.get().subscribe(countries => {
      // Initialize all
      this.countryList = countries;
      this.basicInfoFormModelGroup
        .get('isoCountryCode')
        .setValue(countries[0].isoCountryCode);
      this._changeCountry(countries[0].isoCountryCode);
      this.lastISO = countries[0].isoCountryCode;

      // Create subscription on countries
      this.subscriptions.push(
        this.basicInfoFormModelGroup
          .get('isoCountryCode')
          .valueChanges.subscribe(data => {
            const iso = this.basicInfoFormModelGroup.get('isoCountryCode').value;

            if (
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
    });
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

  onReturnFormCurrency(event) {
    console.log(event);
  }

  private _changeCountry(iso) {
    // TODO: set first child, set configuration on basic info, use stat component
    this._acdmConfigurationService.getConfigurationByISO(iso);
    this._periodService
      .getPeriodWithISO(iso)
      .subscribe(periods => this._createPeriods(periods.values));
    this._tocaService
      .getTocaWithISO(iso)
      .subscribe(toca => (this.tocaList = [new TocaType()].concat(toca))); // TODO: si solo 1 disabled
    // this._currencyService.getCurrencyWithISO(iso).subscribe(currencies => this.currencyList = currencies.currencies);
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

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

  // forList: KeyValue[];
  // tocaTypeList: TocaType[] = [];
  // currencyList: Currency[] = [];
  // statList: KeyValue[] = [];
  // admSubTipeList: KeyValue[] = [];

  // periodList: number[] = [];
  // periodMonthList: KeyValue[] = [];
  // periodYearList: number[] = [];
  // private periods: number[] = [];

  // currency: any;

  // period: number;
  // periodMonth: string;
  // periodYear: number;

  // displayAirline: boolean = false;
  // displayAgent: boolean = false;

  // @Input() isADM: boolean;

  // basicInfo: BasicInfoModel = new BasicInfoModel();
  // countries: KeyValue[];

  // configuration: Configuration;

  // styleAgent = '';
  // styleAirline = '';

  // agentCode: string;
  // agentCheckDigit: string;

  // errorList: string[];

  // private validBasicInfo: boolean  = true;

  // constructor(
  //   private _Injector: Injector,
  //   private _CompanyService: CompanyService,
  //   private _AgentService: AgentService,
  //   private _BISvc: BasicInfoService,
  //   private _AdmAcmService: AdmAcmService
  // ) {
  //   super(_Injector);

  //   this._AgentService.setBaseURL(environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.agent);
  //   this._CompanyService.setBaseURL(environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.company);

  //   this._AdmAcmService.getConfiguration().subscribe(data => {
  //     if (data) {
  //       this.configuration = data;

  //       if (!data.agentVatNumberEnabled) {
  //         this.basicInfo.agentVatNumber = '';
  //       }

  //       if (data.defaultStat) {
  //         this.basicInfo.stat = data.defaultStat;
  //       }

  //       if (!data.airlineVatNumberEnabled) {
  //         this.basicInfo.airlineVatNumber = '';
  //       }

  //       if (!data.companyRegistrationNumberEnabled) {
  //         this.basicInfo.agentRegistrationNumber = '';
  //         this.basicInfo.airlineRegistrationNumber = '';
  //       }

  //       this.register();
  //     }
  //   });

  //   this._BISvc.getCountries().subscribe(data => {
  //     if (data) {
  //       this.countries = data.map(elem => {
  //         return {
  //           code: elem.isoCountryCode,
  //           description: elem.name
  //         };
  //       });

  //       if (this.countries.length > 0) {
  //         this.basicInfo.isoCountryCode = this.countries[0].code;
  //         this.selectCountry();
  //       }
  //     }
  //   });

  //   this._BISvc.getToca().subscribe(data => {
  //     if (data && data.length > 0) {
  //       const empty: TocaType = new TocaType();
  //       empty.code = '';
  //       empty.description = '';
  //       empty.isoCountryCode = '';

  //       this.tocaTypeList = data;
  //       this.tocaTypeList.unshift(empty);

  //       this.basicInfo.tocaType = data[0].code;
  //       this.register();
  //     }
  //   });

  //   this._BISvc.getCurrency().subscribe((data: any) => {
  //     if (data && data.length > 0) {
  //       const d = data[0];
  //       this.currencyList = d.currencies;
  //       this.currency = this.currencyList[0].name;
  //       this.selectCurrency();
  //     }
  //   });

  //   // TODO seleccionar la primera opción
  //   this._BISvc.getPeriod().subscribe(data => {
  //     if (data) {
  //       this.periods = data.values;

  //       this.periodList = [];
  //       this.periodMonthList = [];
  //       this.periodYearList = [];

  //       const months: number[] = [];
  //       for (let x of data.values) {
  //         this.periodYearList.push(Number(x.toString().substr(0, 4)));
  //         months.push(Number(x.toString().substr(4, 2)));
  //       }

  //       if (this.periodYearList.length > 0) {
  //         this.periodYear = this.periodYearList[0];
  //         this.checkBillingPeriod();
  //       }

  //       for (let x of Array.from(new Set(months))) {
  //         this.periodMonthList.push({code: x.toString(), description: this.translation.translate('MONTHS.' + x)});
  //       }

  //       if (this.periodMonthList.length > 0) {
  //         this.periodMonth = this.periodMonthList[0].code;
  //         this.checkBillingPeriod();
  //       }

  //       this.periodYearList = Array.from(new Set(this.periodYearList));

  //       this.listPeriods();
  //     }
  //   });

  //   this._CompanyService.getAirlineCountryAirlineCode().subscribe( data => {
  //     if (data) {
  //       this.basicInfo.company = data;

  //       if (data.isoCountryCode == this.basicInfo.isoCountryCode) {
  //         this.basicInfo.airlineVatNumber = this.configuration.agentVatNumberEnabled ? data.taxNumber : null;
  //         this.styleAirline = '';
  //       } else {
  //         this.styleAirline = 'error';

  //         this.basicInfo.company.address1 = '';
  //         this.basicInfo.company.airlineCode = '';
  //         this.basicInfo.company.city = '';
  //         this.basicInfo.company.country = '';
  //         this.basicInfo.company.globalName = '';
  //         this.basicInfo.company.isoCountryCode = '';
  //         this.basicInfo.company.postalCode = '';
  //         this.basicInfo.company.taxNumber = '';
  //         this.basicInfo.company.toDate = '';

  //         this.basicInfo.airlineVatNumber = '';
  //         this.basicInfo.airlineRegistrationNumber = '';
  //       }

  //       this.register();
  //     }
  //   });

  //   this._AgentService.getAgent().subscribe(data => {
  //     if (data) {
  //       if (data.isoCountryCode == this.basicInfo.isoCountryCode) {
  //         this.basicInfo.agent = data;
  //         this.agentCode = data.iataCode.substr(0, data.iataCode.length - 1);
  //         this.agentCheckDigit = data.iataCode.substr(data.iataCode.length - 1, 1);

  //         this.basicInfo.agentVatNumber = this.configuration.airlineVatNumberEnabled ? data.vatNumber : null;
  //         this.styleAgent = '';
  //       } else {
  //         this.styleAgent = 'error';

  //         this.basicInfo.agent.billingCity = null;
  //         this.basicInfo.agent.billingCountry = null;
  //         this.basicInfo.agent.billingPostalCode = null;
  //         this.basicInfo.agent.billingStreet = null;
  //         this.basicInfo.agent.defaultDate = null;
  //         this.basicInfo.agent.iataCode = null;
  //         this.basicInfo.agent.isoCountryCode = null;
  //         this.basicInfo.agent.name = null;
  //         this.basicInfo.agent.vatNumber = null;

  //         this.basicInfo.agentVatNumber = '';
  //         this.basicInfo.agentRegistrationNumber = '';

  //         this.agentCode = '';
  //         this.agentCheckDigit = '';
  //       }

  //       this.register();
  //     }
  //   });

  //   this._BISvc.getValidBasicInfo().subscribe(data => {
  //     if (!data) {
  //       this.validBasicInfo = data;

  //       this.styleAgent = 'error';
  //       this.styleAirline = 'error';
  //     } else {
  //       this.styleAgent = '';
  //       this.styleAirline = '';
  //     }
  //   });

  //   this._AdmAcmService.getErrors().subscribe(data => {
  //     this.errorList = data;
  //     this.styleAgent = '';
  //     this.styleAirline = '';

  //     if (data.indexOf('agent') != -1
  //         || data.indexOf('agentRegistrationNumber') != -1
  //         || data.indexOf('agentVatNumber') != -1) {
  //       this.styleAgent = 'error';
  //     }

  //     if (data.indexOf('company') != -1
  //         || data.indexOf('airlineRegistrationNumber') != -1
  //         || data.indexOf('airlineVatNumber') != -1) {
  //       this.styleAirline = 'error';
  //     }
  //   });

  // }

  // ngOnInit() {
  //   this.admSubTipeList = this._BISvc.getSubTypeList(this.isADM);
  //   this.basicInfo.transactionCode = this.admSubTipeList[0].code;
  //   this.selectSubtype();

  //   this.forList = this._BISvc.getSPDRCombo();
  //   this.basicInfo.concernsIndicator = this.forList[0].code;
  //   this.selectConcernsIndicator();

  //   this.statList = this._BISvc.getStatList();
  // }

  // register(): void {
  //   this._BISvc.setBasicInfo(this.basicInfo);
  // }

  // selectCountry() {
  //   if (this.basicInfo.isoCountryCode != null) {
  //     this._AdmAcmService.findCountryConfiguration(this.basicInfo.isoCountryCode);
  //     this._BISvc.getTocaAndCurrencies(this.basicInfo.isoCountryCode);

  //     this.basicInfo.company = new CompanyModel();
  //     this.basicInfo.agentVatNumber = '';
  //     this.basicInfo.agentRegistrationNumber = '';

  //     this.basicInfo.agent = new AgentModel();
  //     this.basicInfo.airlineVatNumber = '';
  //     this.basicInfo.airlineRegistrationNumber = '';
  //     this.register();
  //   }
  // }

  // selectConcernsIndicator() { // SPDR
  //   this._AdmAcmService.setSpdr(this.basicInfo.concernsIndicator);
  //   this.register();
  // }

  // selectCurrency() {
  //   const currencyAux = this.currencyList.find(elem => {
  //     return elem.name === this.currency;
  //   });

  //   this.basicInfo.currency.code = currencyAux.name;
  //   this.basicInfo.currency.decimals = currencyAux.numDecimals;
  //   this._AdmAcmService.setCurrency(this.basicInfo.currency);
  //   this.register();
  // }

  // setNetReporting() {
  //   this._AdmAcmService.setSpan(this.basicInfo.netReporting);
  //   this.register();
  // }

  // selectSubtype() {
  //   this._AdmAcmService.setSubtype(this.basicInfo.transactionCode);
  //   this.register();
  // }

  // /** Airline Component */
  // onChangeAirlineCode(value) {
  //   this._CompanyService
  //     .getFromServerAirlineCountryAirlineCode(this.basicInfo.isoCountryCode, value);
  // }

  // onChangeVatNumber(value) {
  //   this.basicInfo.airlineVatNumber = value;
  //   this.register();
  // }

  // onChangeCompanyReg(value) {
  //   console.log('onChangeCompanyReg')
  //   console.log(value)
  //   this.basicInfo.airlineRegistrationNumber = value;
  //   this.register();
  // }

  // onChangeContact(value: Contact) {
  //   this.basicInfo.airlineContact.contactName = value.contactName;
  //   this.basicInfo.airlineContact.email = value.email;
  //   this.basicInfo.airlineContact.phoneFaxNumber = value.phoneFaxNumber;

  //   this.register();
  // }

  // airlineMoreDetails() {
  //   this.displayAirline = true;
  // }

  // closeAirline() {
  //   this.displayAirline = false;
  // }
  // /** Airline Component */

  // /** Agent Component */
  // onChangeAgentCode(value: string) {
  //   this._AgentService.getAgentWithCode(value);
  // }

  // onChangeAgentVatNumber(value) {
  //   this.basicInfo.agentVatNumber = value;
  //   this.register();
  // }

  // onChangeAgentCompanyReg(value) {
  //   this.basicInfo.agentRegistrationNumber = value;
  //   this.register();
  // }

  // agentMoreDetails() {
  //   this.displayAgent = true;
  // }

  // closeAgent() {
  //   this.displayAgent = false;
  // }
  // /** Agent Component */

  // private checkBillingPeriod(): void {
  //   if (this.period && this.periodMonth && this.periodYear) {
  //     this.basicInfo.billingPeriod =
  //       Number(this.periodYear.toString()
  //         + ((this.periodMonth.toString().length == 1 ? '0' : '') + this.periodMonth.toString())
  //         + this.period.toString());
  //     this.register();
  //   } else if (!this.period && this.periodMonth && this.periodYear) {
  //     this.listPeriods();
  //   }
  // }

  // private listPeriods() {
  //   let periodsAux = this.periods;

  //   if (this.periodYear) {
  //     periodsAux = periodsAux.filter((x) => Number(x.toString().substr(0, 4)) == this.periodYear);
  //   }

  //   if (this.periodMonth) {
  //     periodsAux = periodsAux.filter(x => Number(x.toString().substr(4, 2)) == Number(this.periodMonth));
  //   }

  //   this.periodList = Array.from(new Set(periodsAux.map(x => Number(x.toString().substr(6, x.toString().length - 1)))));

  //   if (this.periodList.length > 0) {
  //     this.period = this.periodList[0];
  //     this.checkBillingPeriod();
  //   }
  // }

  // getStyleError(text: string, fieldValue: string): boolean {

  //   return this.errorList.indexOf(text) != -1 && (!fieldValue || fieldValue.length == 0);
  // }
}
