import { by } from 'protractor';

import { Acdm } from '../acdm.po';

describe('Issue ADM/ACM', () => {
  let page: Acdm;

  describe('ADM', () => {
    beforeEach(() => {
      page = new Acdm(true, 'issue');
      page.navigateTo();
    });

    it('TODO: Save', () => {});

    it('Issue ADMA', () => {
      const agentCode = page.getAgentCode();
      const agentControlDigit = page.getAgentControlDigit();
      const airlineCode = page.getAirlineCode();
      const phoneFaxNumber = page.getPhoneFaxNumber();
      const contactName = page.getContactName();
      const email = page.getEmail();
      const stat = page.getStat();

      // Amount
      const airlineFare = page.getAirlineFare();

      // Button
      const issueResumebarButtom = page.getIssueResumebarButtom();

      agentCode.sendKeys('7820002');
      agentControlDigit.sendKeys('1');

      airlineCode.sendKeys('123');

      phoneFaxNumber.sendKeys('+34 666 666 666');
      contactName.sendKeys('TEST ISUE ADMA');
      email.sendKeys('TEST@TEST.es');
      stat.sendKeys('DOM');

      airlineFare.sendKeys('1');
      issueResumebarButtom.click();

      expect(page.getOK().isPresent()).toBe(true);
    });

    it('Issue ADMD', () => {
      const selectType = page.getSelectType();

      selectType.click();
      selectType
        .all(by.css('option'))
        .last()
        .click();

      const agentCode = page.getAgentCode();
      const agentControlDigit = page.getAgentControlDigit();
      const airlineCode = page.getAirlineCode();
      const phoneFaxNumber = page.getPhoneFaxNumber();
      const contactName = page.getContactName();
      const email = page.getEmail();
      const stat = page.getStat();

      // Amount
      const airlineFare = page.getAgentFare(); // RAR: Same ID, not insert on agent

      // Button
      const issueResumebarButtom = page.getIssueResumebarButtom();

      agentCode.sendKeys('7820002');
      agentControlDigit.sendKeys('1');

      airlineCode.sendKeys('123');

      phoneFaxNumber.sendKeys('+34 666 666 666');
      contactName.sendKeys('TEST ISUE ADMA');
      email.sendKeys('TEST@TEST.es');
      stat.sendKeys('DOM');

      airlineFare.sendKeys('1');
      issueResumebarButtom.click();

      expect(page.getOK().isPresent()).toBe(true);
    });
  });

  describe('ACM', () => {
    beforeEach(() => {
      page = new Acdm(false, 'issue');
      page.navigateTo();
    });

    it('TODO: Save', () => {});

    it('Issue ACMA', () => {
      const agentCode = page.getAgentCode();
      const agentControlDigit = page.getAgentControlDigit();
      const airlineCode = page.getAirlineCode();
      const phoneFaxNumber = page.getPhoneFaxNumber();
      const contactName = page.getContactName();
      const email = page.getEmail();
      const stat = page.getStat();

      // Amount
      const agentFare = page.getAgentFare();

      // Button
      const issueResumebarButtom = page.getIssueResumebarButtom();

      agentCode.sendKeys('7820002');
      agentControlDigit.sendKeys('1');

      airlineCode.sendKeys('123');

      phoneFaxNumber.sendKeys('+34 666 666 666');
      contactName.sendKeys('TEST ISUE ADMA');
      email.sendKeys('TEST@TEST.es');
      stat.sendKeys('DOM');

      agentFare.sendKeys('1');
      issueResumebarButtom.click();

      expect(page.getOK().isPresent()).toBe(true);
    });

    it('Issue ACMD', () => {
      const selectType = page.getSelectType();

      selectType.click();
      selectType
        .all(by.css('option'))
        .last()
        .click();

      const agentCode = page.getAgentCode();
      const agentControlDigit = page.getAgentControlDigit();
      const airlineCode = page.getAirlineCode();
      const phoneFaxNumber = page.getPhoneFaxNumber();
      const contactName = page.getContactName();
      const email = page.getEmail();
      const stat = page.getStat();

      // Amount
      const agentFare = page.getAgentFare();

      // Button
      const issueResumebarButtom = page.getIssueResumebarButtom();

      agentCode.sendKeys('7820002');
      agentControlDigit.sendKeys('1');

      airlineCode.sendKeys('123');

      phoneFaxNumber.sendKeys('+34 666 666 666');
      contactName.sendKeys('TEST ISUE ADMA');
      email.sendKeys('TEST@TEST.es');
      stat.sendKeys('DOM');

      agentFare.sendKeys('1');
      issueResumebarButtom.click();

      expect(page.getOK().isPresent()).toBe(true);
    });
  });
});
