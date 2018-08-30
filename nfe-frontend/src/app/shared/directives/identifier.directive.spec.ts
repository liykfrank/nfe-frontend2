import { Component, DebugElement, Renderer2 } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

import { IdentifierDirective } from './identifier.directive';

@Component({
  template: `<div appIdentifier [formGroup]="test"> <input type="text" formControlName="elem"/> </div>`
})
class TestInputComponent {}

describe('IdentifierDirective', () => {
  let component: TestInputComponent;
  let fixture: ComponentFixture<TestInputComponent>;
  let inputEl: DebugElement;

  const rendererMock = jasmine.createSpyObj<Renderer2>('Renderer2', [
    'setProperty'
  ]);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [TestInputComponent, IdentifierDirective],
      providers: [{ provide: Renderer2, useValue: rendererMock }]
    });

    fixture = TestBed.createComponent(TestInputComponent);
    component = fixture.componentInstance;
    inputEl = fixture.debugElement.query(By.css('div'));
  });

  it('should create an instance', () => {
    const directive = new IdentifierDirective(rendererMock, inputEl);
    expect(directive).toBeTruthy();
  });
});
