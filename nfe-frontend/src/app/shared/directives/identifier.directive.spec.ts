import { Component, DebugElement, Renderer2 } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

import { IdentifierDirective } from './identifier.directive';

@Component({
  template: `<div appIdentifier [formGroup]="test" [base]="'testname'">
    <input formControlName="elem">
  </div>`
})
class TestInputComponent {
  test = new FormGroup({
    elem: new FormControl()
  });
}

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
      providers: [
        { provide: Renderer2, useValue: rendererMock }

      ],
      declarations: [
        TestInputComponent,
        IdentifierDirective
      ]
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
