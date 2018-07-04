import { jqxPopoverComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxpopover';
import { ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Contact } from '../../models/contact.model';
import { AlertsService } from '../../../core/services/alerts.service';
import { AlertType } from '../../../core/models/alert-type.enum';
@Component({
  selector: 'app-agent',
  templateUrl: './agent.component.html',
  styleUrls: ['./agent.component.scss']
})
export class AgentComponent implements OnInit {
  private MODULE = 7;
  private CODE_LENGTH = 7;

  @Input('style') style: string = '';


  @Input('agentCode') agentCode: string;
  @Input('agentCheckDigit') agentCheckDigit: string;
  @Input('vatNumber') vatNumber: string;
  @Input('companyReg') companyReg; //PREGUNTAR

  @Input('showVat') showVat: boolean = true;
  @Input('showMoreDetails') showMoreDetails: boolean = true;
  @Input('showCompanyReg') showCompanyReg: boolean = true;
  @Input('showContact') showContact: boolean = true;

  @Input('name') name: string;
  @Input('telephone') telephone: string;
  @Input('email') email: string;
  @Input('height') height: number = 40;


  // tslint:disable-next-line:no-output-on-prefix
  @Output() onClickMoreDetails: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output() onChangeAgentCode: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output() onChangeVatNumber: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output() onChangeCompanyReg: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output() onChangeContact: EventEmitter<any> = new EventEmitter();

  constructor(private _AlertsService: AlertsService) {}

  ngOnInit() {}

  onValueChanged() {
    if (this.agentCode.toString().length == this.CODE_LENGTH && this.agentCheckDigit != null) {
      const control = Number(this.agentCode) % this.MODULE;

      if (control == Number(this.agentCheckDigit)) {
        const concatCode = Number(this.agentCode.toString() + this.agentCheckDigit.toString());

        this.onChangeAgentCode.emit(concatCode.toString());
      } else {
        this._AlertsService.setAlertTranslate('AGENT.title_error', 'AGENT.check_digit_error', AlertType.ERROR);
        this.agentCheckDigit = null;
      }
    }
  }

  changeVatNumber() {
    this.onChangeVatNumber.emit(this.vatNumber);
  }

  changeCompanyReg() {
    this.onChangeCompanyReg.emit(this.companyReg);
  }

  changeContact(value) {
    this.onChangeContact.emit(value);
  }

  clickMoreDetails() {
    this.onClickMoreDetails.emit();
  }

  checkDisableShowDetails () {
    return  !this.agentCode ||
            this.agentCode.length != this.CODE_LENGTH ||
            !this.agentCheckDigit ||
            this.agentCheckDigit.length != 1;
  }

  setStyle(text: string): boolean {
    return this.style == 'error' && (!text || text.length == 0);
  }

}
