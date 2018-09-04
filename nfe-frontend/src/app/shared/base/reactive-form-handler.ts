import { Input } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { GLOBALS } from '../constants/globals';
import { AbstractComponent } from './abstract-component';
import { ReactiveFormHandlerModel } from './reactive-form-handler-model';


export class ReactiveFormHandler<T extends ReactiveFormHandlerModel> extends AbstractComponent {

  @Input() model: T;

  constructor() {
    super();
  }

  enableControl(control: AbstractControl): void {
    control.enable(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  disableControl(control: AbstractControl): void {
    control.disable(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  resetControl(control: AbstractControl): void {
    control.reset('', GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  enableFormGroup(formGroup: FormGroup, exceptions?: AbstractControl[]): void {
    this.enableControl(formGroup);

    if (!exceptions) { return; }

    for (const control of exceptions) {
      this.disableControl(control);
    }
  }

  disableFormGroup(formGroup: FormGroup, exceptions?: AbstractControl[]): void {
    this.disableControl(formGroup);

    if (!exceptions) { return; }

    for (const control of exceptions) {
      this.enableControl(control);
    }
  }
}
