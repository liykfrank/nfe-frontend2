import { PatternDirective } from './pattern.directive';
import { Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

@Component({
  template: `<input type="number" fbInputlistener value="12345">`
})
class TestInputComponent {
}

describe('PatternDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestInputComponent, PatternDirective]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
 });

  it('should create an instance', () => {
    const directive = new PatternDirective(inputEl);
    expect(directive).toBeTruthy();
  });
});
