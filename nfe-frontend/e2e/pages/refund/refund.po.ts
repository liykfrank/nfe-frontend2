import { browser, element, by, Key } from 'protractor';
import { ROUTES } from '../../../src/app/shared/constants/routes';

export class RefundPage {

  navigateTo() {
    return browser.get('#' + ROUTES.REFUNDS.URL);
  }

}
