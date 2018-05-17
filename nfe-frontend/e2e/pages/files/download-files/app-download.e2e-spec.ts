import { UtilsMenu } from "../../../utils/utils.menu";
import { browser } from "protractor";
import { AppPageDownloadFiles } from "./app-download.po";
import { ElementsProviderDown } from "./elements-provider";
import { ActionsEnum } from "../../../utils/utils.models";

describe("nw-front Page Query Files", () => {
  let page: AppPageDownloadFiles;
  page = new AppPageDownloadFiles(new ElementsProviderDown(), new UtilsMenu());

  beforeEach(() => {
    page.cleanDir();
    browser.waitForAngularEnabled(false);
    page.getHomeTab();
    page.navigateToQueryFiles();
  });

  it("should display tab download files", () => {
    page.waitFirstRow().then(() => {
      console.log("first row showed");
    });
  });

  it("download one file", () => {
    page.waitFirstRow().then(() => {
      page.selectFirstRow();
      page.clickDownload();
    });
  });

  it("download two files", () => {
    page.waitFirstRow().then(() => {
      page.selectFirstRow();
      page.selectSecondRow();
      page.clickDownload();
    });
  });

  it("get option Unread ", () => {
    page.waitFirstRow().then(() => {
      page.clickExpand();
      page.elProv
        .getListStatus()
        .click()
        .then(() => {
          browser.waitForAngular();
          browser.wait(() => page.elProv.getOptionByText("UNREAD").isPresent());
          page.elProv
            .getOptionByText("UNREAD")
            .getText()
            .then(tx => expect(tx).toBe("UNREAD"));
        });
    });
  });

  it("select Donloaded files", () => {
    page.waitFirstRow().then(() => {
      page.clickExpand();
      page.selectDownloadedItem();
      page.search();
      page.checkStatusCellDownloaded();
    });
  });

  it("select Donloaded files xx", () => {
    page.waitFirstRow().then(() => {
      page.clickExpand();
      page.selectItemList("DOWNLOADED");
      page.search();
      page.checkCellByText("DOWNLOADED", 6);
    });
  });

  it("select type files", () => {
    page.waitFirstRow().then(() => {
      page.clickExpand();
      page.setTypeFile("xxx");
      page.search();
      browser.sleep(5000);
      page.checkNotData();
    });
  });

  it('Try to open upload page ', () => {
    page.openUploadPage();
  });

});
