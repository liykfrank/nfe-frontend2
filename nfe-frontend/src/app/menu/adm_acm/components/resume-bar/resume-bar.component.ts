import { Component, OnInit } from '@angular/core';
import { ScreenType } from './../../../../shared/models/screen-type.enum';

import { AdmAcmService } from './../../services/adm-acm.service';
import { AmountService } from './../../services/amount.service';
import { BasicInfoService } from './../../services/basic-info.service';

@Component({
  selector: 'app-resume-bar',
  templateUrl: './resume-bar.component.html',
  styleUrls: ['./resume-bar.component.scss']
})
export class ResumeBarComponent implements OnInit {

  spdr: string;
  issue_date: Date = new Date();
  status: string;
  amount_format: string;
  total_amount: number;
  agent_code: string;

  constructor(
    private _AdmAcmService: AdmAcmService,
    private _AmountService: AmountService,
    private _BasicInfoService: BasicInfoService
  ) {
    this._AdmAcmService.getSpdr().subscribe(spdr => {
      this.spdr = spdr || '-';
    });

    this._AdmAcmService.getScreenType().subscribe(screenType => {

      // TODO: añadir el sistema de traducciones, o revisar esta parte para
      //      verificar el valor del ENUM y el resultado de la variable.
      this.status = screenType === ScreenType.CREATE ? 'NEW' : 'DETAIL';
    });

    this._AdmAcmService.getDecimals().subscribe(decimals => {
      this.amount_format = '.' + decimals;
    });

    this._AmountService.getTotal().subscribe(total => {
      this.total_amount = total;
    });

    this._BasicInfoService.getAgent().subscribe(agent => {
      console.log(agent);
      this.agent_code = agent ? agent.iataCode : '-';
    });
  }

  ngOnInit() {

  }

}
