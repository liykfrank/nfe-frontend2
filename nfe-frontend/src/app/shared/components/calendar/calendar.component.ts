import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html'
})
export class CalendarComponent implements OnInit {

  @Input('title') title: string;
  @Input('width') width: number;
  @Input('height') height: number;

  constructor() { }

  ngOnInit() {
  }

}
