import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DivCollapsableComponent } from './div-collapsable.component';

xdescribe('DivCollapsableComponent', () => {
  let component: DivCollapsableComponent;
  let fixture: ComponentFixture<DivCollapsableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DivCollapsableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DivCollapsableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
