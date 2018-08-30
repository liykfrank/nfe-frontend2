import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ScrollPanelModule } from 'primeng/primeng';

import { TicketDocument } from '../../models/ticket-document.model';
import { DocumentsComponent } from './documents.component';

describe('DocumentsComponent', () => {
  let component: DocumentsComponent;
  let fixture: ComponentFixture<DocumentsComponent>;

  const elem1 = new TicketDocument(),
    elem2 = new TicketDocument();

  elem1.relatedTicketDocumentNumber = '111';
  elem2.relatedTicketDocumentNumber = '222';

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ScrollPanelModule],
      declarations: [DocumentsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentsComponent);
    component = fixture.componentInstance;

    component.documents = [elem1, elem2];

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deleteElem', () => {
    let aux;
    component.removeTicket.subscribe(data => (aux = data));

    component.deleteElem(1);
    expect(aux).toBe(1);
  });

  it('onClickTicket', () => {
    let aux;
    component.showTicket.subscribe(data => (aux = data));
    component.onClickTicket(elem2);
    expect(aux).toBe(elem2);
  });
});
