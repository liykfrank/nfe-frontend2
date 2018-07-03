export class PageSizeList {
  list: number[];
  dropDownList: any[];
  defaultNumber: number;

  constructor() {
    this.list = [10, 25, 50, 100];
    this.dropDownList = [];
    this.defaultNumber = this.list[1];

    for (const num of this.list) {
      this.dropDownList.push({ label: String(num), value: num });
    }
  }
}
