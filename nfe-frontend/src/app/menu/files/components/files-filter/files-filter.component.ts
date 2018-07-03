import { UtilsService } from './../../../../shared/services/utils.service';
import { ListFilesFilter } from "./../../models/list-files-filter";
import {
  Component,
  OnInit,
  Renderer2,
  ViewChild,
  Output,
  EventEmitter,
  Injector,
  AfterViewInit,
  Input,
  OnChanges,
  SimpleChanges
} from "@angular/core";
import { jqxDropDownListComponent } from "jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist";
import { NwAbstractComponent } from "../../../../shared/base/abstract-component";
import { jqxDateTimeInputComponent } from "jqwidgets-scripts/jqwidgets-ts/angular_jqxdatetimeinput";
import { jqxInputComponent } from "jqwidgets-scripts/jqwidgets-ts/angular_jqxinput";
import { IListItem } from "../../../../shared/base/ilist-item.model";
import { JqxNwComboComponent } from '../../../../shared/components/jqx-nw-combo/jqx-nw-combo.component';
import { NgForm } from '@angular/forms';

@Component({
  selector: "app-files-filter",
  templateUrl: "./files-filter.component.html",
  styleUrls: ["./files-filter.component.scss"]
})
export class FilesFilterComponent extends NwAbstractComponent
  implements OnInit, AfterViewInit, OnChanges {
  /////////////////////////////
  public showFilterResults: boolean;
  @Output("searchCl") searchCl = new EventEmitter<ListFilesFilter>();
  @Output("resetCl") resetCl = new EventEmitter();
  @Input() dataFilters: ListFilesFilter=new ListFilesFilter();
  @Input() statusList: string[]=[];
  @ViewChild("downListStatus")
  downStatusComp: JqxNwComboComponent;
  @ViewChild("myDateTimeInput")
   myDateTimeInput: jqxDateTimeInputComponent;

  @ViewChild("inputTypeFile")
   inputTypeFile: jqxInputComponent;
  @ViewChild("filesForm") form: NgForm;
  statusListItems: IListItem[];
  constructor(injector: Injector,private utils:UtilsService) {
    super(injector);
    this.showFilterResults = false;

    //   this.listSource = ["ADM", "ACM", "Reports", "Refunds"];
  }

  ngOnInit(): void {
     this.statusListItems = this.statusList.map<IListItem>(data => {
      return { name: data, code: data };
    });
  }
  ngAfterViewInit(): void {
    this.updateWidgets();
  }
  ngOnChanges(changes: SimpleChanges): void {
     for (let propName in changes) {
      let change = changes[propName];
      let curVal = JSON.stringify(change.currentValue);
      let prevVal = JSON.stringify(change.previousValue);
      if (!change.isFirstChange()) {
        this.updateWidgets()}
    }
  }

  updateWidgets(){
     /*  this.downStatusComp.selectItem(this.dataFilters
      .status as jqwidgets.DropDownListItem); */
   setTimeout(_ =>
    this.myDateTimeInput.setRange(
      this.dataFilters.minUploadDate,
      this.dataFilters.maxUploadDate
    )
  );
   this.inputTypeFile.value(this.dataFilters.type);
  }

  reset() {
    this.resetCl.emit();
  }

  showHideFilterResults(value): void {
    this.showFilterResults = value;
  }

  search() {
    const filterData = new ListFilesFilter();
    filterData.status = this.dataFilters.status;
    filterData.type = this.dataFilters.type;
    filterData.minUploadDate = this.dataFilters.minUploadDate;
    filterData.maxUploadDate = this.dataFilters.maxUploadDate;
    this.searchCl.emit(filterData);
    this.form.form.markAsPristine();

  }

  dateOnChange() {
    this.dataFilters.minUploadDate = this.myDateTimeInput.getRange({}).from;
    this.dataFilters.maxUploadDate = this.myDateTimeInput.getRange({}).to;
    this.form.form.markAsDirty();
  }

 /*  setValue() {
    //this.statusSelected_='DELETED';
    this.dataFilters.status='DELETED';
    this.dataFilters.type='lllll';
    const f=this.utils.cloneObj(this.dataFilters);
    f.status='DELETED';
    this.dataFilters=f;

  } */
}
