import { HttpClientModule } from '@angular/common/http';
import { TranslationModule } from 'angular-l10n';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { By } from '@angular/platform-browser';

import { SharedModule } from '../../shared.module';
import { FieldErrorComponent } from '../field-error/field-error.component';
import { InputFormControlComponent } from '../input-form-control/input-form-control.component';
import { StatComponent } from './stat.component';
import { l10nConfig } from '../../base/conf/l10n.config';

describe('StatComponent', () => {
  let component: StatComponent;
  let fixture: ComponentFixture<StatComponent>;

  let fGroup: FormGroup;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        SharedModule,
        TranslationModule.forRoot(l10nConfig),
        HttpClientModule
      ],
      declarations: [
        /* FieldErrorComponent,
        InputFormControlComponent, */
        StatComponent,
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StatComponent);
    component = fixture.componentInstance;

    fGroup = new FormGroup({ test: new FormControl('') });

    component.groupForm = fGroup;
    component.controlName = 'test';

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  /* Checks of the input text */

  it('should create a default input text when freestat = true', () => {
    component.freeStat = true;
    fixture.detectChanges();
    const el = fixture.debugElement.query(By.css('input'));
    expect(el.attributes.type).toBe('text');
  });

  // it('if freestat is true and default stat DOM, value = D', () => {
  //   component.freeStat = true;
  //   component.defaultStat = 'DOM';
  //   fixture.detectChanges();
  //   component.createInputForm();
  //   expect(component.value).toBe('D');
  // });

  // it('if freestat is true and default stat INT, value = I', () => {
  //   component.freeStat = true;
  //   component.defaultStat = 'INT';
  //   fixture.detectChanges();
  //   component.createInputForm();
  //   expect(component.value).toBe('I');
  // });

  // it('if formcontrolname, FormControl is created and have a value', () => {
  //   component.freeStat = true;
  //   component.defaultStat = 'DOM';
  //   component.formControlParentName = 'passenger';
  //   fixture.detectChanges();
  //   expect(component.formControlParent).toBeTruthy();
  //   expect(component.formControlParent.value).toBe(component.value);
  // });

  // it('if formcontrolname is not undefined , formcontrolparent is not null', () => {
  //   component.freeStat = true;
  //   component.defaultStat = 'DOM';
  //   component.formControlParentName = 'passenger';
  //   component.label = 'prueba';
  //   component.formGroupParent = new FormGroup({passenger: new FormControl()});
  //   fixture.detectChanges();
  //   component.ngOnInit();
  //   expect(component.formControlParent).toBeTruthy();
  //   expect(component.formControlParent.value).toBe(component.value);
  // });

  // it('if formcontrolname is undefined , FormControl is created', () => {
  //   component.freeStat = true;
  //   component.formControlParentName = '';
  //   fixture.detectChanges();
  //   expect(component.formControlParent).toBeTruthy();
  // });

  /* Checks of the input radio */
  it('should create a default input type radio when freestat = false and select = false', () => {
    component.freeStat = false;
    component.select = false;
    fixture.detectChanges();
    const el = fixture.debugElement.query(By.css('input'));
    expect(el.attributes.type).toBe('radio');
  });

  // it('method onError is called', () => {
  //   const spy = spyOn(component, 'onError');
  //   component.onError('');
  //   expect(spy).toHaveBeenCalled();
  // });

  // it('method onError change propertie error with value defined', () => {
  //   component.onError(true);
  //   fixture.detectChanges();
  //   expect(component.error).toBe('The stat field must start with the characters D or I and its length can not be greater than 3');
  // });

  // it('method onError change propertie error with value empty', () => {
  //   component.onError(false);
  //   fixture.detectChanges();
  //   expect(component.error).toBe('');
  // });


});
