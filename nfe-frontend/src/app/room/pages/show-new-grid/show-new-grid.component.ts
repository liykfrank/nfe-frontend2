import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  Renderer2,
  Injector
} from "@angular/core";
import { jqxGridComponent } from "jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid";
import { NwAbstractComponent } from "../../../shared/base/abstract-component";
import { JqxNwGridComponent } from "../../../shared/components/jqx-nw-grid/jqx-nw-grid.component";
declare var jquery: any;
declare var $: any;
//import * as $ from 'jquery';

@Component({
  selector: "app-show-new-grid",
  templateUrl: "./show-new-grid.component.html",
  styleUrls: ["./show-new-grid.component.scss"]
})
export class ShowNewGridComponent extends NwAbstractComponent
  implements OnInit {
  @ViewChild("#gridReference") table: JqxNwGridComponent;

  constructor(
    private elRef: ElementRef,
    private render: Renderer2,
    protected injector: Injector
  ) {
    super(injector);
  }

  ngOnInit(): void {
    //throw new Error("Method not implemented.");
  }

  ngAfterViewInit() {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
  }

  source: any = {
    datatype: "xml",
    datafields: [
      { name: "Discontinued", type: "bool" },
      { name: "ProductName", type: "string" },
      { name: "QuantityPerUnit", type: "int" },
      { name: "UnitPrice", type: "float" },
      { name: "UnitsInStock", type: "float" }
    ],
    root: "Products",
    record: "Product",
    id: "ProductID",
    url: "../assets/products.xml"
  };

  sourceJson: any = {
    datatype: "json",
    datafields: [
      { name: "Discontinued", type: "bool" },
      { name: "ProductName", type: "string" },
      { name: "QuantityPerUnit", type: "int" },
      { name: "UnitPrice", type: "float" },
      { name: "UnitsInStock", type: "float" }
    ],
    url: "../assets/products.json"
  };


  dataAdapter: any = new jqx.dataAdapter(this.sourceJson);

  cellsrenderer = (
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
  };

  columns: any[] = [
    {
      text: "Discontinued",
      columntype: "checkbox",
      datafield: "Discontinued",
      align: "center"
    },
    { text: "Product Name", datafield: "ProductName", width: "20%" },
    {
      text: "Quantity per Unit",
      datafield: "QuantityPerUnit",
      cellsalign: "right",
      align: "right",
      width: "20%"
    },
    {
      text: "Unit Price",
      datafield: "UnitPrice",
      align: "right",
      cellsalign: "right",
      cellsformat: "c2",
      width: "15%"
    },
    {
      text: "Units In Stock",
      datafield: "UnitsInStock",
      cellsalign: "left",
      cellsrenderer: this.cellsrenderer,
      width: "20%"
    }
  ];

  pagerrenderer() {
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
  }
}
