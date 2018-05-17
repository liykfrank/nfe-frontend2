import { Component, OnInit, ViewChild, AfterViewInit, ElementRef, Renderer2, OnChanges, Injector } from '@angular/core';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';
import { NwAbstractComponent } from '../../base/abstract-component';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.scss']
})
export class GridComponent extends NwAbstractComponent implements OnInit, AfterViewInit {

  @ViewChild('#gridReference') table: jqxGridComponent;


  constructor(private elRef: ElementRef, private render: Renderer2,protected injector: Injector) {
    super(injector);
  }

  ngOnInit(): void {
    //throw new Error("Method not implemented.");
  }

  ngAfterViewInit() {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
  }

  source: any =
    {
      datatype: 'xml',
      datafields: [
        { name: 'Discontinued', type: 'bool' },
        { name: 'ProductName', type: 'string' },
        { name: 'QuantityPerUnit', type: 'int' },
        { name: 'UnitPrice', type: 'float' },
        { name: 'UnitsInStock', type: 'float' }
      ],
      root: 'Products',
      record: 'Product',
      id: 'ProductID',
      url: '../assets/products.xml'
    };

  dataAdapter: any = new jqx.dataAdapter(this.source);

  cellsrenderer = (row: number, columnfield: string, value: string | number, defaulthtml: string, columnproperties: any, rowdata: any): string => {
    if (value < 20) {
      return `<span style='margin: 4px; float:${columnproperties.cellsalign}; color: #ff0000;'>${value}</span>`;
    }
    else {
      return `<span style='margin: 4px; float:${columnproperties.cellsalign}; color: #008000;'>${value}</span>`;
    }
  };

  columns: any[] = [
    { text: 'Discontinued', columntype: 'checkbox', datafield: 'Discontinued', align: 'center' },
    { text: 'Product Name', datafield: 'ProductName', width: 250 },
    { text: 'Quantity per Unit', datafield: 'QuantityPerUnit', cellsalign: 'right', align: 'right' },
    { text: 'Unit Price', datafield: 'UnitPrice', align: 'right', cellsalign: 'right', cellsformat: 'c2' },
    { text: 'Units In Stock', datafield: 'UnitsInStock', cellsalign: 'right', cellsrenderer: this.cellsrenderer, width: 100 }
  ];

}
