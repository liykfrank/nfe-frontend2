import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReasonsTextAreaComponent } from './reasons-text-area.component';

describe('ReasonsTextAreaComponent', () => {
  let component: ReasonsTextAreaComponent;
  let fixture: ComponentFixture<ReasonsTextAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReasonsTextAreaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReasonsTextAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
