import { Injectable } from '@angular/core';

@Injectable()
export class UtilsService {

  public env: any;

  constructor() {
  }

  execFn(value, func: () => any) {
    if (value) {
      func();
    }
  }

  cloneObj<T>(obj: T): T {
    return Object.assign({}, obj);
  }

  getProv(real, mock) {
    return { provide: real, useClass: (this.env.mock) ? mock : real };
  }
}
