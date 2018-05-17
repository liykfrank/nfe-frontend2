import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-buttons',
  templateUrl: './buttons.component.html',
  styleUrls: ['./buttons.component.scss']
})
export class ButtonsComponent implements OnInit {

  @Input('title') title: string;
  @Input() disabled: boolean;
  @Output() clickBut= new EventEmitter();
  constructor() { }

  ngOnInit() {
  }

  cl(){
    this.clickBut.emit();
  }
}

