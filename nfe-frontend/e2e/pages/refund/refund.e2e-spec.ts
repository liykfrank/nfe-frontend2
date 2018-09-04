import { browser, by, element, ElementFinder, promise, Browser } from 'protractor';
import { RefundPage } from './refund.po';
import { debug } from 'util';

describe('REFUND Issue', () => {

  let page: RefundPage;

  beforeEach(() => {
    page = new RefundPage();
  });

  it('create a simple refund', () => {
    page.navigateTo();
  });


});
