import { browser, by, element } from 'protractor';

import { ROUTES } from '../../../src/app/shared/constants/routes';

export class RefundPage {
  private baseID = 'REFUND.RA.';
  private basicInfo = 'basic_info';
  private details = 'details';
  private formOfPayment = 'form_of_payment';
  private amount = 'amount';

  navigateTo() {
    return browser.get('#' + ROUTES.REFUNDS.URL);
  }

  //basic_info
  getAirlineCode() {
    return element(
      by.id(this.baseID + this.basicInfo + '.airlineCode')
    );
  }

  getPhoneFaxNumber() {
    return element(
      by.id(this.baseID + this.basicInfo + '.phoneFaxNumber_1')
    );
  }

  getContactName() {
    return element(by.id(this.baseID + this.basicInfo + '.contactName_1'));
  }

  getEmail() {
    return element(by.id(this.baseID + this.basicInfo + '.email_1'));
  }

  //details
  getPassenger() {
    return element(by.id(this.baseID + this.details + '.passenger'));
  }

  getAirlineCodeRelatedDocument() {
    return element(
      by.id(this.baseID + this.details + '.airlineCodeRelatedDocument')
    );
  }

  getRelatedTicketDocumentNumber() {
    return element(
      by.id(this.baseID + this.details + '.relatedTicketDocumentNumber')
    );
  }

  getOriginalTicketDocumentNumber() {
    return element(
      by.id(this.baseID + this.details + '.originalTicketDocumentNumber')
    );
  }

  getWaiverCode() {
    return element(by.id(this.baseID + this.details + '.waiverCode'));
  }

  getExchange() {
    return element(by.id(this.baseID + this.details + '.exchange'));
  }

  getOriginalAirlineCode() {
    return element(by.id(this.baseID + this.details + '.originalAirlineCode'));
  }

  getOriginalAgentCode() {
    return element(by.id(this.baseID + this.details + '.originalAgentCode'));
  }

  getDateOfIssueRelatedDocument() {
    return element(
      by.id(this.baseID + this.details + '.dateOfIssueRelatedDocument')
    );
  }

  getOriginalDateOfIssue() {
    return element(by.id(this.baseID + this.details + '.originalDateOfIssue'));
  }

  getOriginalLocationCityCode() {
    return element(
      by.id(this.baseID + this.details + '.originalLocationCityCode')
    );
  }

  //Fop

  getFopType() {
    return element(by.id(this.baseID + this.formOfPayment + '.type'));
  }

  getFopAmount() {
    return element(by.id(this.baseID + this.formOfPayment + '.amount'));
  }

  getFopVendorCode() {
    return element(by.id(this.baseID + this.formOfPayment + '.vendorCode'));
  }

  getFopNumber() {
    return element(by.id(this.baseID + this.formOfPayment + '.number'));
  }

  getAddFopButton() {
    return element(by.id(this.baseID + this.formOfPayment + '.button.0'));
  }

  getTourCode() {
    return element(by.id(this.baseID + this.formOfPayment + '.tourCode'));
  }

  // Amount
  getGrossFare() {
    return element(by.id(this.baseID + this.amount + '.grossFare'));
  }

  getSaveResumebarButtom() {
    return element(by.id('saveResumeBar'));
  }

  getIssueResumebarButtom() {
    return element(by.id('issueResumeBar'));
  }

  // Message OK
  getOK() {
    return element(by.css('div.alert-info'));
  }
}
