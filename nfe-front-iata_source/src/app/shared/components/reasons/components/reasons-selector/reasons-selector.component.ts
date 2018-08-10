import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

import { Reason } from '../../models/reason.model';

@Component({
  selector: 'bspl-reasons-selector',
  templateUrl: './reasons-selector.component.html',
  styleUrls: ['./reasons-selector.component.scss']
})
export class ReasonsSelectorComponent implements OnInit, OnChanges {

  @Input() dropdownTitle: string;
  @Input() dropdownOptions: Reason[];

  @Output() changeSelect: EventEmitter<string> = new EventEmitter();

  hide = true;

  constructor() { }

  ngOnInit() {
    this.hide = this.dropdownOptions.length > 0;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.dropdownOptions) {
      this.hide = this.dropdownOptions.length > 0;
    }
  }

  onChangeSelector(value) {
    if (value != -1) {
      this.changeSelect.emit(value);
    }
  }

}
