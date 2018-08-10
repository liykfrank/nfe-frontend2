import { Injectable } from '@angular/core';
import { FormGroup, AbstractControl } from '@angular/forms';

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
/*
  touchAllForms(forms: FormGroup[]) {
    for (const form of forms) {
      this._travelForm(form, this.fun());
    }
  } */

  fun(obj) {
    console.log(obj);
  }
  setBackErrorsOnForms(forms: FormGroup[], errors: any[]): any {
    const ret = [];

    for (const err of errors) {
      let found = false;
      let i = 0;

      if (err.fieldName && err.fieldName.length > 0) {
        const keys = err.fieldName.split(/[.\[\]]+/);

        while (!found && i < forms.length) {
          found = this._findKeysOnForm(forms[i], keys, err.message);
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

  private _travelForm(form: FormGroup, fn: Function) {
    Object.keys(form.controls).forEach(key => {

    });
  }

  private _findKeysOnForm(form: FormGroup, keys: string[], msg: string): boolean {
    let ret = false;

    if (form.get(keys[0])) {
      ret = this._setErrorOnForm(form, keys, this._getCustomError(msg));
    } else {
      Object.keys(form.controls).forEach(key => {
        if (!ret && form.get(key).get(keys[0])) {
          ret = this._setErrorOnForm(form.get(key), keys, this._getCustomError(msg));
        }
      });
    }

    return ret;
  }

  private _setErrorOnForm(form: AbstractControl, keys: string[], msg: any): boolean {
    let aux = form;

    let setError = false;
    for (const x of keys) {
      if (aux) {
        aux = aux.get(x);
        setError = true;
      } else {
        setError = false;
      }
    }
    if (setError) {
      aux.setErrors(msg);
    }

    return setError;
  }

  private _getCustomError(msg: string): any {
    return {
      backendError: {
        invalid: true,
        message: msg
      }
    };
  }
}
