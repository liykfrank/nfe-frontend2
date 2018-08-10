import { PageSizeList } from './../../models/page-size-list.model';
import { FilterPaginationAbstract } from './../../base/filter-pagination-abstract';
import {
  Component,
  OnInit,
  Injector,
  Input,
  ContentChild,
  AfterContentInit,
  Output,
  EventEmitter,
  AfterViewInit
} from '@angular/core';
import { Pagination } from '../../../menu/files/models/pagination';
import { ListData } from '../../../menu/files/models/list-data';
import { PageRowsHelper } from './rows/page-rows-helper';
import { SortType } from '../../../menu/files/models/sort-type.enum';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';

@Component({
  selector: 'bspl-table-pagination',
  templateUrl: './table-pagination.component.html',
  styleUrls: ['./table-pagination.component.scss']
})
export class TablePaginationComponent
  implements OnInit, AfterContentInit, AfterViewInit {
  @Output() pageChange = new EventEmitter<number>();
  @Output() reload = new EventEmitter<any>();
  @Output() pageSizeChange = new EventEmitter<any>();
  @Input() pageCurrent: number;
  pagination: Pagination;
  @ContentChild('gridReference') table: jqxGridComponent;
  rowsHelper: PageRowsHelper;
  pagesar = [];
  showMask_: boolean = false;
  pageSizeList: PageSizeList;

  public ready: Boolean = false;

  constructor(injector: Injector) {
    this.rowsHelper = new PageRowsHelper();

    this.pagination = new Pagination({});
    this.pagination.totalPages = 0;
    this.pageCurrent = 0;
  }

  ngOnInit() {
    this.pageSizeList = new PageSizeList();
  }

  changePageSize(event: any): void {
    this.pageSizeChange.emit(event);
  }

  clNumPage(num: number) {
    this.pageChange.emit(num);
    this.showMask_ = true;
  }

  isCurrentPage(numPag) {
    return numPag == this.pageCurrent;
  }

  ngAfterContentInit(): void {}

  ngAfterViewInit(): void {
    this.ready = true;
  }

  getPages() {
    if (this.pagination) {
      this.pagesar.length = 0;
      for (let i = 0; i < this.pagination.totalPages; i++) {
        this.pagesar.push({});
      }
    }
    return this.pagesar;
  }

  public updatePaginationData<T>(
    listData: ListData<T>,
    pageCurrent: number,
    sort: boolean = false
  ) {
    this.updatePageRowsCurrent();
    this.pageCurrent = pageCurrent;
    this.pagination = listData.pagination;
    if (sort) {
      this.table.updatebounddata('sort');
    } else {
      this.table.updatebounddata('cells');
    }

    //refresh selection
    this.refreshPageSelection();
    this.getPages();
    this.hideMask();
  }

  refreshPageSelection() {
    console.log('on refreshPageSelection');
    if (this.table.getselectedrowindexes().length > 0) {
      this.table.clearselection();
    }
    const pageRows = this.rowsHelper.findPage(this.pageCurrent);
    console.log(pageRows);
    console.log(this.rowsHelper.listPagesRows);
    if (pageRows) {
      pageRows.rows.forEach(row => {
        this.table.selectrow(row);
      });
    }
  }

  updatePageRowsCurrent() {
    this.rowsHelper.delPage(this.pageCurrent);

    if (this.ready && this.table.getselectedrowindexes()) {
      this.table.getselectedrowindexes().forEach(num => {
        const data = this.table.getrowdata(num);
        this.rowsHelper.addRowPage(this.pageCurrent, num, data);
      });
    }
  }

  getAllSelected(): any[] {
    this.updatePageRowsCurrent();
    return this.rowsHelper.getAllData();
  }

  clearselection() {
    if (
      this.table.getselectedrowindexes() &&
      this.table.getselectedrowindexes().length > 0
    ) {
      this.table.clearselection();
    }
    this.rowsHelper.clean();
  }

  removeSort() {
    this.table.removesort();
  }

  customsortfunc = (
    column: any,
    direction: string | boolean,
    dataFilterCurrent: FilterPaginationAbstract,
    dataFilterIni: FilterPaginationAbstract
  ): void => {
    //reset pagination
    dataFilterCurrent.sizePage = dataFilterIni.sizePage;
    dataFilterCurrent.numberPage = dataFilterIni.numberPage;

    //update filter sort
    if (direction == null && dataFilterCurrent.sort) {
      //delete sort only if cuurent sort is defined
      dataFilterCurrent.sort = null;
      this.reload.emit({ filter: dataFilterCurrent });
    } else if (direction) {
      //load data with sort fields
      dataFilterCurrent.sort = [];
      dataFilterCurrent.sort.push({
        name: column as string,
        type: direction as SortType
      });
      this.reload.emit({ filter: dataFilterCurrent });
    }
    };

  hideMask() {
    this.showMask_ = false;
  }
}
