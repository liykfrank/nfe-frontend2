import { CompanyService } from './../../services/resources/company.service';
import { PipeTransform, Injector } from '@angular/core';
import { AgentModel } from './../../models/agent.model';
import { AdmAcmService } from './../../services/adm-acm.service';

import { BasicInfoService } from './../../services/basic-info.service';

import { BasicInfoModel } from './../../models/basic-info.model';
import { Component, OnInit, Input, ViewChild, OnChanges, SimpleChanges } from '@angular/core';

import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxPopoverComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxpopover';

import { NwAbstractComponent } from '../../../../shared/base/abstract-component';
import { KeyValue } from '../../../../shared/models/key.value.model';
import { CompanyModel } from '../../models/company.model';
import { Configuration } from '../../models/configuration.model';
import { Currency } from '../../models/currency.model';
import { TocaType } from '../../models/toca-type.model';

import { Contact } from '../../../../shared/models/contact.model';

import { FormControl, FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DatePipe } from '@angular/common';
import { AgentService } from '../../services/resources/agent.service';

@Component({
  selector: 'app-basic-info',
  templateUrl: './basic-info.component.html',
  styleUrls: ['./basic-info.component.scss']
})
export class BasicInfoComponent extends NwAbstractComponent implements OnInit {

  forList: KeyValue[];
  tocaTypeList: TocaType[] = [];
  currencyList: Currency[] = [];
  statList: KeyValue[] = [];
  admSubTipeList: KeyValue[] = [];

  periodList: number[] = [];
  periodMonthList: KeyValue[] = [];
  periodYearList: number[] = [];
  private periods: number[] = [];

  currency: any;

  period: number;
  periodMonth: string;
  periodYear: number;

  displayAirline: boolean = false;
  displayAgent: boolean = false;

  @Input() isADM: boolean;

  basicInfo: BasicInfoModel = new BasicInfoModel();
  countries: KeyValue[];

  configuration: Configuration;

  styleAgent = '';
  styleAirline = '';

  agentCode: string;
  agentCheckDigit: string;

  errorList: string[];

  private validBasicInfo: boolean  = true;

