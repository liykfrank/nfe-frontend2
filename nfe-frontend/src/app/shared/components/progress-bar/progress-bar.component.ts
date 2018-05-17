import { Component, OnInit, Input } from '@angular/core';
import { jqxProgressBarComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxprogressbar';

@Component({
  selector: 'app-progress-bar',
  templateUrl: './progress-bar.component.html',
  styleUrls: ['./progress-bar.component.scss']
})
export class ProgressBarComponent implements OnInit {

  @Input('orientation') orientation: string;
  @Input('width') width: number;
  @Input('height') height: number;
  @Input ('value') value: number;

  constructor() {}

  ngOnInit() {}

}
