import { ReactiveComponentBase } from './../../base/reactive-component-base';
import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup } from '../../../../../node_modules/@angular/forms';


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

  constructor() {
    super();
  }

  ngOnInit() {
    this.label = this.label = '' ? '&nbsp;' : this.label;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.freeStat && this.defaultStat && this.defaultStat.length > 0) {
      this.groupForm.get(this.controlName).setValue(this.defaultStat.substr(0, 1).toUpperCase());
    }
  }

}
