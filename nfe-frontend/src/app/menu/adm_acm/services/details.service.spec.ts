import { TestBed, inject } from '@angular/core/testing';

import { DetailsService } from './details.service';
import { ReasonService } from './resources/reason.service';
import { Observable } from 'rxjs/Observable';
import { TicketDocument } from '../models/ticket-document.model';
import { Acdm } from '../models/acdm.model';

describe('DetailsService', () => {

  const HTTP = jasmine.createSpyObj<ReasonService>('ReasonService', ['get']);
  HTTP.get.and.returnValue(Observable.of({}));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        DetailsService,
        {
          provide: ReasonService,
          useValue: HTTP
        }
      ]
    });
  });

  it('should be created', inject([DetailsService], (service: DetailsService) => {
    expect(service).toBeTruthy();
  }));

  it('getCheckDigitList', inject([DetailsService], (service: DetailsService) => {
    expect(service.getCheckDigitList()).toBeTruthy();
  }));

  it('getRelatedTicketDocuments', inject([DetailsService], (service: DetailsService) => {
    expect(service.getRelatedTicketDocuments()).toBeTruthy();
  }));

  it('getTicket', inject([DetailsService], (service: DetailsService) => {
    expect(service.getTicket()).toBeTruthy();
  }));

  it('getReasons', inject([DetailsService], (service: DetailsService) => {
    expect(service.getReasons()).toBeTruthy();
  }));

  it('getDateOfIssueRelatedDocument', inject([DetailsService], (service: DetailsService) => {
    expect(service.getDateOfIssueRelatedDocument()).toBeTruthy();
  }));

  it('getPassenger', inject([DetailsService], (service: DetailsService) => {
    expect(service.getPassenger()).toBeTruthy();
  }));

  it('getReasonForMemoIssuanceCode', inject([DetailsService], (service: DetailsService) => {
    expect(service.getReasonForMemoIssuanceCode()).toBeTruthy();
  }));

  it('getReasonForMemo', inject([DetailsService], (service: DetailsService) => {
    expect(service.getReasonForMemo()).toBeTruthy();
  }));

  it('copyToAdcm', inject([DetailsService], (service: DetailsService) => {
    const ticket: TicketDocument = new TicketDocument();
    ticket.relatedTicketDocumentNumber = '111111';
    ticket.checkDigit = 1;
    service.setRelatedTicketDocuments([ticket]);

    const dateOfIssueRelatedDocument = 'dateOfIssueRelatedDocument';
    service.setDateOfIssueRelatedDocument(dateOfIssueRelatedDocument);

    const passenger = 'passenger';
    service.setPassenger(passenger);


    const reasonForMemoIssuanceCode = 'reasonForMemoIssuanceCode';
    service.setReasonForMemoIssuanceCode(reasonForMemoIssuanceCode);

    const reasonForMemo = 'reasonForMemo';
    service.setReasonForMemo(reasonForMemo);

    const acdm: Acdm = new Acdm();
    service.copyToAdcm(acdm);

    console.log(acdm.relatedTicketDocuments); console.log(ticket);

    expect(acdm.dateOfIssueRelatedDocument == dateOfIssueRelatedDocument).toBe(true, 'error dateOfIssueRelatedDocument');
    expect(acdm.passenger == passenger).toBe(true, 'error passenger');
    expect(acdm.relatedTicketDocuments[0].checkDigit == ticket.checkDigit).toBe(true, 'error relatedTicketDocuments');
    expect(acdm.relatedTicketDocuments[0].relatedTicketDocumentNumber == ticket.relatedTicketDocumentNumber)
              .toBe(true, 'error relatedTicketDocuments');
    expect(acdm.reasonForMemoIssuanceCode == reasonForMemoIssuanceCode).toBe(true, 'error reasonForMemoIssuanceCode');
    expect(acdm.reasonForMemo == reasonForMemo).toBe(true, 'error reasonForMemo');
  }));

  it('copyFromAdcm', inject([DetailsService], (service: DetailsService) => {
    let observeDateOfIssueRelatedDocument, observePassenger, observeRelatedTicketDocuments
          , observeReasonForMemoIssuanceCode, observeReasonForMemo;

    service.getDateOfIssueRelatedDocument().subscribe(data => observeDateOfIssueRelatedDocument = data);
    service.getPassenger().subscribe(data => observePassenger = data);
    service.getRelatedTicketDocuments().subscribe(data => observeRelatedTicketDocuments = data);
    service.getReasonForMemoIssuanceCode().subscribe(data => observeReasonForMemoIssuanceCode = data);
    service.getReasonForMemo().subscribe(data => observeReasonForMemo = data);

    const ticket: TicketDocument = new TicketDocument();
    ticket.checkDigit = 1;
    ticket.relatedTicketDocumentNumber =  '111111';

    const acdm: Acdm = new Acdm();
    acdm.dateOfIssueRelatedDocument = 'dateOfIssueRelatedDocument';
    acdm.passenger = 'passenger';
    acdm.relatedTicketDocuments = [ticket];
    acdm.reasonForMemoIssuanceCode = 'reasonForMemoIssuanceCode';
    acdm.reasonForMemo = 'reasonForMemo';

    service.copyFromAdcm(acdm);
    expect(observeDateOfIssueRelatedDocument == acdm.dateOfIssueRelatedDocument).toBe(true);
    expect(observePassenger == acdm.passenger).toBe(true);
    expect(observeRelatedTicketDocuments == acdm.relatedTicketDocuments).toBe(true);
    expect(observeReasonForMemoIssuanceCode == acdm.reasonForMemoIssuanceCode).toBe(true);
    expect(observeReasonForMemo == acdm.reasonForMemo).toBe(true);

  }));

  it('setTicket', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getTicket().subscribe(data => observable = data);
    const ticket: TicketDocument = new TicketDocument();
    ticket.relatedTicketDocumentNumber = '111111';
    ticket.checkDigit = 1;
    service.setTicket(ticket);

    expect(observable.relatedTicketDocumentNumber == ticket.relatedTicketDocumentNumber).toBe(true);
    expect(observable.checkDigit == ticket.checkDigit).toBe(true);
  }));

  it('setPassenger', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getPassenger().subscribe(data => observable = data);
    const text = 'passenger';
    service.setPassenger(text);
    expect(observable == text).toBe(true);
  }));

  it('setDateOfIssueRelatedDocument', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getDateOfIssueRelatedDocument().subscribe(data => observable = data);
    const text = 'dateOfIssueRelatedDocument';
    service.setDateOfIssueRelatedDocument(text);
    expect(observable == text).toBe(true);
  }));

  it('setReasonForMemoIssuanceCode', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getReasonForMemoIssuanceCode().subscribe(data => observable = data);

    const text = 'reasonForMemoIssuanceCode';
    service.setReasonForMemoIssuanceCode(text);
    expect(observable == text).toBe(true);
  }));

  it('setReasonForMemo', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getReasonForMemo().subscribe(data => observable = data);

    const text = 'reasonForMemo';
    service.setReasonForMemo(text);
    expect(observable == text).toBe(true);
  }));

  it('removeElem', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getRelatedTicketDocuments().subscribe(data => observable = data);

    const ticket: TicketDocument = new TicketDocument();
    ticket.relatedTicketDocumentNumber = '111111';
    ticket.checkDigit = 1;
    service.setRelatedTicketDocuments([ticket]);

    expect(observable[0].relatedTicketDocumentNumber == ticket.relatedTicketDocumentNumber).toBe(true);
    expect(observable[0].checkDigit == ticket.checkDigit).toBe(true);

    service.removeElem(0);

    expect(observable.length).toBe(0);
  }));

  it('pushRelatedTicketDocument', inject([DetailsService], (service: DetailsService) => {
    let observable;
    service.getRelatedTicketDocuments().subscribe(data => observable = data);

    expect(observable.length).toBe(0);

    const ticket: TicketDocument = new TicketDocument();
    ticket.relatedTicketDocumentNumber = '111111';
    ticket.checkDigit = 1;
    service.pushRelatedTicketDocument(ticket);
    expect(observable.length).toBe(1);

    service.pushRelatedTicketDocument(ticket);
    expect(observable.length).toBe(2);
  }));

});
