import { DetailsService } from './../../services/details.service';
import { Component, OnInit } from '@angular/core';
import { TicketDocument } from '../../models/ticket-document.model';

@Component({
  selector: 'app-document-pop-up',
  templateUrl: './document-pop-up.component.html',
  styleUrls: ['./document-pop-up.component.scss']
})
export class DocumentPopUpComponent implements OnInit {

  ticket: TicketDocument;

  constructor (private _DetailsService: DetailsService) {
    this._DetailsService.getTicket().subscribe( data => this.ticket = data);
  }

  ngOnInit() {
  }

}
