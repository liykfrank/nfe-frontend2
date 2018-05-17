import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  @Input('title') title: string;
  @Input('source') source: string[];
  @Input('selectedSource') selectedSource: Object;
  public indexSource: number;
  @ViewChild('myDropDownList') myDropDownList: jqxDropDownListComponent;

  constructor() {
    this.indexSource = 0;
  }


  ngOnInit() {}

  setSelectedSource(event) {
    this.selectedSource['selected'] = this.myDropDownList.getSelectedItem().value;
  }

}
