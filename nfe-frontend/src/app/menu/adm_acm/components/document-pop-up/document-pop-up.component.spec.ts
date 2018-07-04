import { Observable } from 'rxjs/Observable';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentPopUpComponent } from './document-pop-up.component';
import { DetailsService } from '../../services/details.service';

describe('DocumentPopUpComponent', () => {
  let component: DocumentPopUpComponent;
  let fixture: ComponentFixture<DocumentPopUpComponent>;

  const _DetailsService = jasmine.createSpyObj<DetailsService>('DetailsService', ['getTicket']);
  _DetailsService.getTicket.and.returnValue(Observable.of({}));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: DetailsService, useValue: _DetailsService}
      ],
      declarations: [ DocumentPopUpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentPopUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
