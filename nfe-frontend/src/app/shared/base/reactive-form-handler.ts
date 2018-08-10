import { EventEmitter, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { AbstractComponent } from './abstract-component';

export class ReactiveFormHandler extends AbstractComponent {

  @Output() returnForm = new EventEmitter();
  protected reactiveFormGroup: FormGroup;

  constructor() {
    super();
  }

  subscribe(reactiveFormGroup: FormGroup) {
    this.reactiveFormGroup = reactiveFormGroup;
    this.subscriptions.push(
      this.reactiveFormGroup.valueChanges.subscribe(val => {
        this.returnForm.emit(this.reactiveFormGroup);
        // console.log(val);
      })
    );
  }

}
