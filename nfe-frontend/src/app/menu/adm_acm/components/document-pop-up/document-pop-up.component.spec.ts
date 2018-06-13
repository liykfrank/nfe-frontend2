import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentPopUpComponent } from './document-pop-up.component';

xdescribe('DocumentPopUpComponent', () => {
  let component: DocumentPopUpComponent;
  let fixture: ComponentFixture<DocumentPopUpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
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
