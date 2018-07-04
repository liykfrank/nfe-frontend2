import { DecimalsFormatterDirectiveDirective } from './decimals-formatter-directive.directive';
import { Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DecimalsFormatterPipe } from './decimals-formatter.pipe';


@Component({
  template: `<input type="number" fbInputlistener value="12345">`
})
class TestInputComponent {
}

describe('DecimalsFormatterDirectiveDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DecimalsFormatterPipe],
      declarations: [TestInputComponent, DecimalsFormatterDirectiveDirective]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
 });

  it('should create an instance', () => {
    const directive = new DecimalsFormatterDirectiveDirective(inputEl, new DecimalsFormatterPipe());
    expect(directive).toBeTruthy();
  });
});
