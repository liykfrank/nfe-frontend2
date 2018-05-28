import { environment } from './../../../environments/environment.prod';
import { ActionsEnum } from './../models/actions-enum.enum';
import { Injectable } from "@angular/core";

@Injectable()
export class UtilsService {
  public env:any;
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

  wrapperMenuID(id: ActionsEnum): string {
    return 'MENU_' + id;
  }

  getProv(real,mock){
    return { provide: real, useClass: (this.env.mock)?mock:real };
  }
}
