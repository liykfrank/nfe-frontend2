import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedModule } from './../../../../shared/shared.module';
import { DetailsRefundComponent } from './details-refund.component';

xdescribe('DetailsRefundComponent', () => {
  let component: DetailsRefundComponent;
  let fixture: ComponentFixture<DetailsRefundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [DetailsRefundComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailsRefundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
