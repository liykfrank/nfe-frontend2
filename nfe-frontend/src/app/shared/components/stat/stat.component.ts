import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

import { ReactiveComponentBase } from './../../base/reactive-component-base';

@Component({
  selector: 'bspl-stat',
  templateUrl: './stat.component.html',
  styleUrls: ['./stat.component.scss']
})
export class StatComponent extends ReactiveComponentBase implements OnInit, OnChanges {

  @Input() freeStat = false;
  @Input() defaultStat: string;
  @Input() label: string;
  @Input() select = false;
  @Input() domesticLabel: string;
  @Input() internacionalLabel: string;

  value: string = '';

  constructor() {
    super();
  }

  ngOnInit() {
    this.label = this.label = '' ? '&nbsp;' : this.label;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.freeStat && this.defaultStat && this.defaultStat.length > 0) {
      this.groupForm.get(this.controlName).setValue(this.defaultStat.substr(0, 1).toUpperCase());
      this.value = this.defaultStat.substr(0, 1).toUpperCase();
    }
  }

}
