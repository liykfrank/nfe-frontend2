import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasonsSelectorComponent } from './reasons-selector.component';

describe('ReasonsSelectorComponent', () => {
  let component: ReasonsSelectorComponent;
  let fixture: ComponentFixture<ReasonsSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasonsSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasonsSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
