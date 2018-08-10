import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormOfPaymentRefundComponent } from './form-of-payment-refund.component';

xdescribe('FormOfPaymentComponent', () => {
  let component: FormOfPaymentRefundComponent;
  let fixture: ComponentFixture<FormOfPaymentRefundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FormOfPaymentRefundComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormOfPaymentRefundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
