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
} from "@angular/core";
import { NwAbstractComponent } from "../../base/abstract-component";
import { Pagination } from "../../../files/models/pagination";
import { ListData } from "../../../files/models/list-data";
import { PageRowsHelper } from "./rows/page-rows-helper";
import { SortType } from '../../../files/models/sort-type.enum';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';

@Component({
  selector: "app-table-pagination",
  templateUrl: "./table-pagination.component.html",
  styleUrls: ["./table-pagination.component.scss"]
})
export class TablePaginationComponent extends NwAbstractComponent
  implements OnInit, AfterContentInit,AfterViewInit {
  @Output() pageChange = new EventEmitter<number>();
  @Output() reload = new EventEmitter<any>();
  @Input() pageCurrent: number;
  pagination: Pagination;
  @ContentChild("gridReference") table: jqxGridComponent;
  rowsHelper: PageRowsHelper;
  pagesar=[];
  showMask_:boolean=false;

  constructor(injector: Injector) {
    super(injector);
    this.rowsHelper= new PageRowsHelper();
    //init pagination
    this.pagination= new Pagination({});
    this.pagination.totalPages=0;
    this.pageCurrent = 0;
  }

  ngOnInit() {

  }

  clNumPage(num: number) {
    console.log("on clNumPage");
    this.log.info("number page click " + num);
    //this.pageCurrent=num;
    this.pageChange.emit(num);
    this.log.info("grid rows " + this.table.getrows().length);
    this.showMask_=true;
  }

  isCurrentPage(numPag) {
    return numPag == this.pageCurrent;
  }

  ngAfterContentInit(): void {
   // this.log.info("grid rows " + this.table.getrows().length);

  }
  ngAfterViewInit(): void {
   //this.showMask();
  }
  getPages() {
    if(this.pagination){
      this.log.info('total pages getpages '+ this.pagination.totalPages);
      this.pagesar.length=0;
      for(let i=0;i<this.pagination.totalPages;i++){
        this.pagesar.push({});
      }
    }
    return this.pagesar;
  }

  public updatePaginationData<T>(listData: ListData<T>, pageCurrent: number, sort: boolean= false ) {
    this.updatePageRowsCurrent();
    this.pageCurrent = pageCurrent;
    this.pagination= listData.pagination;
    if(sort){
      this.table.updatebounddata("sort");
    }
    else{
      this.table.updatebounddata("cells");
    }
    //refresh selection
    this.refreshPageSelection();
    this.getPages();
    this.hideMask();
  }

  refreshPageSelection(){
    console.log("on refreshPageSelection");
    if(this.table.getselectedrowindexes().length>0){
      this.table.clearselection();
    }
    const pageRows= this.rowsHelper.findPage(this.pageCurrent);
    console.log(pageRows);
    console.log(this.rowsHelper.listPagesRows);
    if(pageRows){
        pageRows.rows.forEach((row)=>{
          this.table.selectrow(row);
          this.log.info('refresh row selected '+ row+ ' page '+ this.pageCurrent);
        });
      };
  }

  updatePageRowsCurrent(){
    this.rowsHelper.delPage(this.pageCurrent);
    if( this.table.getselectedrowindexes()){
      this.table.getselectedrowindexes().forEach((num)=>{
        const data = this.table.getrowdata(num);
        this.rowsHelper.addRowPage(this.pageCurrent,num,data);
        //this.log.info('add row selected '+ num+ ' page '+ this.pageCurrent);
        //console.log(this.rowsHelper.listPagesRows);
      });
    }
 }

 getAllSelected():any[]{
   this.updatePageRowsCurrent();
   return this.rowsHelper.getAllData();
 }

 clearselection(){
  if(this.table.getselectedrowindexes() && this.table.getselectedrowindexes().length>0){
    this.table.clearselection();
  }
  this.rowsHelper.clean();
 }

 removeSort(){
  this.table.removesort();
 }

 customsortfunc = (column: any, direction: string | boolean,
  dataFilterCurrent:FilterPaginationAbstract,dataFilterIni:FilterPaginationAbstract ) : void => {
  this.log.info('custom sort '+ column+ ' '+ direction);
  this.log.info('current sort '+ dataFilterCurrent.sort);
  //reset pagination
  dataFilterCurrent.sizePage=dataFilterIni.sizePage;
  dataFilterCurrent.numberPage=dataFilterIni.numberPage;

  //update filter sort
  if(direction==null && dataFilterCurrent.sort){ //delete sort only if cuurent sort is defined
    dataFilterCurrent.sort=null;
    this.reload.emit({filter:dataFilterCurrent});
  }else if(direction) { //load data with sort fields
    dataFilterCurrent.sort= [];
    dataFilterCurrent.sort.push({name:column as string,type:direction as SortType});
    this.reload.emit({filter:dataFilterCurrent});
  }

}

hideMask(){
  this.showMask_=false;
}
 showAlert(cad){
  alert('bbb');
 }
}
