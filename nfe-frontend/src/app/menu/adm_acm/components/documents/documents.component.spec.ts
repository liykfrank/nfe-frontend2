import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentsComponent } from './documents.component';
import { SharedModule } from '../../../../shared/shared.module';
import { DetailsService } from '../../services/details.service';
import { Observable } from 'rxjs/Observable';
import { TicketDocument } from '../../models/ticket-document.model';

describe('DocumentsComponent', () => {
  let component: DocumentsComponent;
  let fixture: ComponentFixture<DocumentsComponent>;

  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService',
    ['getRelatedTicketDocuments', 'setRelatedTicketDocuments', 'setTicket']);
  _DetailsService.getRelatedTicketDocuments.and.returnValue(Observable.of([{relatedTicketDocumentNumber: '1111111111111'}]));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule ],
      providers: [
        {provide: DetailsService, useValue: _DetailsService},
      ],
      declarations: [ DocumentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deleteElem', () => {
    _DetailsService.setRelatedTicketDocuments.and.returnValue(Observable.of({}));
    component.deleteElem(0);
    expect(component.documents.length).toBe(0);
    expect(_DetailsService.setRelatedTicketDocuments.calls.count()).toBe(1);
  });

  it('showTicket', () => {
    _DetailsService.setTicket.and.returnValue(Observable.of({}));
    const ticket: TicketDocument = new TicketDocument();
    ticket.relatedTicketDocumentNumber = '1111111111111';
    component.showTicket(ticket);

    expect(_DetailsService.setTicket.calls.count()).toBe(1);
  });


});
