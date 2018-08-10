import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

import { DecimalsFormatterDirective } from '../../directives/decimals-formatter.directive';
import { PatternDirective } from '../../directives/pattern.directive';
import { DecimalsFormatterPipe } from '../../pipes/decimals-formatter.pipe';
import { FieldErrorComponent } from '../field-error/field-error.component';
import { InputFormControlComponent } from './input-form-control.component';

fdescribe('InputFormControlComponent', () => {
  let component: InputFormControlComponent;
  let fixture: ComponentFixture<InputFormControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      providers: [DecimalsFormatterPipe],
      declarations: [
        PatternDirective,
        DecimalsFormatterDirective,
        FieldErrorComponent,
        InputFormControlComponent
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InputFormControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create a default input text when type is not specified', () => {
    const el = fixture.debugElement.query(By.css('input'));
    expect(el.nativeElement.value).toBe('');
  });

  it('should create an input with the decimalsFormatterDirective and its decimals param', () => {
    component.type = 'decimal';
    component.decimalsNumber = 3;

    fixture.detectChanges();

    const el = fixture.debugElement.query(By.css('input'));

    expect(el.attributes.decimalsFormatterDirective).toBe('');
    expect(el.attributes['ng-reflect-decimals']).toBe('3');
  });

  it('should create an input the ownPattern directive with its typingPattern param', () => {
    component.type = 'pattern';
    component.typingPattern = 'patternTest';

    fixture.detectChanges();

    const el = fixture.debugElement.query(By.css('input'));

    expect(el.attributes.ownPattern).toBe('');
    expect(el.attributes['ng-reflect-typing-pattern']).toBe('patternTest');
  });

  it('should show the error img and the tooltip', () => {
    component.error = 'errorTest';

    fixture.detectChanges();

    const tooltipTextEl = fixture.debugElement.query(By.css('.tooltiptext'))
      .nativeElement.textContent;
    const errorImgElPath = fixture.debugElement.query(By.css('img')).attributes
      .src;

    expect(tooltipTextEl).toBe('errorTest');
    expect(errorImgElPath).toBe('assets/images/alerts/ico-error_small.png');
  });

  it('should show a label', () => {
    component.label = 'testLabel';

    fixture.detectChanges();

    const el = fixture.debugElement.query(By.css('label'));

    expect(el.nativeElement.textContent).toBe('testLabel');
  });

  it('should show a void label', () => {
    component.label = '';

    fixture.detectChanges();

    const el = fixture.debugElement.query(By.css('label'));

    expect(el.nativeElement.textContent).toBe('');
  });

  it('should not show a label', () => {
    const el = fixture.debugElement.query(By.css('label'));

    expect(el).toBeNull();
  });

  // it('should bind', () => {

  // });

});
