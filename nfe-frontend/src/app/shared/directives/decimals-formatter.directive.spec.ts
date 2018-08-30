import { Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule, NgControl } from '@angular/forms';
import { By } from '@angular/platform-browser';

import { DecimalsFormatterPipe } from '../pipes/decimals-formatter.pipe';
import { DecimalsFormatterDirective } from './decimals-formatter.directive';

@Component({
  template: `<input type="text" decimalsFormatterDirective [decimals]="2" />`
})
class TestInputComponent {}

describe('DecimalsFormatterDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  const ngControlStub = jasmine.createSpyObj<NgControl>('NgControl', ['control']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      providers: [DecimalsFormatterPipe, { provide: NgControl, useValue: ngControlStub }],
      declarations: [TestInputComponent, DecimalsFormatterDirective]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
  });

  it('should create an instance', () => {
    const directive = new DecimalsFormatterDirective(inputEl, new DecimalsFormatterPipe(), ngControlStub);

    expect(directive).toBeTruthy();
  });
});
