import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AirlineRfComponent } from './airline-rf.component';

xdescribe('AirlineRfComponent', () => {
  let component: AirlineRfComponent;
  let fixture: ComponentFixture<AirlineRfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AirlineRfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AirlineRfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
