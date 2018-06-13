import { IPageRows } from './irows-selected.model';

export class PageRowsHelper {
  listPagesRows: IPageRows[] = [];

  findPage(page: number): IPageRows {
    return this.listPagesRows.find(
      (pageRows: IPageRows) => pageRows.page == page
    );
  }

  addPage(newpage: number) {
    this.listPagesRows.push({ page: newpage, rows: [], rowsData: [] });
  }

  addRowPage(page: number, row: number, data: any) {
    let pageRows = this.findPage(page);
    if (pageRows == null) {
      this.addPage(page);
      pageRows = this.findPage(page);
    }
    pageRows.rows.push(row);
    pageRows.rowsData.push(data);
  }

  delPage(page: number) {
    this.listPagesRows = this.listPagesRows.filter(
      (pageRows: IPageRows) => pageRows.page != page
    );
  }

  getAllData(): any[] {
    const allData = [];

    this.listPagesRows.forEach(pr => {
      pr.rowsData.forEach(data => allData.push(data));
    });
    return allData;
  }

  clean() {
    this.listPagesRows = [];
  }
}
