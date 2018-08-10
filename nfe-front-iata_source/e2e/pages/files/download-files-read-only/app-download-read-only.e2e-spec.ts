import { browser } from 'protractor';

import { UtilsElement } from '../../../utils/utils.element';
import { UtilsMenu } from '../../../utils/utils.menu';
import { UtilsProc } from '../../../utils/utils-poc';
import { ActionsEnum } from '../../../utils/utils.models';

describe('nw-front Page Query Files Read Only', () => {

  const _UTILSMENU: UtilsMenu = new UtilsMenu();
  const _UTILSELEMENT: UtilsElement = new UtilsElement();
  const _UTILSPROC: UtilsProc = new UtilsProc();

  beforeEach(() => {
    browser.waitForAngularEnabled(false);
    _UTILSMENU.navigateToHome();
    _UTILSMENU.navigateToMenu(ActionsEnum.FILES, ActionsEnum.QUERY_FILES_READ_ONLY);
  });

  it('check if div upload & download and checkbox selector on grid are hide', () => {
    const EL = _UTILSELEMENT.findByCSS('.jqx-grid-content div[role="row"]:first-child');

    _UTILSPROC.waitElemDisp(EL, 'Waiting for the element on the screen').then( () => {
      _UTILSELEMENT.isNotPresentFindByCSS('div.top_buttons');
      _UTILSELEMENT.isNotPresentFindByCSS('div.bottom_buttons');
      _UTILSELEMENT.isNotPresentFindByCSS('div.jqx-checkbox-default');
    });
  });

});
