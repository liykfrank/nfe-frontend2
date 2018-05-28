import { by, element, browser, ElementFinder, promise } from "protractor";
import { UtilsElement } from "../../utils/utils.element";
import { UtilsMenu } from "../../utils/utils.menu";
import { UtilsProc } from "../../utils/utils-poc";
import { ActionsEnum } from "../../utils/utils.models";

export class SftpAccountPo {
  _UTILSMENU: UtilsMenu = new UtilsMenu();
  _UTILSELEMENT: UtilsElement = new UtilsElement();
  _UTILSPROC: UtilsProc = new UtilsProc();

  pageIsEmpty() {
    const LOGIN = this._UTILSELEMENT.findByName("login");
    expect(LOGIN.getAttribute("value")).toBe("");

    const PKEY = this._UTILSELEMENT.findByName("publicKey");
    expect(PKEY.getAttribute("value")).toBe("");

    const SAVE = this._UTILSELEMENT
      .findById("SaveSftpAccount")
      .getAttribute("ng-reflect-attr-disabled");
    expect(SAVE).toBe("true");
  }

  pageIsNotEmpty() {
    const LOGIN = this._UTILSELEMENT.findByName("login");
    expect(LOGIN.getAttribute("value")).not.toBe("");

    const DELETE = this._UTILSELEMENT.findById("DeleteSftpAccount");
    expect(DELETE).toBeTruthy();
  }
}
