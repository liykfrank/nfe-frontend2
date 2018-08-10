import { Component, EventEmitter, Injector, Input, Output } from '@angular/core';
import { TicketDocument } from '../../models/ticket-document.model';

@Component({
  selector: 'bspl-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent {

  @Input() documents: TicketDocument[] = [];

  @Output() removeTicket = new EventEmitter();
  @Output() showTicket = new EventEmitter();

  constructor() {
  }

  deleteElem(index: number) {
    this.removeTicket.emit(this.documents.splice(index, 1));
  }

  onClickTicket(ticket: TicketDocument) {
    this.showTicket.emit(ticket);
  }

}
