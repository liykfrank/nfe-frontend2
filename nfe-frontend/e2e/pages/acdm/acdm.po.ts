import { browser, by, element } from 'protractor';

import { ROUTES } from './../../../src/app/shared/constants/routes';

export class Acdm {
  private route = '';
  private baseBI = '';
  private baseAmount = '';
  private baseDetail = 'ACDM.details';

  constructor(isAdm: boolean, type: string) {
    if (type == 'issue') {
      this.route = isAdm ? ROUTES.ADM_ISSUE.URL : ROUTES.ACM_ISSUE.URL;

      this.baseBI = isAdm ? 'ADM.BASIC_INFO.' : 'ACM.BASIC_INFO.';
      this.baseAmount = isAdm ? 'ADM.AMOUNT.' : 'ACM.AMOUNT.';
    } else if (type == 'modify') {
      this.route = '';
    }
  }

  navigateTo() {
    browser.get('#' + this.route);
  }

  // BASIC INFO
  getSelectIso() {
    return element(by.id(this.baseBI + 'isoCountryCode'));
  }

  getSelectPeriodNumber() {
    return element(by.name(this.baseBI + 'selectPeriodNumber'));
  }

  getSelectPeriodMonth() {
    return element(by.name(this.baseBI + 'selectPeriodMonth'));
  }

  getSelectPeriodYear() {
    return element(by.name(this.baseBI + 'selectPeriodYear'));
  }

  getSelectType() {
    return element(by.id(this.baseBI + 'transactionCode'));
  }

  getSelectConcernsIndicator() {
    return element(by.id(this.baseBI + 'concernsIndicator'));
  }

  getSelectTOCA() {
    return element(by.id(this.baseBI + 'taxOnCommissionType'));
  }

  getCurrency() {
    return element(by.css('bspl-currency>div>select'));
  }

  getAgentCode() {
    return element(by.id(this.baseBI + 'agentCode'));
  }

  getAgentControlDigit() {
    return element(by.id(this.baseBI + 'agentControlDigit'));
  }

  getAgentVatNumber() {
    return element(by.id(this.baseBI + 'agentVatNumber'));
  }

  getAgentRegistrationNumber() {
    return element(by.id(this.baseBI + 'agentRegistrationNumber'));
  }

  getAirlineCode() {
    return element(by.id(this.baseBI + 'airlineCode'));
  }

  getAirlineVatNumber() {
    return element(by.id(this.baseBI + 'airlineVatNumber'));
  }

  getAirlineRegistrationNumber() {
    return element(by.id(this.baseBI + 'airlineRegistrationNumber'));
  }

  getPhoneFaxNumber() {
    return element(by.id(this.baseBI + 'phoneFaxNumber'));
  }

  getContactName() {
    return element(by.id(this.baseBI + 'contactName'));
  }

  getEmail() {
    return element(by.id(this.baseBI + 'email'));
  }

  getStat() {
    return element(by.id(this.baseBI + 'statisticalCode'));
  }

  getNetReporting() {
    return element(by.id(this.baseBI + 'netReporting'));
  }

  // AMOUNT AGENT
  getAgentFare() {
    return element(by.id(this.baseAmount + 'fare'));
  }

  getAirlineFare() {
    return element(by.id(this.baseAmount + 'fare_1'));
  }

  getAgentTax() {
    return element(by.id(this.baseAmount + 'tax'));
  }

  getAirlineTax() {
    return element(by.id(this.baseAmount + 'tax_1'));
  }

  getAgentCommission() {
    return element(by.id(this.baseAmount + 'commission'));
  }

  getAirlineCommission() {
    return element(by.id(this.baseAmount + 'commission_1'));
  }



  // Resume Bar
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
