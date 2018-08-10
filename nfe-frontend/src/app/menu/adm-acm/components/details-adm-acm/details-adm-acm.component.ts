import { ReactiveFormHandler } from './../../../../shared/base/reactive-form-handler';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { Alignment } from '../../../../shared/enums/alignment.enum';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { AcdmConfigurationService } from '../../services/adm-acm-configuration.service';

// import { AlertType } from '../../../../core/enums/alert-type.enum';
// import { AlertsService } from '../../../../core/services/alerts.service';
// import { TicketDocument } from '../../../../shared/components/multitabs/models/ticket-document.model';
// import { Reason } from '../../../../shared/models/reason.model';
// import { AdmAcmService } from './../../services/adm-acm.service';
// import { DetailsService } from './../../services/details.service';
// import { AdmAcmConfiguration } from '../../../../shared/models/adm-acm-configuration.model';

@Component({
  selector: 'bspl-details-adm-acm',
  templateUrl: './details-adm-acm.component.html',
  styleUrls: ['./details-adm-acm.component.scss']
})
export class DetailsAdmAcmComponent extends ReactiveFormHandler implements OnInit {

  public reasonsAlignment = Alignment.VERTICAL;
  public type = EnvironmentType.ACDM;
  configuration: AdmAcmConfiguration;

  constructor(private _admAcmConfigurationService: AcdmConfigurationService) {
    super();
    this.subscriptions.push(this._admAcmConfigurationService.getConfiguration().subscribe(data => {
      this.configuration = data;
    }));
  }

  ngOnInit() {

  }

  onReturnFormReasons(event) {

  }

  // documentNumber = '';

  // topTenList: Reason[] = [];

  // relatedDocument: Date;
  // passenger: string;
  // reasonForMemoIssuanceCode: string;
  // reasonForMemo: string;

  // elemSelect: Reason;
  // private conf: AdmAcmConfiguration;

  // @Input() tickets: TicketDocument[] = [];

  // @Output() pushTicket: EventEmitter<TicketDocument> = new EventEmitter();

  // constructor(
  //   private _DetailsService: DetailsService,
  //   private _AdmAcmService: AdmAcmService,
  //   private _AlertsService: AlertsService /* ,
  //   private _MultitabService: MultitabService */
  // ) {
  //   this._AdmAcmService.getConfiguration().subscribe(data => {
  //     this.conf = data;
  //     this._DetailsService.getReasonsOnISO(data.isoCountryCode);
  //   });

  //   this._DetailsService.getReasons().subscribe(data => {
  //     this.topTenList = data;
  //   });

  //   /*
  //   this._MultitabService.getTickets().subscribe(data => {
  //     this.tickets = data;
  //     this._DetailsService.setRelatedTicketDocuments(data);
  //   });
  //   */
  // }

  // ngOnInit() {}

  // onRelatedDocumentChange() {
  //   this._DetailsService.setDateOfIssueRelatedDocument(
  //     this.relatedDocument.toISOString()
  //   );
  // }

  // onPasengerChange() {
  //   this._DetailsService.setPassenger(this.passenger);
  // }

  // onReasonForMemoIssuanceCodeChange() {
  //   this._DetailsService.setReasonForMemoIssuanceCode(
  //     this.reasonForMemoIssuanceCode
  //   );
  // }

  // onReasonForMemoChange() {
  //   this._DetailsService.setReasonForMemo(this.reasonForMemo);
  // }

  // checkNumbers() {
  //   this.documentNumber = this.documentNumber.replace(/\D/g, '');
  // }

  // addDocument() {
  //   if (
  //     this.conf.maxNumberOfRelatedDocuments == -1 ||
  //     this.tickets.length < this.conf.maxNumberOfRelatedDocuments
  //   ) {
  //     const find = this.tickets.findIndex(
  //       x => x.relatedTicketDocumentNumber == this.documentNumber
  //     );

  //     if (find == -1) {
  //       const ticket: TicketDocument = new TicketDocument();
  //       ticket.relatedTicketDocumentNumber = this.documentNumber;
  //       this.pushTicket.emit(ticket);
  //     } else {
  //       this._AlertsService.setAlertTranslate(
  //         'ADM_ACM.DETAILS.error.tickets-title',
  //         'ADM_ACM.DETAILS.error.alredy-added',
  //         AlertType.WARN
  //       );
  //     }

  //     this.documentNumber = '';
  //   } else {
  //     this._AlertsService.setAlertTranslate(
  //       'ADM_ACM.DETAILS.error.tickets-title',
  //       'ADM_ACM.DETAILS.error.detail-max-tickets',
  //       AlertType.ERROR
  //     );
  //   }
  // }

  // topTenSelect() {
  //   this.reasonForMemo = this.elemSelect.detail;
  //   this.onReasonForMemoChange();
  // }
}
