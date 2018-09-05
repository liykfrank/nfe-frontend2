import { browser, element, by } from 'protractor';

import { ROUTES } from '../../../../src/app/shared/constants/routes';

export class UploadFiles {

  navigateTo() {
    browser.get('#' + ROUTES.UPLOAD_FILES.URL);
  }

  getInputToSelectElement() {
    return element(by.id('file-browser-input'));
  }

  getCancel() {
    return element(by.css('button.button-cancel'));
  }

  getUpdate() {
    return element(by.css('button.button-update'));
  }

  getDivsFiles() {
    return element.all(by.css('div.file-name'));
  }

  getDivOKs() {
    return element.all(by.css('div.check-accept'));
  }

}
