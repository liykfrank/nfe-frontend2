import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AmountRefundComponent } from './amount-refund.component';

xdescribe('AmountRefundComponent', () => {
  let component: AmountRefundComponent;
  let fixture: ComponentFixture<AmountRefundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AmountRefundComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AmountRefundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
