import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactRfComponent } from './contact-rf.component';

xdescribe('ContactRfComponent', () => {
  let component: ContactRfComponent;
  let fixture: ComponentFixture<ContactRfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContactRfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContactRfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
