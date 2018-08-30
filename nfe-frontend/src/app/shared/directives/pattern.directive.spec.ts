import { Component, DebugElement, Renderer } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { PatternDirective } from './pattern.directive';

@Component({
  template: `<input type="number" fbInputlistener value="12345">`
})
class TestInputComponent {}

describe('PatternDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  const rendererMock = jasmine.createSpyObj('rendererMock', [
    'setElementStyle'
  ]);

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TestInputComponent, PatternDirective]
    }).overrideComponent(TestInputComponent, {
      set: {
        providers: [{ provide: Renderer, useValue: rendererMock }]
      }
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
  });

  it('should create an instance', () => {
    const directive = new PatternDirective(rendererMock, inputEl);
    expect(directive).toBeTruthy();
  });
});
