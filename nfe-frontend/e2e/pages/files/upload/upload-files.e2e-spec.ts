import { by, element, browser, protractor } from "protractor";
import { UtilsElement } from "../../../utils/utils.element";
import { UtilsMenu } from "../../../utils/utils.menu";
import { UtilsProc } from "../../../utils/utils-poc";
import { ActionsEnum } from "../../../utils/utils.models";

describe("nw-front Page Upload Files", () => {
  const _UTILSMENU: UtilsMenu = new UtilsMenu();
  const _UTILSELEMENT: UtilsElement = new UtilsElement();
  const _UTILSPROC: UtilsProc = new UtilsProc();

  const UPLOAD_PATH = process.cwd() + "/e2e/resources/upload/";


  const FILENAME1 = "A0aaAAA0_20180521.txt";
  const FILENAME2 = "B0aaAAA0_20180521.txt";
  const FILENAME3 = "C0aaAAA0_20180521.txt";
  const FILENAME4 = "D0aaAAA0_20180521.txt";
  const FILENAME_FOO = "foo.txt";

  beforeEach(() => {
    browser.waitForAngularEnabled(false);
    _UTILSMENU.navigateToHome();
    _UTILSMENU.navigateToMenu(ActionsEnum.FILES, ActionsEnum.UPLOAD_FILES);
  });

  it("should select a valid file", function() {
    const EL = _UTILSELEMENT.findByCSS(".files-upload");
    _UTILSPROC
      .waitElemDisp(EL, "Waiting for the element on the screen")
      .then(() => {

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME1);

        const LIST = _UTILSELEMENT.findByCSS(".ui-fileupload-row");
        expect(LIST).toBeTruthy();

        _UTILSELEMENT
          .findByCSS(".fa-upload")
          .click()
          .then(() => {
            expect(element(by.linkText(FILENAME1))).toBeTruthy();
          });
      });
  });

  it("should reject an invalid file", () => {
    const EL = _UTILSELEMENT.findByCSS(".files-upload");

    _UTILSPROC
      .waitElemDisp(EL, "Waiting for the element on the screen")
      .then(() => {

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME_FOO);

        const MSG = _UTILSELEMENT.findByCSS(".ui-messages-error");
        expect(MSG).toBeTruthy();
      });
  });

  it("Try to insert more than 3 files", () => {
    const EL = _UTILSELEMENT.findByCSS(".files-upload");

    _UTILSPROC
      .waitElemDisp(EL, "Waiting for the element on the screen")
      .then(() => {

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME1);
        FILE.sendKeys(UPLOAD_PATH + FILENAME2);
        FILE.sendKeys(UPLOAD_PATH + FILENAME3);
        FILE.sendKeys(UPLOAD_PATH + FILENAME4);

        const MSG = _UTILSELEMENT.findByCSS(".ui-messages-error");
        expect(MSG).toBeTruthy();
      });
  });

  it("Click on cancel", function() {
    const EL = _UTILSELEMENT.findByCSS(".files-upload");

    _UTILSPROC
      .waitElemDisp(EL, "Waiting for the element on the screen")
      .then(() => {

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME1);

        const EL = _UTILSELEMENT.findByCSS(".ui-fileupload-row");
        expect(EL).toBeTruthy();

        _UTILSELEMENT
          .findByCSS(".fa-close")
          .click()
          .then(() => {
            _UTILSELEMENT.isNotPresentFindByCSS(".ui-fileupload-row");
          });
      });
  });
});
