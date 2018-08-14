import { ReactiveFormHandler } from './reactive-form-handler';
import { FormGroup } from '@angular/forms';
export abstract class ReactiveFormHandlerModel {

  public defaultDisabled = false;

  constructor(defaultDisabled?: boolean) {
    this.defaultDisabled = defaultDisabled;
    this.createFormControls();
    this.createFormGroups();
    this.createForm();
  }

  abstract createFormControls();

  abstract createFormGroups();

  abstract createForm();

}