  constructor(
    private _Injector: Injector,
    private _CompanyService: CompanyService,
    private _AgentService: AgentService,
    private _BISvc: BasicInfoService,
    private _AdmAcmService: AdmAcmService
  ) {
    super(_Injector);

    this._AdmAcmService.getConfiguration().subscribe(data => {
      if (data) {
        this.configuration = data;

        if (!data.agentVatNumberEnabled) {
          this.basicInfo.agentVatNumber = '';
        }

        if (data.defaultStat) {
          this.basicInfo.stat = data.defaultStat;
        }

        if (!data.airlineVatNumberEnabled) {
          this.basicInfo.airlineVatNumber = '';
        }

        if (!data.companyRegistrationNumberEnabled) {
          this.basicInfo.agentRegistrationNumber = '';
          this.basicInfo.airlineRegistrationNumber = '';
        }

        this.register();
      }
    });

    this._BISvc.getCountries().subscribe(data => {
      if (data) {
        this.countries = data.map(elem => {
          return {
            code: elem.isoCountryCode,
            description: elem.name
          };
        });

        if (this.countries.length > 0) {
          this.basicInfo.isoCountryCode = this.countries[0].code;
          this.selectCountry();
        }
      }
    });

    this._BISvc.getToca().subscribe(data => {
      if (data && data.length > 0) {
        const empty: TocaType = new TocaType();
        empty.code = '';
        empty.description = '';
        empty.isoCountryCode = '';

        this.tocaTypeList = data;
        this.tocaTypeList.unshift(empty);

        this.basicInfo.tocaType = data[0].code;
        this.register();
      }
    });

    this._BISvc.getCurrency().subscribe((data: any) => {
      if (data && data.length > 0) {
        const d = data[0];
        this.currencyList = d.currencies;
        this.currency = this.currencyList[0].name;
        this.selectCurrency();
      }
    });

    // TODO seleccionar la primera opción
    this._BISvc.getPeriod().subscribe(data => {
      if (data) {
        this.periods = data.values;

        this.periodList = [];
        this.periodMonthList = [];
        this.periodYearList = [];

        const months: number[] = [];
        for (let x of data.values) {
          this.periodYearList.push(Number(x.toString().substr(0, 4)));
          months.push(Number(x.toString().substr(4, 2)));
        }

        if (this.periodYearList.length > 0) {
          this.periodYear = this.periodYearList[0];
          this.checkBillingPeriod();
        }

        for (let x of Array.from(new Set(months))) {
          this.periodMonthList.push({code: x.toString(), description: this.translation.translate('MONTHS.' + x)});
        }

        if (this.periodMonthList.length > 0) {
          this.periodMonth = this.periodMonthList[0].code;
          this.checkBillingPeriod();
        }

        this.periodYearList = Array.from(new Set(this.periodYearList));

        this.listPeriods();
      }
    });

    this._CompanyService.getAirlineCountryAirlineCode().subscribe( data => {
      if (data) {
        this.basicInfo.company = data;

        if (data.isoCountryCode == this.basicInfo.isoCountryCode) {
          this.basicInfo.airlineVatNumber = this.configuration.agentVatNumberEnabled ? data.taxNumber : null;
          this.styleAirline = '';
        } else {
          this.styleAirline = 'error';

          this.basicInfo.company.address1 = '';
          this.basicInfo.company.airlineCode = '';
          this.basicInfo.company.city = '';
          this.basicInfo.company.country = '';
          this.basicInfo.company.globalName = '';
          this.basicInfo.company.isoCountryCode = '';
          this.basicInfo.company.postalCode = '';
          this.basicInfo.company.taxNumber = '';
          this.basicInfo.company.toDate = '';

          this.basicInfo.airlineVatNumber = '';
          this.basicInfo.airlineRegistrationNumber = '';
        }

        this.register();
      }
    });

    this._AgentService.getAgent().subscribe(data => {
      if (data) {
        if (data.isoCountryCode == this.basicInfo.isoCountryCode) {
          this.basicInfo.agent = data;
          this.agentCode = data.iataCode.substr(0, data.iataCode.length - 1);
          this.agentCheckDigit = data.iataCode.substr(data.iataCode.length - 1, 1);

          this.basicInfo.agentVatNumber = this.configuration.airlineVatNumberEnabled ? data.vatNumber : null;
          this.styleAgent = '';
        } else {
          this.styleAgent = 'error';

          this.basicInfo.agent.billingCity = null;
          this.basicInfo.agent.billingCountry = null;
          this.basicInfo.agent.billingPostalCode = null;
          this.basicInfo.agent.billingStreet = null;
          this.basicInfo.agent.defaultDate = null;
          this.basicInfo.agent.iataCode = null;
          this.basicInfo.agent.isoCountryCode = null;
          this.basicInfo.agent.name = null;
          this.basicInfo.agent.vatNumber = null;

          this.basicInfo.agentVatNumber = '';
          this.basicInfo.agentRegistrationNumber = '';

          this.agentCode = '';
          this.agentCheckDigit = '';
        }

        this.register();
      }
    });

    this._BISvc.getValidBasicInfo().subscribe(data => {
      if (!data) {
        this.validBasicInfo = data;

        this.styleAgent = 'error';
        this.styleAirline = 'error';
      } else {
        this.styleAgent = '';
        this.styleAirline = '';
      }
    });

    this._AdmAcmService.getErrors().subscribe(data => {
      this.errorList = data;
      this.styleAgent = '';
      this.styleAirline = '';

      if (data.indexOf('agent') != -1
          || data.indexOf('agentRegistrationNumber') != -1
          || data.indexOf('agentVatNumber') != -1) {
        this.styleAgent = 'error';
      }

      if (data.indexOf('company') != -1
          || data.indexOf('airlineRegistrationNumber') != -1
          || data.indexOf('airlineVatNumber') != -1) {
        this.styleAirline = 'error';
      }
    });

  }

  ngOnInit() {
    this.admSubTipeList = this._BISvc.getSubTypeList(this.isADM);
    console.log(this.admSubTipeList);
    this.basicInfo.transactionCode = this.admSubTipeList[0].code;
    this.selectSubtype();

    this.forList = this._BISvc.getSPDRCombo();
    this.basicInfo.concernsIndicator = this.forList[0].code;
    this.selectConcernsIndicator();

    this.statList = this._BISvc.getStatList();
  }

