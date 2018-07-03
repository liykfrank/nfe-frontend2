import { DetailsService } from './../../services/details.service';
import { Component, OnInit, Input } from '@angular/core';
import { TicketDocument } from '../../models/ticket-document.model';

@Component({
  selector: 'app-documents',
  templateUrl: './documents.component.html',
  styleUrls: ['./documents.component.scss']
})
export class DocumentsComponent implements OnInit {
  @Input() notice = 0;

  documents: TicketDocument[] = [];
  private num: number = 0;

  constructor(private _DetailsService: DetailsService) {
    this._DetailsService.getRelatedTicketDocuments().subscribe(elems => {
      this.notice = elems.length - this.num;
      this.notice = this.notice < 0 ? 0 : this.notice;

      this.documents = elems;
      this.num = this.documents.length;
    });
  }

  ngOnInit() {

  }

  deleteElem(item) {
    this.documents.splice(item, 1);
    this._DetailsService.setRelatedTicketDocuments(this.documents);
  }

  showTicket(ticket: TicketDocument) {
    console.log('on show');
    this._DetailsService.setTicket(ticket);
  }

}
