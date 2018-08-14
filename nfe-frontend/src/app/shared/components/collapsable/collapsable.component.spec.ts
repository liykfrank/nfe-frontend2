import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CollapsableComponent } from './collapsable.component';

describe('CollapsableComponent', () => {
  let component: CollapsableComponent;
  let fixture: ComponentFixture<CollapsableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CollapsableComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CollapsableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onShowHide', () => {
    let data = component.open;
    component.showHide.subscribe(val => (data = val));

    expect(component.open).toBe(true, 'First');
    component.onShowHide();
    fixture.detectChanges();
    expect(component.open).toBe(false, 'Second');
  });

  it('checkOpen', () => {
    expect(component.checkOpen()).toBe(true);
  });
});
