import { by, element, browser } from "protractor";
import { UtilsElement } from "../../utils/utils.element";
import { UtilsMenu } from "../../utils/utils.menu";
import { UtilsProc } from "../../utils/utils-poc";
import { ActionsEnum } from "../../utils/utils.models";

import { SftpAccountPo } from "./sftp-account.po";

xdescribe("nw-front Page Sftp-Accounts", () => {
  const _UTILSMENU: UtilsMenu = new UtilsMenu();
  const _UTILSELEMENT: UtilsElement = new UtilsElement();
  const _UTILSPROC: UtilsProc = new UtilsProc();

  const PAGE: SftpAccountPo = new SftpAccountPo();

  const DEFAULT_PASS = 'PhrW7XN9';
  const KEY = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC6Kij4YoGZaC6JCUTqma3sUB5VKAwnSPgShJdxQfQfKWW8ruAqEHV1v+lTg+aATJqnTFEp8VEihrGpgjc7p7gyH+bWLmk/RnRJf6lsy6mqq2S79F5x83dJ7YoupY6ty/t/d+b38j4eo2wJyGjb5XSvnBvm26CEAdUz5kH37UT1C1Dm1ISj/Tk4s3NL0nJNVYguwtb7HaK1wxH5bNbRvAa1Bwm4Z3kbeJtR1oE3jl4cKmPwhFhKSKLxbo3BR81PvoEhWb35S6cOgC9nfd46Dz2vkukcHbBu+LoFDkRxrqF3U07O5wme7byjW9McdsB5F3khlZk0gHbWblfPJ2+7+yxD antonio.nunez@T4-0103";

  beforeEach(() => {
    browser.waitForAngularEnabled(false);

    _UTILSMENU.navigateToHome();
    _UTILSMENU.navigateToMenu(ActionsEnum.SFTP_ACCOUNTS);
  });

  it("Page should be empty", () => {
    const SAVE = _UTILSELEMENT.findById("SaveSftpAccount");

    _UTILSPROC
      .waitElemDisp(SAVE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsEmpty();
      });
  });

  it("Create an account (without PKey)", () => {
    const SAVE = _UTILSELEMENT.findById("SaveSftpAccount");

    _UTILSPROC
      .waitElemDisp(SAVE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsEmpty();

        console.log("Write Login");
        const LOGIN = _UTILSELEMENT.findByName("login");
        LOGIN.sendKeys("login");

        console.log("Select Enabled");
        const STATUS = _UTILSELEMENT.findByName("status");
        expect(STATUS).toBeDefined();
        expect(STATUS.getAttribute('ng-reflect-readonly')).toBe('true');

        console.log("Click Save");
        const OK = _UTILSELEMENT.findByCSS(
          "span.ui-messages-summary.ng-star-inserted"
        );

        SAVE.click().then(() => {
          _UTILSPROC
            .waitElemDisp(OK, "Waiting for the element on the screen")
            .then(() => {
              expect(OK).toBeTruthy();
            });
        });
      });
  });

  it("Click modify and then click cancel", () => {
    const CHANGE = _UTILSELEMENT.findById("changePassword");
    const SAVE = _UTILSELEMENT.findById("SaveSftpAccount");

    _UTILSPROC
      .waitElemDisp(CHANGE, "Waiting for the element on the screen")
      .then(() => {
        CHANGE.click().then(() => {
          const SAVE_PASS = _UTILSELEMENT.findById("savePassword");
          const SAVE_PASS_ATTR = SAVE_PASS.getAttribute("ng-reflect-attr-disabled");

          expect(SAVE_PASS_ATTR).toBe("true");

          const CANCEL = _UTILSELEMENT.findById("cancelChangePassword");
          CANCEL.click().then(() => {
            expect(SAVE).toBeTruthy();
          });
        });
      });
  });

  it("Modify pass with wrong old pass", () => {
    const CHANGE = _UTILSELEMENT.findById("changePassword");

    _UTILSPROC
      .waitElemDisp(CHANGE, "Waiting for the element on the screen")
      .then(() => {
        CHANGE.click().then(() => {
          const SAVE = _UTILSELEMENT.findById("savePassword");
          const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

          const OLD = _UTILSELEMENT.findByName("oldPassword");
          OLD.sendKeys('wrong');
          expect(SAVE_ATTR).toBe("true");

          const NEW = _UTILSELEMENT.findByName("password");
          NEW.sendKeys("password");
          expect(SAVE_ATTR).toBe("true");

          const NEW_CONFIRM = _UTILSELEMENT.findByName("confirmPasword");
          NEW_CONFIRM.sendKeys("password");

          OLD.sendKeys('wrong');

          console.log("Click Save");
          const OK = _UTILSELEMENT.findByCSS(
            "span.inline-error-text"
          );

          SAVE.click().then(() => {
            _UTILSPROC
              .waitElemDisp(OK, "Waiting for the element on the screen")
              .then(() => {
                expect(OK).toBeTruthy();
              });
          });
        });
      });
  });

  it("Modify pass wrong new pass", () => {
    const CHANGE = _UTILSELEMENT.findById("changePassword");

    _UTILSPROC
      .waitElemDisp(CHANGE, "Waiting for the element on the screen")
      .then(() => {
        CHANGE.click().then(() => {
          const SAVE = _UTILSELEMENT.findById("savePassword");
          const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

          const NEW = _UTILSELEMENT.findByName("password");
          NEW.sendKeys("wrong");
          expect(SAVE_ATTR).toBe("true");

          const OLD = _UTILSELEMENT.findByName("oldPassword");
          OLD.sendKeys('changefocus');

          const ERR = _UTILSELEMENT.findByCSS("span.inline-error-text");
          expect(ERR).toBeTruthy();
      });
    });
  });

  it("Modify pass with wrong match", () => {
    const CHANGE = _UTILSELEMENT.findById("changePassword");

    _UTILSPROC
      .waitElemDisp(CHANGE, "Waiting for the element on the screen")
      .then(() => {
        CHANGE.click().then(() => {
          const SAVE = _UTILSELEMENT.findById("savePassword");
          const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

          const NEW = _UTILSELEMENT.findByName("password");
          NEW.sendKeys("password");
          expect(SAVE_ATTR).toBe("true");

          const NEW_CONFIRM = _UTILSELEMENT.findByName("confirmPasword");
          NEW_CONFIRM.sendKeys("password1");

          const OLD = _UTILSELEMENT.findByName("oldPassword");
          OLD.sendKeys('changefocus');

          const ERR = _UTILSELEMENT.findByCSS("span.inline-error-text");

          expect(ERR).toBeTruthy();
      });
    });
  });


  it("Modify pass on account", () => {
    const CHANGE = _UTILSELEMENT.findById("changePassword");

    _UTILSPROC
      .waitElemDisp(CHANGE, "Waiting for the element on the screen")
      .then(() => {
        CHANGE.click().then(() => {
          const SAVE = _UTILSELEMENT.findById("savePassword");
          const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

          const OLD = _UTILSELEMENT.findByName("oldPassword");
          OLD.sendKeys(DEFAULT_PASS);
          expect(SAVE_ATTR).toBe("true");

          const NEW = _UTILSELEMENT.findByName("password");
          NEW.sendKeys("password");
          expect(SAVE_ATTR).toBe("true");

          const NEW_CONFIRM = _UTILSELEMENT.findByName("confirmPasword");
          NEW_CONFIRM.sendKeys("password");

          console.log("Click Save");
          const OK = _UTILSELEMENT.findByCSS(
            "span.ui-messages-summary.ng-star-inserted"
          );

          SAVE.click().then(() => {
            _UTILSPROC
              .waitElemDisp(OK, "Waiting for the element on the screen")
              .then(() => {
                expect(OK).toBeTruthy();
              });
          });
        });
      });
  });

  it("Modify status of account", () => {
    const SAVE = _UTILSELEMENT.findById("SaveSftpAccount");

    _UTILSPROC
      .waitElemDisp(SAVE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsNotEmpty();
        const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

        console.log("Write Login");
        const LOGIN = _UTILSELEMENT.findByName("login");
        LOGIN.sendKeys("login2");
        expect(LOGIN.getAttribute("value")).toBe("login");

        console.log("Select Disabled");
        const STATUS = _UTILSELEMENT.findByName("status");
        _UTILSELEMENT.selectElementOnDropDown(STATUS, ".ui-dropdown-item", 1);

        console.log("Click Save");
        const OK = _UTILSELEMENT.findByCSS("span.ui-messages-summary.ng-star-inserted");

        SAVE.click().then(() => {
          _UTILSPROC
            .waitElemDisp(OK, "Waiting for the element on the screen")
            .then(() => {
              expect(OK).toBeTruthy();
            });
        });
      });
  });

  it("Delete the account (without PKey)", () => {
    const DELETE = _UTILSELEMENT.findById("DeleteSftpAccount");

    _UTILSPROC
      .waitElemDisp(DELETE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsNotEmpty();
        console.log("Click Delete");

        const OK = _UTILSELEMENT.findByCSS("span.ui-messages-summary.ng-star-inserted");
        DELETE.click().then(() => {
          _UTILSPROC
            .waitElemDisp(OK, "Waiting for the element on the screen")
            .then(() => {
              expect(OK).toBeTruthy();
            });
        });
      });
  });

  it("Create an account", () => {
    const SAVE = _UTILSELEMENT.findById("SaveSftpAccount");

    _UTILSPROC
      .waitElemDisp(SAVE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsEmpty();
        const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

        console.log("Write Login");
        const LOGIN = _UTILSELEMENT.findByName("login");
        LOGIN.sendKeys("login");
        expect(SAVE_ATTR).toBe("true");

        console.log("Select Enabled");
        const STATUS = _UTILSELEMENT.findByName("status");
        expect(STATUS).toBeDefined();
        expect(STATUS.getAttribute('ng-reflect-readonly')).toBe('true');

        console.log("Write PKey");

        const PKEY = _UTILSELEMENT.findByName("publicKey");
        PKEY.sendKeys(KEY);

        console.log("Click Save");
        const OK = _UTILSELEMENT.findByCSS(
          "span.ui-messages-summary.ng-star-inserted"
        );

        SAVE.click().then(() => {
          _UTILSPROC
            .waitElemDisp(OK, "Waiting for the element on the screen")
            .then(() => {
              expect(OK).toBeTruthy();
            });
        });
      });
  });

  it("Modify status of account", () => {
    const SAVE = _UTILSELEMENT.findById("SaveSftpAccount");

    _UTILSPROC
      .waitElemDisp(SAVE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsNotEmpty();
        const SAVE_ATTR = SAVE.getAttribute("ng-reflect-attr-disabled");

        console.log("Write Login");
        const LOGIN = _UTILSELEMENT.findByName("login");
        LOGIN.sendKeys("login2");
        expect(LOGIN.getAttribute("value")).toBe("login");

        console.log("Select Disabled");
        const STATUS = _UTILSELEMENT.findByName("status");
        _UTILSELEMENT.selectElementOnDropDown(STATUS, ".ui-dropdown-item", 1);

        console.log("Click Save");
        const OK = _UTILSELEMENT.findByCSS("span.ui-messages-summary.ng-star-inserted");

        SAVE.click().then(() => {
          _UTILSPROC
            .waitElemDisp(OK, "Waiting for the element on the screen")
            .then(() => {
              expect(OK).toBeTruthy();
            });
        });
      });
  });

  it("Delete the account", () => {
    const DELETE = _UTILSELEMENT.findById("DeleteSftpAccount");

    _UTILSPROC
      .waitElemDisp(DELETE, "Waiting for the element on the screen")
      .then(() => {
        PAGE.pageIsNotEmpty();
        console.log("Click Delete");

        const OK = _UTILSELEMENT.findByCSS("span.ui-messages-summary.ng-star-inserted");
        DELETE.click().then(() => {
          _UTILSPROC
            .waitElemDisp(OK, "Waiting for the element on the screen")
            .then(() => {
              expect(OK).toBeTruthy();
            });
        });
      });
  });

});
