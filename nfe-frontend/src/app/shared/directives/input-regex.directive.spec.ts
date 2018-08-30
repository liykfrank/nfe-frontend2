import { Component, DebugElement } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { InputRegexDirective } from './input-regex.directive';
import { NgControl } from '@angular/forms';

@Component({
  template: `<input type="text"
        bsplInputRegex
        [regexPattern]="'[A-Z]{2}'"
      />`
})
class TestInputComponent {}

describe('InputAlphaDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  const ngControlStub = jasmine.createSpyObj<NgControl>('NgControl', ['control']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: NgControl, useValue: ngControlStub }],
      declarations: [TestInputComponent, InputRegexDirective]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('input'));
  });

  it('should create an instance', () => {
    const directive = new InputRegexDirective(inputEl, ngControlStub);
    expect(directive).toBeTruthy();
  });
});
