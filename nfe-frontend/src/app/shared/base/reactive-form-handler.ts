import { Input } from '@angular/core';

import { AbstractComponent } from './abstract-component';
import { ReactiveFormHandlerModel } from './reactive-form-handler-model';

export class ReactiveFormHandler<T extends ReactiveFormHandlerModel> extends AbstractComponent {

  @Input() model: T;

  constructor() {
    super();
  }
}
