import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Injectable } from '@angular/core';
import { TicketDocument } from '../models/ticket-document.model';
import { Observable } from 'rxjs/Observable';
import { ReasonService } from './resources/reason.service';
import { Reason } from '../models/reason.model';
import { Acdm } from './../models/acdm.model';

@Injectable()
export class DetailsService {
  private $dateOfIssueRelatedDocument = new BehaviorSubject<string>('');
  private $passenger = new BehaviorSubject<string>('');
  private $relatedTicketDocuments = new BehaviorSubject<TicketDocument[]>([]);
  private $reasonForMemoIssuanceCode = new BehaviorSubject<string>('');
  private $reasonForMemo = new BehaviorSubject<string>('');

  // ticket to show
  private $ticket = new BehaviorSubject<TicketDocument>(null);

  // combo
  private $reasons = new BehaviorSubject<Reason[]>([]);

  constructor(private _ReasonService: ReasonService) { }

  public copyToAdcm(acdm: Acdm): void {
    acdm.dateOfIssueRelatedDocument = this.$dateOfIssueRelatedDocument.getValue();
    acdm.passenger =                  this.$passenger.getValue();
    acdm.relatedTicketDocuments =     this.$relatedTicketDocuments.getValue();
    acdm.reasonForMemoIssuanceCode =  this.$reasonForMemoIssuanceCode.getValue();
    acdm.reasonForMemo =              this.$reasonForMemo.getValue();
  }

  public copyFromAdcm(acdm: Acdm): void {
    this.$dateOfIssueRelatedDocument.next(acdm.dateOfIssueRelatedDocument);
    this.$passenger.next(acdm.passenger);
    this.$relatedTicketDocuments.next(acdm.relatedTicketDocuments);
    this.$reasonForMemoIssuanceCode.next(acdm.reasonForMemoIssuanceCode);
    this.$reasonForMemo.next(acdm.reasonForMemo);
  }

  public getCheckDigitList(): string[] {
    return ['', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
  }

  public removeElem(index: number): void {
    const aux = this.$relatedTicketDocuments.getValue();
    aux.splice(index, 1);
    this.setRelatedTicketDocuments(this.$relatedTicketDocuments.getValue());
  }

  public pushRelatedTicketDocument(input: TicketDocument): void {
    const list: TicketDocument[] = this.$relatedTicketDocuments.getValue();
    list.push(input);
    this.setRelatedTicketDocuments(list);
  }

  public getDateOfIssueRelatedDocument(): Observable<string> {
    return this.$dateOfIssueRelatedDocument.asObservable();
  }

  public setDateOfIssueRelatedDocument(str: string): void {
    this.$dateOfIssueRelatedDocument.next(str);
  }

  public getPassenger(): Observable<string> {
    return this.$passenger.asObservable();
  }

  public setPassenger(str: string): void {
    this.$passenger.next(str);
  }

  public getRelatedTicketDocuments(): Observable<TicketDocument[]> {
    return this.$relatedTicketDocuments.asObservable();
  }

  public setRelatedTicketDocuments(inputList: TicketDocument[]): void {
    this.$relatedTicketDocuments.next(inputList);
  }

  public getReasonForMemoIssuanceCode(): Observable<string> {
    return this.$reasonForMemoIssuanceCode.asObservable();
  }

  public setReasonForMemoIssuanceCode(str: string): void {
    this.$reasonForMemoIssuanceCode.next(str);
  }

  public getReasonForMemo(): Observable<string> {
    return this.$reasonForMemo.asObservable();
  }

  public setReasonForMemo(str: string): void {
    this.$reasonForMemo.next(str);
  }

  public getTicket(): Observable<TicketDocument> {
    return this.$ticket.asObservable();
  }

  public setTicket(ticket: TicketDocument): void {
    this.$ticket.next(ticket);
  }

  public getReasons(): Observable<Reason[]> {
    return this.$reasons.asObservable();
  }

  public getReasonsOnISO(iso: String) {

    if (iso.length > 0) {
      let param: any = {};
      param.isoCountryCode = iso;

      this._ReasonService.get(param).subscribe(data => this.$reasons.next(data));
    }
  }

}
