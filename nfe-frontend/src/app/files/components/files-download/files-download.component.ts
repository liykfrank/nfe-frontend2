import { Pagination } from './../../models/pagination';
import { IFileDeleted } from './../../models/contract/delete-files.model';
import { ActionsEnum } from "./../../../shared/models/actions-enum.enum";
import { ListFilesFilter } from "./../../models/list-files-filter";
import { Observable } from "rxjs/Observable";
import {
  Component,
  OnInit,
  AfterViewInit,
  ViewChild,
  Injector
} from "@angular/core";
import { ListFilesService } from "../../services/list-files.service";
import { FileNw } from "../../models/file";
import { JqxNwGridComponent } from "../../../shared/components/jqx-nw-grid/jqx-nw-grid.component";
import { saveAs } from "file-saver/FileSaver";
import { NwAbstractComponent } from "../../../shared/base/abstract-component";
import { IElementFilter } from "../../../shared/components/filter-crumbs/ielement-filter.model";
import { EnumTypesFilter } from "../../../shared/components/filter-crumbs/enum-types-filter.enum";
import { UtilsService } from "../../../shared/services/utils.service";
import { jqxNotificationComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxnotification';
import {Message} from 'primeng/api';
import { MessageService } from 'primeng/components/common/messageservice';
import { TablePaginationComponent } from '../../../shared/components/table-pagination/table-pagination.component';
import { SortType } from '../../models/sort-type.enum';
import { TabsStateService } from "../../../core/services/tabs-state.service";

declare var $: any;
@Component({
  selector: "app-files-download",
  templateUrl: "./files-download.component.html",
  styleUrls: ["./files-download.component.scss"],
  providers: [MessageService]
})
export class FilesDownloadComponent extends NwAbstractComponent
  implements OnInit, AfterViewInit {
  @ViewChild(TablePaginationComponent) tablePagination: TablePaginationComponent;

  dataFiles: FileNw[] = [];
  elementsFilter: IElementFilter[];
  // init filters with initial data
  dataFilterIni: ListFilesFilter;
  //filter used for binding data to component
  dataFilterLoad: ListFilesFilter;
  //filter with cuurent search filter
  dataFilterCurrent: ListFilesFilter;

  statusList: string[];

  actions = ActionsEnum;

  public readOnly: Boolean = false;

  sourceArray:any= {
    sort: (column: any, direction: string | boolean) : void => {
      this.tablePagination.customsortfunc(column,direction,this.dataFilterCurrent,this.dataFilterIni);
    },
    localdata: this.dataFiles,
    datafields: [
      { name: "id", type: "number" },
      { name: "name", type: "string" },
      { name: "type", type: "string" },
      { name: "bytes", type: "numbre" },
      { name: "uploadDate", type: "date" },
      { name: "status", type: "string" }
    ],
    datatype: "json",
  //  pagenum: 0,
  //  pagesize: 3,
  /*   pager: (pagenum: any, pagesize: any, oldpagenum: any): void => {
        // callback called when a page or page size is changed.
    } */
  };

  dataAdapter: any = new jqx.dataAdapter(
    this.sourceArray
    //  ,{ autoBind: true }
  );

  constructor(
    private listFilesServ: ListFilesService,
    private utils: UtilsService,
    injector: Injector,
    private messageService: MessageService,
    public tabsService: TabsStateService
  ) {
    super(injector);
  }

  ngOnInit() {
    this.statusList = this.listFilesServ.getStatusCodes();
    //create initial filter
    this.dataFilterIni = this.getDataFilterIni();
    //clone data filter from initial
    this.dataFilterCurrent = this.utils.cloneObj(this.dataFilterIni);
    this.dataFilterLoad = this.utils.cloneObj(this.dataFilterIni);


  }

  ngAfterViewInit(): void {
    this.loadDataTable(this.dataFilterIni);
  }

  /*  get dataFilterClone(){
    return Object.assign({}, this.dataFilter);;
  } */

  private getDataFilterIni(): ListFilesFilter {
    const filter = new ListFilesFilter();
    filter.minUploadDate = new Date(2017, 11, 11);
    filter.maxUploadDate = new Date(2018, 6, 1);
    filter.status = "ALL STATUS";
    filter.type = null;
    filter.sort = null;
    //Pgination data
    filter.sizePage=3;
    filter.numberPage=0;
    return filter;
  }
  /**
   * New search with new filter,reset pagination and sort, load the table and update the crumbs.
   * This method is suitable for the serach filter action.
   * @param event -new filter
   */
  searchCl(event) {
    const filterData: ListFilesFilter = event;
    //reset pagination
    filterData.sizePage=this.dataFilterIni.sizePage;
    filterData.numberPage=this.dataFilterIni.numberPage;
    //reset sort
    filterData.sort=null;
    //load data table
    this.loadDataTable(filterData,true);
    //update cuurent search filters
    this.dataFilterCurrent = filterData;
    this.tablePagination.removeSort(); //with current sort=null
    //update the crumbs with the new query comparing to initial data
    this.updateFilterCrumbs(this.dataFilterIni, filterData);
  }

  /**
   * Load table data with the filter and update pagination data
   * @param filter apllied
   * @param sort ,is already sorted??
   */
  loadDataTable(filter: ListFilesFilter,sort:boolean=false) {
    //const filter = new ListFilesFilter();
    // filter.minUploadDate = new Date();
    this.log.info("load table data ***");
    this.listFilesServ.listFilesData(filter).subscribe(data => {
      this.dataFiles = data.listData;
      this.sourceArray.localdata = data.listData;
      this.tablePagination.updatePaginationData(data,filter.numberPage,sort);
    });
  }

  /**
   * this method is called when the page number changes,
   * reload the table with the new page number
   * @param page number
   */
  pageChange(page){
    this.log.info('page change download '+page);
    //update filters with new page
    this.dataFilterCurrent.numberPage=page;
    this.dataFilterLoad.numberPage=page;
    //reload table with new page
    this.loadDataTable(this.dataFilterCurrent,true);
  }

  /**
   * get files selected from the table
   */
  private getFilesSelected(): FileNw[] {
    return this.tablePagination.getAllSelected();
  }

  /**
   * ask is there are selected rows
   */
  isSelected(): boolean {
    return !(this.tablePagination.getAllSelected().length >0);
  }

  /**
   * download one or more selected files
   */
  download() {
    const files = this.getFilesSelected();
    if (files.length == 0) return;
    if (files.length == 1) {
      this.listFilesServ.downloadFile(files[0]).subscribe(data => {
        if (data.type == "text/xml") saveAs(data, "filenew.txt");
        //save or open
        else saveAs(data, "filenew");
        this.refreshQuery();
      });
      return;
    }
    if (files.length > 1) {
      this.listFilesServ.downloadFiles(files).subscribe(data => {
        //save or open
        saveAs(data, "filenew");
        this.refreshQuery();
      });
      return;
    }
  }
  /**
   * remove one or more selected files
   */
  remove() {
    console.log("remove calledddddd");
    const files = this.getFilesSelected();
    if (files.length == 0) return;
    if (files.length == 1) {
      this.listFilesServ.removeFile(files[0]).subscribe(data => {
        this.messageService.add({
          severity: "success",
          summary: "File deleted:",
          detail: ""
        });
        this.refreshQuery();
      });
      return;
    }
    if (files.length > 1) {
      this.listFilesServ
        .removeFiles(files)
        .subscribe((data: Array<IFileDeleted>) => {
          data.forEach(data => {
            this.messageService.add({
              severity: "success",
              summary: "File deleted:",
              detail: "status: " + data.status.toString()
            });
          });
          this.refreshQuery();
        });
      return;
    }
  }

  private refreshQuery() {
    this.tablePagination.clearselection();
    this.searchCl(this.dataFilterCurrent);
  }

  public updateFilterCrumbs(
    filterIni: ListFilesFilter,
    filterNew: ListFilesFilter
  ) {
    const elementsFilter: Array<IElementFilter> = [
      {
        id: 1,
        name: "DatesFilter",
        initValue: {
          from: filterIni.minUploadDate,
          to: filterIni.maxUploadDate
        },
        newValue: {
          from: filterNew.minUploadDate,
          to: filterNew.maxUploadDate
        },
        resetFilter: () => {
          this.resetFilterBread(filters => {
            filters.maxUploadDate = filterIni.maxUploadDate;
            filters.minUploadDate = filterIni.minUploadDate;
          });
        },
        type: EnumTypesFilter.RANGE_DATE
      },
      {
        id: 3,
        name: "FileType",
        initValue: filterIni.type,
        newValue: filterNew.type,
        resetFilter: () => {
          this.resetFilterBread(filters => (filters.type = filterIni.type));
        }
      },
      {
        id: 4,
        name: "StatusFilter",
        initValue: filterIni.status,
        newValue: filterNew.status,
        resetFilter: () => {
          this.resetFilterBread(filters => (filters.status = filterIni.status));
        }
      }
    ];
    this.elementsFilter = elementsFilter;
  }

  resetFilterBread(fn: (filters: ListFilesFilter) => void) {
    const filters = this.dataFilterCurrent;
    fn(filters);
    this.dataFilterLoad = this.utils.cloneObj(filters);
    this.searchCl(filters);
  }
  resetAllFilters() {
    this.dataFilterLoad = this.utils.cloneObj(this.dataFilterIni);
    this.searchCl(this.utils.cloneObj(this.dataFilterIni));
  }

  /*  private cellsrenderer = (
    row: number,
    columnfield: string,
    value: string | number,
    defaulthtml: string,
    columnproperties: any,
    rowdata: any
  ): string => {
    if (value < 20) {
      return `<span style='margin: 4px; float:${
        columnproperties.cellsalign
      }; color: #ff0000;'>${value}</span>`;
    } else {
      return `<span style='margin: 4px; float:${
        columnproperties.cellsalign
      }; color: #008000;'>${value}</span>`;
    }
  }; */

  private columns: any[] = [
    {
      text: this.translation.translate("File"),
      //text: "File",
      //   columntype: "checkbox",
      datafield: "name",
      align: "left",
      width: "auto"
    },
    {
      text: this.translation.translate("Date"),
      datafield: "uploadDate",
      cellsformat: "d",
      width: "20%"
    },
    {
      text: this.translation.translate("FileType"),
      datafield: "type",
      cellsalign: "left",
      align: "left",
      width: "20%"
    },
    {
      text: this.translation.translate("Size"),
      datafield: "bytes",
      align: "left",
      cellsalign: "left",
      width: "15%"
    },
    {
      text: this.translation.translate("StatusFilter"),
      datafield: "status",
      cellsalign: "left",
      width: "20%"
    }
  ];
}
