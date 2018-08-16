import { Injectable } from '@angular/core';
import { AbstractControl, FormGroup, FormControl } from '@angular/forms';

@Injectable()
export class UtilsService {
  public env: any;

  constructor() {}

  execFn(value, func: () => any) {
    if (value) {
      func();
    }
  }

  cloneObj<T>(obj: T): T {
    return Object.assign({}, obj);
  }

  getProv(real, mock) {
    return { provide: real, useClass: this.env.mock ? mock : real };
  }

  touchAllForms(forms: FormGroup[]) {
    for (const form of forms) {
      this._travelForm(form, null, null, this._touch);
    }
  }

  setBackErrorsOnForms(forms: FormGroup[], errors: any[]): any {
    const ret = [];

    for (const err of errors) {
      let found = false;
      let i = 0;

      if (err.fieldName && err.fieldName.length > 0) {
        const keys = err.fieldName.split(/[.\[\]]+/);

        while (!found && i < forms.length) {
          found = this._travelForm(forms[i], keys, err.message, this._setError);
          i++;
        }

        if (!found) {
          ret.push(err);
        }
      } else {
        ret.push(err);
      }
    }

    return ret;
  }

  private _travelForm(
    form: FormGroup,
    keys: string[],
    msg: string,
    fn: Function
  ): boolean {
    let ret = false;
    Object.keys(form.controls).forEach(key => {
      if (!ret) {
        if (form.get(key) instanceof FormGroup) {
          ret = this._travelForm(form.get(key) as FormGroup, keys, msg, fn);
        } else {
          ret = fn(form, keys, msg);
        }
      }
    });

    return ret;
  }

  private _touch(form: FormGroup, keys: string[], msg): boolean {
    Object.keys(form.controls).forEach(key => {
      form.get(key).markAsDirty();
    });
    return false;
  }

  private _setError(form: FormGroup, keys: string[], msg: string): boolean {
    let ret = false;
    let formAux: AbstractControl = form;

    for (const i of keys) {
      if (formAux.get(i)) {
        formAux = formAux.get(i);
        ret = true;
      } else {
        ret = false;
      }
    }

    if (ret && !formAux.disabled && formAux instanceof FormControl) {
      formAux.setErrors({
        backendError: {
          invalid: true,
          message: msg
        }
      });
    } else {
      ret = false;
    }
    return ret;
  }
}
