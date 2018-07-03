import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Injectable } from '@angular/core';
import { TicketDocument } from '../models/ticket-document.model';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DetailsService {

  private relatedTicketDocuments = new BehaviorSubject<TicketDocument[]>([]);

  private ticket = new BehaviorSubject<TicketDocument>(null);

  constructor() { }

  getCheckDigitList(): string[] {
    //return ['', 0, 1, 2, 3, 4, 5, 6, 7, 8, 9];
    return ['', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
  }

  public getRelatedTicketDocuments(): Observable<TicketDocument[]> {
    return this.relatedTicketDocuments.asObservable();
  }

  public setRelatedTicketDocuments(inputList: TicketDocument[]): void {
    console.log('on setRelatedTicketDocuments');
    console.log(inputList);
    this.relatedTicketDocuments.next(inputList);
  }

  public removeElem(index: number): void {
    let list = this.relatedTicketDocuments.getValue().splice(index, 1);
    this.setRelatedTicketDocuments(list);
  }

  public pushRelatedTicketDocument(input: TicketDocument): void {
    console.log('on pushRelatedTicketDocument');
    const list: TicketDocument[] = this.relatedTicketDocuments.getValue();
    console.log(list);
    list.push(input);
    console.log(list);
    this.setRelatedTicketDocuments(list);
  }

  public getTicket(): Observable<TicketDocument> {
    return this.ticket.asObservable();
  }

  public setTicket(ticket: TicketDocument): void {
    this.ticket.next(ticket);
  }
}
