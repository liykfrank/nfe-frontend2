import { TranslationService } from 'angular-l10n';
import { Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

export class ReactiveComponentBase {

  @Input('formGroupParent') formGroupParent: FormGroup;
  @Input('formControlParentName') formControlParentName: string;

  formControlParent: FormControl;
  inputForm: FormGroup;
  INPUT_FORM_CONTROL_NAME = 'inputFormControl';

  constructor(protected _translationService: TranslationService) {
  }

  protected createForm(): void {

    let formGroupObj = {};

    if (this.formControlParentName) {
      this.formControlParent = (this.formGroupParent.get(this.formControlParentName) as FormControl);
    } else {
      this.formControlParent = new FormControl('', []);
    }

    formGroupObj[this.INPUT_FORM_CONTROL_NAME] = this.formControlParent;
    this.inputForm = new FormGroup(formGroupObj);

    if (this.formControlParentName) {
      this.formGroupParent.setControl(this.formControlParentName, this.formControlParent);
    }
  }

  protected reactiveFormReady(): boolean {

    return this.formGroupParent != undefined && this.formControlParentName != undefined;
  }

  checkIfErrorExist(): boolean {
    if ((this.formControlParent.dirty || this.formControlParent.touched) && this.formControlParent.errors) {
      return true;
    }
    return false;
  }





}
