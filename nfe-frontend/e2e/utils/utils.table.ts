import { UtilsElement } from "./utils.element";
import { ElementFinder } from "protractor";

export class UtilsMenu extends UtilsElement {
  getNRow(i: number): ElementFinder {
    const EL = this.findByCSS('div[role="row"]:nth-child(' + i + ")");
    expect(EL).toBeTruthy();
    return EL;
  }
}
