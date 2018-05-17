import { ActionsEnum } from './../models/actions-enum.enum';
import { Injectable } from "@angular/core";

@Injectable()
export class UtilsService {
  constructor() {}

  execFn(value, func: () => any) {
    if (value) {
      func();
    }
  }

  cloneObj<T>(obj: T): T {
    return Object.assign({}, obj);
  }

  wrapperMenuID(id: ActionsEnum): string {
    return 'MENU_' + id;
  }
}
