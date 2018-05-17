import { ElementFinder, browser, protractor } from "protractor";
import { promise } from "selenium-webdriver";

const glob = require("glob");
const fs = require("fs");
const path = require("path");
const TIME_WAIT_DEFAULT:number= 7000;
export class UtilsProc {
  removeDirAll(directory) {
    fs.readdir(directory, (err, files) => {
      if (err) throw err;

      for (const file of files) {
        fs.unlink(path.join(directory, file), err => {
          if (err) {
            console.log(err);
            //throw err;
          }
        });
      }
    });
  }

  mkdir(dir) {
    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir);
    }
  }

  existFile(file): boolean {
    return fs.existsSync(file);
  }

  waitElemDisp(elem :ElementFinder,msg:string='elem**'): promise.Promise<void>{
    let EC = protractor.ExpectedConditions;
    browser.waitForAngular();
    let prsent=EC.visibilityOf(elem);
    return browser.wait(prsent,TIME_WAIT_DEFAULT,'waitting display '+msg).then(()=> {
        //expect(elem.isPresent()).toBeTruthy();
        console.log(msg+' is displayed');
        return;});

  }

  waitElemClEnable(elem :ElementFinder,msg:string='elem**'): promise.Promise<void>{
    let EC = protractor.ExpectedConditions;
    browser.waitForAngular();
    let enable=EC.elementToBeClickable(elem);
    return browser.wait(enable,TIME_WAIT_DEFAULT,'waitting clEnable '+msg).then(()=> {
       // expect(elem.isEnabled()).toBeTruthy();
        console.log(msg+' is clenabled');return;});

  }
}
