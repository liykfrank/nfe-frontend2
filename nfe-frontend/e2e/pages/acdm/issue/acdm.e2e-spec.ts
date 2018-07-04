import { browser, by, element } from 'protractor';
import { UtilsMenu } from '../../../utils/utils.menu';
import { UtilsElement } from '../../../utils/utils.element';
import { UtilsProc } from '../../../utils/utils-poc';
import { ActionsEnum } from '../../../utils/utils.models';

describe('ACDM Issue', () => {
  const _UTILSMENU: UtilsMenu = new UtilsMenu();
  const _UTILSELEMENT: UtilsElement = new UtilsElement();
  const _UTILSPROC: UtilsProc = new UtilsProc();

  beforeEach(() => {
    browser.waitForAngularEnabled(false);
    _UTILSMENU.navigateToHome();
    _UTILSMENU.navigateToMenu(ActionsEnum.ACDM, ActionsEnum.DISPUTE_ADM);
  });

  it('check screen, dropdown has data', () => {
    const el_period = _UTILSELEMENT.findByCSS('select[name="period"]');

    _UTILSPROC.waitElemDisp(el_period, 'Waiting for the element on the screen')
      .then(() => {
        el_period.click().then(() => expect(el_period.all(by.tagName('option')).count()).toBeGreaterThan(0));

        const el_period_month = _UTILSELEMENT.findByCSS('select[name="periodMonth"]');
        el_period_month.click().then(() => expect(el_period_month.all(by.tagName('option')).count()).toBeGreaterThan(0));

        const el_period_year = _UTILSELEMENT.findByCSS('select[name="periodYear"]');
        el_period_year.click().then(() => expect(el_period_year.all(by.tagName('option')).count()).toBeGreaterThan(0));

        const el_isoCountryCode = _UTILSELEMENT.findByCSS('select[name="isoCountryCode"]');
        el_isoCountryCode.click().then(() => expect(el_isoCountryCode.all(by.tagName('option')).count()).toBeGreaterThan(0));
      });
  });

  it('check agent', () => {
    const el_period = _UTILSELEMENT.findByCSS('select[name="period"]');

    _UTILSPROC.waitElemDisp(el_period, 'Waiting for the element on the screen')
      .then(() => {
        const el_agent = _UTILSELEMENT.findByCSS('input[maxlength="7"]');
        const el_agent_check = _UTILSELEMENT.findByCSS('input[maxlength="1"]');
        el_agent.sendKeys('1111111');
        el_agent_check.sendKeys('1');

        expect(_UTILSELEMENT.findByCSS('div.ui-dialog-content[css="1"]')).toBeTruthy();
      });
  });

  it('check airline', () => {
    const el_period = _UTILSELEMENT.findByCSS('select[name="period"]');

    _UTILSPROC.waitElemDisp(el_period, 'Waiting for the element on the screen')
      .then(() => {
        const el_agent = _UTILSELEMENT.findByCSS('input[ng-reflect-typing-pattern="[A-Z0-9]{3}$"]');
        el_agent.sendKeys('ZZZ');

        expect(_UTILSELEMENT.findByCSS('div.ui-dialog-content[css="1"]')).toBeTruthy();
      });
  });

});
