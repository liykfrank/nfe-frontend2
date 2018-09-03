import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationService } from 'angular-l10n';

import { FieldErrorComponent } from './field-error.component';

describe('FieldErrorComponent', () => {
  let component: FieldErrorComponent;
  let fixture: ComponentFixture<FieldErrorComponent>;

  let count = 0;

  const errors = {
    backendError: {
      invalid: true,
      message: 'msg'
    },
    required: {
      invalid: true
    },
    pattern: {
      invalid: true
    },
    minlength: {
      invalid: true,
      requiredLength: 1
    },
    customError: {
      message: 'TEST'
    }
  };

  beforeEach(async(() => {
    const translationServiceStub = {
      translate: () => (count++).toString()
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: TranslationService, useValue: translationServiceStub }
      ],
      declarations: [FieldErrorComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('hasErrorClass no errors', () => {
    component.errors = null;
    expect(component.hasErrorClass()).toBe('hide');
    expect(component.errorString).toBeUndefined();
  });

  it('hasErrorClass all errors', () => {
    component.errors = errors;
    component.ngOnChanges(null);
    expect(component.hasErrorClass()).toBe('show');
    expect(component.errorString.length).toBeGreaterThan(1);
  });

  it('hasErrorClass unknown errors', () => {
    component.errors = {test:{invalid: true}};
    component.ngOnChanges(null);
    expect(component.hasErrorClass()).toBe('show');
    expect(component.errorString.length).toBe(0);
  });

});
