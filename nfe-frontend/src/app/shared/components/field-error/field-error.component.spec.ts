import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationService } from 'angular-l10n';

import { FieldErrorComponent } from './field-error.component';

describe('FieldErrorComponent', () => {
  let component: FieldErrorComponent;
  let fixture: ComponentFixture<FieldErrorComponent>;

  beforeEach(async(() => {
    const translationServiceStub = {
      translate: () => ({})
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
});
