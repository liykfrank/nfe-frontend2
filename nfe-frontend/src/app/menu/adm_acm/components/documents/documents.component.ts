import { DetailsService } from './../../services/details.service';
import { Component, OnInit, Input } from '@angular/core';
import { TicketDocument } from '../../models/ticket-document.model';

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {

  documents: TicketDocument[] = [];

  constructor(private _DetailsService: DetailsService) {
    this._DetailsService.getRelatedTicketDocuments().subscribe(elems => {
      this.documents = elems;
    });
  }

  ngOnInit() { }

  deleteElem(item) {
    this.documents.splice(item, 1);
    this._DetailsService.setRelatedTicketDocuments(this.documents);
  }

  showTicket(ticket: TicketDocument) {
    this._DetailsService.setTicket(ticket);
  }

}
