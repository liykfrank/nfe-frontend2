import { Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

export class ReactiveComponentBase {

  @Input('groupForm') groupForm: FormGroup;
  @Input('controlName') controlName: string;

  constructor() {}

  checkIfErrorExist(): boolean {
    const elem = this.groupForm.get(this.controlName);

    if (
      ((elem.dirty || elem.touched) && elem.errors) ||
      (elem.errors && elem.errors.backendError)
    ) {
      return true;
    }

    return false;
  }
}