  register(): void {
    this._BISvc.setBasicInfo(this.basicInfo);
  }

  selectCountry() {
    if (this.basicInfo.isoCountryCode != null) {
      this._AdmAcmService.findCountryConfiguration(this.basicInfo.isoCountryCode);
      this._BISvc.getTocaAndCurrencies(this.basicInfo.isoCountryCode);

      this.basicInfo.company = new CompanyModel();
      this.basicInfo.agentVatNumber = '';
      this.basicInfo.agentRegistrationNumber = '';

      this.basicInfo.agent = new AgentModel();
      this.basicInfo.airlineVatNumber = '';
      this.basicInfo.airlineRegistrationNumber = '';
      this.register();
    }
  }

  selectConcernsIndicator() { // SPDR
    this._AdmAcmService.setSpdr(this.basicInfo.concernsIndicator);
    this.register();
  }

  selectCurrency() {
    const currencyAux = this.currencyList.find(elem => {
      return elem.name === this.currency;
    });

    this.basicInfo.currency.code = currencyAux.name;
    this.basicInfo.currency.decimals = currencyAux.numDecimals;
    this._AdmAcmService.setCurrency(this.basicInfo.currency);
    this.register();
  }

  setNetReporting() {
    this._AdmAcmService.setSpan(this.basicInfo.netReporting);
    this.register();
  }

  selectSubtype() {
    this._AdmAcmService.setSubtype(this.basicInfo.transactionCode);
    this.register();
  }

  /** Airline Component */
  onChangeAirlineCode(value) {
    this._CompanyService
      .getFromServerAirlineCountryAirlineCode(this.basicInfo.isoCountryCode, value);
  }

  onChangeVatNumber(value) {
    this.basicInfo.airlineVatNumber = value;
    this.register();
  }

  onChangeCompanyReg(value) {
    this.basicInfo.airlineRegistrationNumber = value;
    this.register();
  }

  onChangeContact(value: Contact) {
    this.basicInfo.airlineContact.contactName = value.contactName;
    this.basicInfo.airlineContact.email = value.email;
    this.basicInfo.airlineContact.phoneFaxNumber = value.phoneFaxNumber;

    this.register();
  }

  airlineMoreDetails() {
    this.displayAirline = true;
  }

  closeAirline() {
    this.displayAirline = false;
  }
  /** Airline Component */

  /** Agent Component */
  onChangeAgentCode(value: string) {
    this._AgentService.getAgentWithCode(value);
  }

  onChangeAgentVatNumber(value) {
    this.basicInfo.agentVatNumber = value;
    this.register();
  }

  onChangeAgentCompanyReg(value) {
    this.basicInfo.agentRegistrationNumber = value;
    this.register();
  }

  agentMoreDetails() {
    this.displayAgent = true;
  }

  closeAgent() {
    this.displayAgent = false;
  }
  /** Agent Component */

  private checkBillingPeriod(): void {
    if (this.period && this.periodMonth && this.periodYear) {
      this.basicInfo.billingPeriod =
        Number(this.periodYear.toString()
          + ((this.periodMonth.toString().length == 1 ? '0' : '') + this.periodMonth.toString())
          + this.period.toString());
      this.register();
    } else if (!this.period && this.periodMonth && this.periodYear) {
      this.listPeriods();
    }
  }

  private listPeriods() {
    let periodsAux = this.periods;

    if (this.periodYear) {
      periodsAux = periodsAux.filter((x) => Number(x.toString().substr(0, 4)) == this.periodYear);
    }

    if (this.periodMonth) {
      periodsAux = periodsAux.filter(x => Number(x.toString().substr(4, 2)) == Number(this.periodMonth));
    }

    this.periodList = Array.from(new Set(periodsAux.map(x => Number(x.toString().substr(6, x.toString().length - 1)))));

    if (this.periodList.length > 0) {
      this.period = this.periodList[0];
      this.checkBillingPeriod();
    }
  }

  getStyleError(text: string, fieldValue: string): boolean {

    return this.errorList.indexOf(text) != -1 && (!fieldValue || fieldValue.length == 0);
  }

}
