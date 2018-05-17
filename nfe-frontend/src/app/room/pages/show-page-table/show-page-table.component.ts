import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { JqxNwGridComponent } from "../../../shared/components/jqx-nw-grid/jqx-nw-grid.component";
import { Person } from '../../models/person';
import { PersonsService } from '../../resources/persons.service';
declare var jquery: any;
declare var $: any;


@Component({
  selector: 'app-show-page-table',
  templateUrl: './show-page-table.component.html',
  styleUrls: ['./show-page-table.component.scss']
})
export class ShowPageTableComponent implements OnInit,AfterViewInit {

  @ViewChild("gridReference") table: JqxNwGridComponent;

  persons: Person[];
  constructor(private personsService: PersonsService) { }

  ngOnInit() {

  }
  ngAfterViewInit(): void {
    this.personsService.get().subscribe(
      persons => {
      //this.dataFiles.push(d);
      this.persons = persons;
      this.sourceArray.localdata = this.persons;
      this.table.updatebounddata("cells");
    }

    )
  }

  sourceArray: any = {
    localdata: this.persons,
    datafields: [
      { name: "name", type: "string" },
      { name: "age", type: "numbre" },
      { name: "telephone", type: "string" }
    ],
    datatype: "json",
    pagenum: 0,
    pagesize: 5,
    pager: (pagenum: any, pagesize: any, oldpagenum: any): void => {
        // callback called when a page or page size is changed.
    }
  };


  dataAdapter: any = new jqx.dataAdapter(this.sourceArray);


  columns: any[] = [
    {
      text: "Name",
      datafield: "name",
      align: "center"
    },
    {
      text: "Age",
      datafield: "age",
      align: "center"
    },
    {
      text: "Telephone",
      datafield: "telephone",
      align: "center"
    }

  ];

  /* pagerrenderer() {
    var selectorGrid = "jqxgridnew > div";
    var theme = "newtheme";
    var element = $(
      "<div style='margin-left: 10px; margin-top: 5px; width: 100%; height: 100%;'></div>"
    );
    var datainfo = $(selectorGrid).jqxGrid("getdatainformation");
    var paginginfo = datainfo.paginginformation;
    var leftButton = $(
      "<div style='padding: 0px; float: left;'><div style='margin-left: 9px; width: 16px; height: 16px;'></div></div>"
    );
    leftButton.find("div").addClass("jqx-icon-arrow-left");
    leftButton.width(36);
    leftButton.jqxButton({ theme: theme });
    var rightButton = $(
      "<div style='padding: 0px; margin: 0px 3px; float: left;'><div style='margin-left: 9px; width: 16px; height: 16px;'></div></div>"
    );
    rightButton.find("div").addClass("jqx-icon-arrow-right");
    rightButton.width(36);
    rightButton.jqxButton({ theme: theme });
    leftButton.appendTo(element);
    rightButton.appendTo(element);
    var label = $(
      "<div style='font-size: 11px; margin: 2px 3px; font-weight: bold; float: left;'></div>"
    );
    label.text("1-" + paginginfo.pagesize + " of " + datainfo.rowscount);
    label.appendTo(element);
    ///self.label = label;
    console.log('label pagination= '+ label);
    // update buttons states.
    var handleStates = function(event, button, className, add) {
      button.on(event, function() {
        if (add == true) {
          button.find("div").addClass(className);
        } else button.find("div").removeClass(className);
      });
    };
    if (theme != "") {
      handleStates(
        "mousedown",
        rightButton,
        "jqx-icon-arrow-right-selected-" + theme,
        true
      );
      handleStates(
        "mouseup",
        rightButton,
        "jqx-icon-arrow-right-selected-" + theme,
        false
      );
      handleStates(
        "mousedown",
        leftButton,
        "jqx-icon-arrow-left-selected-" + theme,
        true
      );
      handleStates(
        "mouseup",
        leftButton,
        "jqx-icon-arrow-left-selected-" + theme,
        false
      );
      handleStates(
        "mouseenter",
        rightButton,
        "jqx-icon-arrow-right-hover-" + theme,
        true
      );
      handleStates(
        "mouseleave",
        rightButton,
        "jqx-icon-arrow-right-hover-" + theme,
        false
      );
      handleStates(
        "mouseenter",
        leftButton,
        "jqx-icon-arrow-left-hover-" + theme,
        true
      );
      handleStates(
        "mouseleave",
        leftButton,
        "jqx-icon-arrow-left-hover-" + theme,
        false
      );
    }
    rightButton.click(function() {
      $(selectorGrid).jqxGrid("gotonextpage");
    });
    leftButton.click(function() {
      $(selectorGrid).jqxGrid("gotoprevpage");
    });

    $(selectorGrid).on('pagechanged', function () {
      var datainfo = $(selectorGrid).jqxGrid('getdatainformation');
      var paginginfo = datainfo.paginginformation;
     // self.label.text(1 + paginginfo.pagenum * paginginfo.pagesize + "-" + Math.min(datainfo.rowscount, (paginginfo.pagenum + 1) * paginginfo.pagesize) + ' of ' + datainfo.rowscount);
      console.log(1 + paginginfo.pagenum * paginginfo.pagesize + "-" + Math.min(datainfo.rowscount, (paginginfo.pagenum + 1) * paginginfo.pagesize) + ' of ' + datainfo.rowscount);
    });

    return element;
  } */
}
