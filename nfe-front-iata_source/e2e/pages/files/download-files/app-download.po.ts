import { UtilsMenu } from "../../../utils/utils.menu";
import { ElementsProviderDown } from "./elements-provider";
import { browser, by, element, ElementFinder, promise, protractor } from "protractor";
import { UtilsProc } from "../../../utils/utils-poc";
import { ActionsEnum } from "../../../utils/utils.models";

export class AppPageDownloadFiles {
  path = process.cwd() + "/e2e/resources/downloads/";
  utils = new UtilsProc();

  constructor(
    public elProv: ElementsProviderDown,
    private _utilsMenu: UtilsMenu
  ) {}

  cleanDir() {
    this.utils.removeDirAll(this.path);
  }

  navigateToQueryFiles() {
    this._utilsMenu.navigateToMenu(ActionsEnum.FILES, ActionsEnum.QUERY_FILES);
  }

  waitQueryFiles(): promise.Promise<void> {
    return this.utils.waitElemDisp(this.elProv.getQueryFiles(), "QueryFiles");
  }

  waitFirstRow(): promise.Promise<void> {
    return this.utils.waitElemDisp(this.elProv.getFirstRow(), "FirstRow");
  }

  getParagraphText() {
    return element(by.css("app-root h1")).getText();
  }

  getHomeTab() {
    browser.get("/");
    browser.waitForAngular();
    return;
  }

  selectFirstRow() {
    this.elProv.getFirstRow().click();
  }
  selectSecondRow() {
    this.elProv.getSecondRow().click();
  }

  clickDownload() {
    this.utils.waitElemClEnable(this.elProv.getDownloadFiles(), "Download");
    this.elProv.getDownloadFiles().click();
    browser
      .wait(
        () => {
          return (
            this.utils.existFile(this.path + "file1.txt") ||
            this.utils.existFile(this.path + "allfiles.zip")
          );
        },
        7000,
        "files not downloded**"
      )
      .then(() => {
        console.log("file downloaded");
      },
      (error) => {
        console.log("file not downloaded -- "+error);
      }
    );
    return;
  }

  clickExpand() {
    this.elProv.getExpandElement().click();
  }

  selectDownloadedItem() {
    this.elProv.getListStatus().click();
    this.utils.waitElemClEnable(this.elProv.getDownloadedItem());
    const el = this.elProv.getDownloadedItem().click();
    browser.sleep(1000);
  }

  selectItemList(item: string) {
    this.elProv.getListStatus().click();
    this.utils.waitElemClEnable(this.elProv.getOptionByText(item));
    const el = this.elProv.getOptionByText(item).click();
    browser.sleep(1000);
  }

  search() {
    this.elProv.getSearchButton().click();
    browser.sleep(3000);
  }

  checkStatusCellDownloaded() {
    this.elProv.getFileStatusCellsTable().each(statusCell => {
      statusCell.getText().then(tx => expect(tx).toBe("DOWNLOADED"));
    });
  }

  checkCellByText(text: string, column: number) {
    this.elProv.getCellsTable(column).each(cell => {
      cell.getText().then(tx => expect(tx).toBe(text));
    });
  }

  setTypeFile(type) {
    this.elProv
      .getInputTypeFile()
      .isPresent()
      .then(() => console.log("input type file "));
    this.elProv.getInputTypeFile().click();
    this.elProv.getInputTypeFile().sendKeys(type);
    browser.sleep(3000);
  }

  checkNotData() {
    this.utils.waitElemDisp(this.elProv.getNotDataTable()).then(()=>
      {
        this.elProv.getNotDataTable().getText().then(tx=>expect(tx).toBe('No data to display'))
      }
    );
  }

  openUploadPage() {
    this.utils.waitElemClEnable(element(by.id("uploadFiles"))).then(() => {
      expect(element(by.css(".files-upload"))).toBeTruthy();
    });
  }

  goToSecondPage(){
    this.utils.waitElemClEnable(this.elProv.getSecondPageTable());
    this.elProv.getSecondPageTable().click();
  }

  checkSecondPage(){
    const cell=this.elProv.getCellTable(2,0);
    let EC = protractor.ExpectedConditions;
    browser.waitForAngular();
    const expect =EC.textToBePresentInElement(cell,'file6');
    browser.wait(expect,10000,'waitting file6 element');
    cell.getText().then(t=>{
      console.log('text cell==='+ t);
    })

  }
}
