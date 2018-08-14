
import { Component, OnInit } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'bspl-monitor',
  templateUrl: 'monitor.component.html',
  styleUrls: ['./monitor.component.scss']
})
export class MonitorComponent implements OnInit {

  url: string;

  constructor() {}

  ngOnInit() {
    this.url = environment.monitorUrl;
  }
}
