import { Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { DecimalsFormatterDirective } from './decimals-formatter.directive';
import { DecimalsFormatterPipe } from '../pipes/decimals-formatter.pipe';


@Component({
  template:
    `<input type="text"
        decimalsFormatterDirective
        [decimals]="2"
      />`
})
class TestInputComponent {
}

describe('DecimalsFormatterDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ DecimalsFormatterPipe ],
      declarations: [
        TestInputComponent,
        DecimalsFormatterDirective
      ]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
 });

  it('should create an instance', () => {
    const directive = new DecimalsFormatterDirective(inputEl, new DecimalsFormatterPipe());
    expect(directive).toBeTruthy();
  });

});
