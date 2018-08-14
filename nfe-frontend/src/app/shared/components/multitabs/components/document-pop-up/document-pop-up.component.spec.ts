import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { DocumentPopUpComponent } from './document-pop-up.component';

describe('DocumentPopUpComponent', () => {
  let component: DocumentPopUpComponent;
  let fixture: ComponentFixture<DocumentPopUpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [

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
