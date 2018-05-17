import { by, element, browser } from "protractor";
import { UtilsElement } from "../../../utils/utils.element";
import { UtilsMenu } from "../../../utils/utils.menu";
import { UtilsProc } from "../../../utils/utils-poc";
import { ActionsEnum } from "../../../utils/utils.models";

describe("nw-front Page Upload Files", () => {
  const _UTILSMENU: UtilsMenu = new UtilsMenu();
  const _UTILSELEMENT: UtilsElement = new UtilsElement();
  const _UTILSPROC: UtilsProc = new UtilsProc();

  const UPLOAD_PATH = process.cwd() + "/e2e/resources/upload/";

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
        const FILENAME = "SAec20180101.txt";

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME);

        const LIST = _UTILSELEMENT.findByCSS(".ui-fileupload-row");
        expect(LIST).toBeTruthy();

        _UTILSELEMENT
          .findByCSS(".fa-upload")
          .click()
          .then(() => {
            expect(element(by.linkText(FILENAME))).toBeTruthy();
          });
      });
  });

  it("should reject an invalid file", () => {
    const EL = _UTILSELEMENT.findByCSS(".files-upload");

    _UTILSPROC
      .waitElemDisp(EL, "Waiting for the element on the screen")
      .then(() => {
        const FILENAME = "foo.txt";

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME);

        const MSG = _UTILSELEMENT.findByCSS(".ui-messages-error");
        expect(MSG).toBeTruthy();
      });
  });

  it("Try to insert more than 3 files", () => {
    const EL = _UTILSELEMENT.findByCSS(".files-upload");

    _UTILSPROC
      .waitElemDisp(EL, "Waiting for the element on the screen")
      .then(() => {
        const FILENAME1 = "SAec20180101.txt";
        const FILENAME2 = "SBec20180101.txt";
        const FILENAME3 = "SCec20180101.txt";
        const FILENAME4 = "SDec20180101.txt";

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
        const FILENAME = "SAec20180101.txt";

        const FILE = _UTILSELEMENT.findByCSS('input[type="file"]');
        FILE.sendKeys(UPLOAD_PATH + FILENAME);

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
