import { by, element } from 'protractor';

import { RefundPage } from './refund.po';

xdescribe('REFUND Issue', () => {
  let page: RefundPage;

  beforeEach(() => {
    page = new RefundPage();
    page.navigateTo();
  });

  it('create a simple refund', () => {
    // basic info
    const airlineCode = page.getAirlineCode();
    const contactName = page.getContactName();
    const email = page.getEmail();
    const phoneFaxNumber = page.getPhoneFaxNumber();

    airlineCode.sendKeys('123');
    contactName.sendKeys('Test name');
    email.sendKeys('email@testing.com');
    phoneFaxNumber.sendKeys('+34 64571245');

    //details
    const passenger = page.getPassenger();
    const airlineCodeRelatedDocument = page.getAirlineCodeRelatedDocument();
    const relatedTicketDocumentNumber = page.getRelatedTicketDocumentNumber();
    const dateOfIssueRelatedDocument = page.getDateOfIssueRelatedDocument();
    const waiverCode = page.getWaiverCode();

    const exchange = page.getExchange();
    const originalAirlineCode = page.getOriginalAirlineCode();
    const originalTicketDocumentNumber = page.getOriginalTicketDocumentNumber();
    const originalAgentCode = page.getOriginalAgentCode();
    const originalDateOfIssue = page.getOriginalDateOfIssue();
    const originalLocationCityCode = page.getOriginalLocationCityCode();

    passenger.sendKeys('PASSENGER TEST');

    element(by.css('bspl-reasons-selector>div>select')).click();
    element
      .all(by.css('bspl-reasons-selector>div>select>option'))
      .last()
      .click();

    airlineCodeRelatedDocument.sendKeys('123');
    relatedTicketDocumentNumber.sendKeys('78200021');
    dateOfIssueRelatedDocument.click();
    element(by.css('td.ui-datepicker-today')).click();
    waiverCode.sendKeys('waiverCode');

    exchange.click();
    originalAirlineCode.sendKeys('123');
    originalTicketDocumentNumber.sendKeys('78200021');
    originalAgentCode.sendKeys('78200021');
    originalDateOfIssue.click();
    element(by.css('td.ui-datepicker-today')).click();
    originalLocationCityCode.sendKeys('AAA');

    //fop

    const fopType = page.getFopType();
    const fopAmount = page.getFopAmount();
    const fopAddbutton = page.getAddFopButton();
    const fopVendorCode = page.getFopVendorCode();
    const fopNumber = page.getFopNumber();
    const tourCode = page.getTourCode();
    const grossFare = page.getGrossFare();

    const resumeSaveButtom = page.getSaveResumebarButtom();

    fopType.click();
    fopType
      .all(by.css('option'))
      .last()
      .click();
    fopType.click();

    fopAmount.sendKeys('4051');
    fopVendorCode.sendKeys('VI');
    fopNumber.sendKeys('12123');
    fopAddbutton.click();
    tourCode.sendKeys('4051');

    //amount
    grossFare.sendKeys('4051');

    resumeSaveButtom.click();

    expect(page.getOK().isPresent()).toBe(true);
  });
});
