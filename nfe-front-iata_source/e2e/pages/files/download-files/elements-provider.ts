import { browser, by, element, ElementFinder, ElementArrayFinder } from "protractor";
import { UtilsProc } from "../../../utils/utils-poc";

export class ElementsProviderDown{

  getQueryFiles(): ElementFinder {
    const elem = element(by.linkText('Query Files'));
    expect(elem).toBeTruthy();
    return elem;
  }

  getFirstRow(): ElementFinder {
    const el = element(by.css('.jqx-grid-content div[role="row"]:first-child'));
    expect(el).toBeTruthy();
    return el;
  }
  getSecondRow(): ElementFinder {
    const el = element(by.css('div[role="row"]:nth-child(2)'));
    expect(el).toBeTruthy();
    return el;
  }

  getDownloadFiles() {
    const el = element(by.css("#downloadFiles"));
    expect(el).toBeTruthy();
    return el;
  }

  getExpandElement(): ElementFinder {
    const el = element(by.css('.expandCollapse img'));
    expect(el).toBeTruthy();
    return el;
  }


  getListStatus(){
    const el = element(by.name('downListStatus'));
    el.getText().then(data=> console.log(data));
    return el;
  }


  getDownloadedItem():ElementFinder{
     return this.getOptionByText('DOWNLOADED');
   }

  getOtionsItem():ElementArrayFinder{
   const options= element.all(by.css('div[role="option"].jqx-listitem-element'));
   return options;
  }

  getOptionByText(text):ElementFinder{
   return this.getOtionsItem().filter((option)=>
    {
      return option.getText().then(tx=>tx==text);
    }).get(0);
  }

  getSearchButton(){
    const el = element(by.css('#searchFiles button'));
    return el;
  }

  getFileStatusCellsTable(){
    const elems= element.all(by.css('div[role="gridcell"]:nth-child(6)'));
    elems.each((el) => {
      el.getText().then(str=>console.log('cell table file status ' + str));
    });
    return elems;
  }

  getCellsTable(column:number){
    const elems= element.all(by.css('div[role="gridcell"]:nth-child('+column+ ')'));
    elems.each((el) => {
      el.getText().then(str=>console.log('cell table ' + str));
    });
    return elems;
  }

  getCellTable(column:number,row:number):ElementFinder{
    const elems:ElementArrayFinder= element.all(by.css('div[role="gridcell"]:nth-child('+column+ ')'));
    return elems.get(row);
  }

  getInputTypeFile(){
    const el = element(by.css('jqxinput input'));
    return el;
  }

  getNotDataTable():ElementFinder{
    const el = element(by.css('.jqx-grid-cell span'));
    return el;
  }

  getSecondPageTable():ElementFinder{
    const el = element(by.css('.numpag:nth-child(2)'));
    return el;
  }
}
