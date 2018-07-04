import { Component, Injector, OnInit, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxTextAreaComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtextarea';

import { NwAbstractComponent } from '../../../../shared/base/abstract-component';
import { TicketDocument } from '../../models/ticket-document.model';
import { Configuration } from './../../models/configuration.model';
import { AdmAcmService } from './../../services/adm-acm.service';
import { DetailsService } from './../../services/details.service';
import { Reason } from '../../models/reason.model';
import { AlertsService } from '../../../../core/services/alerts.service';
import { AlertType } from '../../../../core/models/alert-type.enum';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent extends NwAbstractComponent implements OnInit {

  documentNumber = '';

  topTenList: Reason[] = [];

  relatedDocument: Date;
  passenger: string;
  reasonForMemoIssuanceCode: string;
  reasonForMemo: string;

  elemSelect: Reason;
  private conf: Configuration;
  private tickets: TicketDocument[];

  constructor(
    private _Injector: Injector,
    private _DetailsService: DetailsService,
    private _AdmAcmService: AdmAcmService,
    private _AlertsService: AlertsService
  ) {
    super(_Injector);

    this._AdmAcmService.getConfiguration().subscribe(data => {
      this.conf = data;
      this._DetailsService.getReasonsOnISO(data.isoCountryCode);
    });

    this._DetailsService.getReasons().subscribe(data => {
      this.topTenList = data;
    });

    this._DetailsService.getRelatedTicketDocuments().subscribe(data => this.tickets = data);

  }

  ngOnInit() { }

  onRelatedDocumentChange() {
    console.log(this.relatedDocument)
    this._DetailsService.setDateOfIssueRelatedDocument(this.relatedDocument.toISOString());
  }

  onPasengerChange() {
    this._DetailsService.setPassenger(this.passenger);
  }

  onReasonForMemoIssuanceCodeChange() {
    this._DetailsService.setReasonForMemoIssuanceCode(this.reasonForMemoIssuanceCode);
  }

  onReasonForMemoChange() {
    this._DetailsService.setReasonForMemo(this.reasonForMemo);
  }

  checkNumbers() {
    this.documentNumber = this.documentNumber.replace(/\D/g, '');
  }

  addDocument() {
    console.log('max: ' + this.conf.maxNumberOfRelatedDocuments);
    console.log('tickets: ' + this.tickets.length);
    console.log('compare: ' + (this.tickets.length < this.conf.maxNumberOfRelatedDocuments));

    if (this.conf.maxNumberOfRelatedDocuments == -1 || this.tickets.length < this.conf.maxNumberOfRelatedDocuments) {
      console.log('aqui 2');
      const find = this.tickets.findIndex(x => x.relatedTicketDocumentNumber == this.documentNumber);

      if (find == -1) {
        console.log('aqui 3');
        const ticket: TicketDocument = new TicketDocument();
        ticket.relatedTicketDocumentNumber = this.documentNumber;
        this._DetailsService.pushRelatedTicketDocument(ticket);
      } else {
        console.log('aqui 4');
        this._AlertsService.setAlertTranslate('ADM_ACM.DETAILS.error.tickets-title',
                                              'ADM_ACM.DETAILS.error.alredy-added',
                                              AlertType.WARN);
      }

      this.documentNumber = '';
    } else {
      this._AlertsService.setAlertTranslate('ADM_ACM.DETAILS.error.tickets-title',
                                            'ADM_ACM.DETAILS.error.detail-max-tickets',
                                            AlertType.ERROR);
    }
  }

  topTenSelect() {
    this.reasonForMemo = this.elemSelect.detail;
    this.onReasonForMemoChange();
  }

}
