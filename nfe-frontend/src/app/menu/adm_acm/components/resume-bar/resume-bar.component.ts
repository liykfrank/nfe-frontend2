import { Component, OnInit } from '@angular/core';
import { ScreenType } from './../../../../shared/models/screen-type.enum';

import { AdmAcmService } from './../../services/adm-acm.service';
import { AmountService } from './../../services/amount.service';
import { BasicInfoService } from './../../services/basic-info.service';
import { KeyValue } from '../../../../shared/models/key.value.model';
import { Country } from '../../models/country.model';

@Component({
  selector: 'app-resume-bar',
  templateUrl: './resume-bar.component.html',
  styleUrls: ['./resume-bar.component.scss']
})
export class ResumeBarComponent implements OnInit {

  subtype: string;
  concernsIndicator: string;
  issue_date: Date;
  status: string;
  decimals: number;
  total_amount: number;
  agent_code: string;
  currency: string;
  bsp: Country = null;

  private countries: Country[] = [];
  private forList;

  constructor(
    private _AdmAcmService: AdmAcmService,
    private _AmountService: AmountService,
    private _BasicInfoService: BasicInfoService
  ) {

    this._BasicInfoService.getCountries().subscribe(data => {
      if (data) {
        this.countries = data;
      }
    });

    this._AdmAcmService.getScreenType().subscribe(screenType => {

      // TODO: añadir el sistema de traducciones, o revisar esta parte para
      //      verificar el valor del ENUM y el resultado de la variable.
      this.status = screenType === ScreenType.CREATE ? 'NEW' : 'DETAIL';
    });

    this._AdmAcmService.getDecimals().subscribe(decimals => {
      this.decimals = decimals;
    });

    this._AmountService.getTotal().subscribe(total => {
      this.total_amount = total;
    });

    this._BasicInfoService.getBasicInfo().subscribe(data => {

      if (data.isoCountryCode) {
        const iso = data.isoCountryCode;
        this.bsp = this.countries.find((v: any) => v.isoCountryCode == iso);
      } else {
        this.bsp = {isoCountryCode: '-', name: '-', code: '-', description: '-'};
      }

      if (data.agent && data.agent.iataCode && data.agent.iataCode.length > 0) {
        this.agent_code = data.agent.iataCode;
      } else {
        this.agent_code = '-';
      }

      if (data.currency) {
        this.currency = ' ' + data.currency.code;
      } else {
        this.currency = '';
      }

      if (data.concernsIndicator) {
        this.concernsIndicator = this.forList.find(x => x.code == data.concernsIndicator).description;
      } else {
        this.concernsIndicator = '-';
      }

      this.subtype = data.transactionCode || '-';
    });

    this.issue_date = new Date(this._AdmAcmService.getDateOfIssue());
  }

  ngOnInit() {
    this.forList = this._BasicInfoService.getSPDRCombo();
  }

}
