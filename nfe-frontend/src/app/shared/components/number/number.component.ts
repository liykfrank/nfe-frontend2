import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-number',
  templateUrl: './number.component.html'
})
export class NumberComponent implements OnInit {

  @Input('title') title: string;

  constructor() { }

  ngOnInit() {
  }

}
