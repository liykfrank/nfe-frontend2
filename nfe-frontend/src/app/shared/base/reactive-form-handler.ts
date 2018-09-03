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

  enableControl(formControl: AbstractControl): void {
    formControl.enable(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  disableControl(formControl: AbstractControl): void {
    formControl.disable(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  resetControl(formControl: AbstractControl): void {
    formControl.reset('', GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  enableGroup(group: FormGroup, exceptions?: AbstractControl[]): void {
    this.enableControl(group);

    if (!exceptions) { return; }

    for (const control of exceptions) {
      this.disableControl(control);
    }
  }

  disableGroup(group: FormGroup, exceptions?: AbstractControl[]): void {
    this.disableControl(group);

    if (!exceptions) { return; }

    for (const control of exceptions) {
      this.enableControl(control);
    }
  }
}
