
import { PipeTransform, Injector } from '@angular/core';
import { AgentModel } from './../../models/agent.model';
import { AdmAcmService } from './../../services/adm-acm.service';

import { BasicInfoService } from './../../services/basic-info.service';

import { BasicInfoModel } from './../../models/basic-info.model';
import { Component, OnInit, Input, ViewChild, OnChanges, SimpleChanges } from '@angular/core';

import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxPopoverComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxpopover';
import { jqxDateTimeInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdatetimeinput';
import { jqxCheckBoxComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxcheckbox';

import { KeyValue } from '../../../../shared/models/key.value.model';
import { JqxNwComboComponent } from '../../../../shared/components/jqx-nw-combo/jqx-nw-combo.component';
import { Currency } from '../../models/currency.model';
import { CompanyModel } from '../../models/company.model';
import { Configuration } from '../../models/configuration.model';
import { TocaType } from '../../models/toca-type.model';
import { FormControl, FormGroup } from '@angular/forms';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { DatePipe } from '@angular/common';
import { NwAbstractComponent } from '../../../../shared/base/abstract-component';

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


  @ViewChild('popoverAgent') popAgent: jqxPopoverComponent;
  @ViewChild('popoverAirline') popAirline: jqxPopoverComponent;
  @ViewChild('billingPeriodDay') period: jqxDropDownListComponent;
  @ViewChild('billingPeriodMonth') periodMonth: jqxDropDownListComponent;
  @ViewChild('billingPeriodYear') periodYear: jqxDropDownListComponent;

  @Input() isADM: boolean;

  basicInfo: BasicInfoModel = new BasicInfoModel();
  countries: KeyValue[];

  agent: AgentModel;
  company: CompanyModel;
  configuration: Configuration;

  constructor(
    private _Injector: Injector,
    private _BISvc: BasicInfoService,
    private _AdmAcmService: AdmAcmService
  ) {
    super(_Injector);

    this._AdmAcmService.getConfiguration().subscribe(data => {
      if (data) {
        this.configuration = data;
      }
    });

    this._BISvc.getCountries().subscribe(data => {
      if (data) {
        this.countries = data;
      }
    });

    this._BISvc.getAgent().subscribe(data => {
      if(data) {
        this.agent = data;
        this.basicInfo.agent = data;
      }
    });

    this._BISvc.getCompany().subscribe(data => {
      if (data) {
        this.company = data;
        this.basicInfo.company = data;
      }
    });

    this._BISvc.getToca().subscribe(data => {
      if (data) {
        this.tocaTypeList = data;
      }
    });

    this._BISvc.getCurrency().subscribe(data => {
      if (data) {
        this.currencyList = data;
      }
    });

    this._BISvc.getPeriod().subscribe(data => {
      if (data) {
        this.periodList = [];
        this.periodMonthList = [];
        this.periodYearList = [];

        const months: number[] = []
        for (let x of data) {
          this.periodYearList.push(Number(x.toString().substr(0, 4)));
          this.periodList.push(Number(x.toString().substr(6, 1)));
          months.push(Number(x.toString().substr(4, 2)));
        }

        for (let x of Array.from(new Set(months))) {
          this.periodMonthList.push({code: x.toString(), description: this.translation.translate('MONTHS.' + x)});
        }

        this.periodYearList = Array.from(new Set(this.periodYearList));

        if (this.periodList.length == 1) {
          this.period.selectIndex(0);
        }
        if (this.periodMonthList.length == 1) {
          this.periodMonth.selectIndex(0);
        }
        if (this.periodYearList.length == 1) {
          this.periodYear.selectIndex(0);
        }
      }
    });
  }

  ngOnInit() {
    this.admSubTipeList = this._BISvc.getSubTypeList(this.isADM);
    this.forList = this._BISvc.getSPDRCombo();
    this.statList = this._BISvc.getStatList();
  }

  register(): void {
    this._BISvc.setBasicInfo(this.basicInfo);
  }

  selectCountry(event: any) {
    this._AdmAcmService.findCountryConfiguration(event.args.item.originalItem.code);
    this._BISvc.getTocaAndCurrencies(event.args.item.originalItem.code);
    this.register();
  }

  selectConcernsIndicator(event: any) { //spdr
    this._AdmAcmService.setSpdr(event.args.item.originalItem.code);
    this.register();
  }

  getBillingPeriod(): void {
    const x: number = this.period.getSelectedItem().value();
    const m: number = this.periodMonth.getSelectedItem().value();
    const y: number = this.periodYear.getSelectedItem().value();

    if (x != null && m != null && y != null ) {
      this.basicInfo.billingPeriod = Number(y.toString() + m.toString() + x.toString());
      this.register();
    }

  }

  selectCurrency(event: any) {
    this.basicInfo.currency = {
      code: event.args.item.originalItem.code,
      decimals: event.args.item.originalItem.decimals
    };

    this._AdmAcmService.setDecimals(event.args.item.originalItem.decimals);
    this.register();
  }

  setNetReporting(value: boolean) {
    this._AdmAcmService.setSpan(value);
    this.register();
  }

  selectSubtype(event: any) {
    this._AdmAcmService.setSubtype(event.args.item.originalItem.code);
    this.register();
  }

  closeAgent() {
    this.popAgent.close();
  }

  closeAirline() {
    this.popAirline.close();
  }
}
