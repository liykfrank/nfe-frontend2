import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Contact } from '../../models/contact.model';

@Component({
  selector: 'app-airline',
  templateUrl: './airline.component.html',
  styleUrls: ['./airline.component.scss']
})
export class AirlineComponent implements OnInit {
  @Input('style') style: string = '';
  @Input('disabled')disabled: boolean = false;

  @Input('airlineCode')airlineCode: string;
  @Input('showMoreDetails')showMoreDetails : boolean = true;

  @Input('showVat')showVat: boolean = false;
  @Input('vatNumber')vatNumber: string;

  @Input('showCompanyReg')showCompanyReg: boolean = false;
  @Input('companyReg')companyReg;

  @Input('showContact')showContact: boolean = false;
  @Input('name')name: string;
  @Input('telephone')telephone: string;
  @Input('email')email: string;

  // tslint:disable-next-line:no-output-on-prefix
  @Output()onClickMoreDetails: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output()onChangeAirlineCode: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output()onChangeVatNumber: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output()onChangeCompanyReg: EventEmitter<any> = new EventEmitter();
  // tslint:disable-next-line:no-output-on-prefix
  @Output()onChangeContact: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() { }

  changeAirline() {
    if (this.airlineCode.length == 3) {
      this.onChangeAirlineCode.emit(this.airlineCode);
    }
  }

  changeVatNumber() {
    this.onChangeVatNumber.emit(this.vatNumber);
  }

  changeCompanyReg() {
    this.onChangeCompanyReg.emit(this.companyReg);
  }

  changeContact(contact) {
    this.onChangeContact.emit(contact);
  }

  clickMoreDetails() {
    this.onClickMoreDetails.emit();
  }

}
