import { Component, OnInit, ViewChild, ViewContainerRef } from '@angular/core';
import { jqxTabsComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtabs';

@Component({
  selector: 'app-multitabs',
  templateUrl: './multitabs.component.html',
  styleUrls: ['./multitabs.component.scss']
})
export class MultitabsComponent implements OnInit {

  noticeDocuments: number = 0;

  constructor() { }

  ngOnInit() {
  }


}
