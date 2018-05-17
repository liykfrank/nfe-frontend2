
import { UtilsProc } from './utils-poc';
import { UtilsElement } from './utils.element';
import { Injectable } from '@angular/core';
// Models
import { ActionsEnum } from './../../src/app/shared/models/actions-enum.enum';

// Services
import { UtilsService } from './../../src/app/shared/services/utils.service';

import { browser, by, element, ElementFinder, promise } from 'protractor';

export class UtilsMenu  extends UtilsProc {

  private _utilsService: UtilsService = new UtilsService();
  private _utilsElement: UtilsElement = new UtilsElement();

  navigateToHome() {
    console.log('move home');
    browser.get('/');
    browser.waitForAngular();
  }

  /**
   * Select the requested menu option
   * @param parent Elem parent
   * @param child (Optional)Elem child
   */
  navigateToMenu(parent: ActionsEnum, child?: ActionsEnum) {
    browser.waitForAngular();
    
    if (!child) {
      this.navigateToMenuParent(parent);
    } else {
      this.navigateToMenuChild(parent, child);
    }

    browser.waitForAngular();
  }

  private navigateToMenuParent(parent: ActionsEnum) {
    console.log('Waiting for menu bar');
    const ELEMPARENT = this._utilsElement.findById(this._utilsService.wrapperMenuID(parent));
    this.waitElemDisp(ELEMPARENT)
      .then( () => {
        expect(ELEMPARENT).toBeTruthy();
        console.log('Click on parent');
        ELEMPARENT.click();
      });
  }

  private navigateToMenuChild(parent: ActionsEnum, child: ActionsEnum) {
    console.log('Waiting for menu bar');
    const ELEMPARENT = this._utilsElement.findById(this._utilsService.wrapperMenuID(parent));
    this.waitElemDisp(ELEMPARENT);

    console.log('Going to menubar');
    expect(ELEMPARENT).toBeTruthy();
    browser.actions().mouseMove(ELEMPARENT.getWebElement()).perform();

    const ELEMCHILD = this._utilsElement.findById(this._utilsService.wrapperMenuID(child));
    this.waitElemDisp(ELEMCHILD)
      .then( () => {
        expect(ELEMCHILD).toBeTruthy();
        console.log('Click on child');
        ELEMCHILD.click();
      });
  }

}

