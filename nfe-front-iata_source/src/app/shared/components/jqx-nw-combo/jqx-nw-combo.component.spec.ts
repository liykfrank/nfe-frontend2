import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JqxNwComboComponent } from './jqx-nw-combo.component';

describe('JqxNwComboComponent', () => {
  let component: JqxNwComboComponent;
  let fixture: ComponentFixture<JqxNwComboComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JqxNwComboComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JqxNwComboComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
