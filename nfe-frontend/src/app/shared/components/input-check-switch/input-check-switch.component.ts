import { Component, Input, OnInit } from '@angular/core';
import { ReactiveComponentBase } from '../../base/reactive-component-base';

@Component({
  selector: 'bspl-input-check-switch',
  templateUrl: './input-check-switch.component.html',
  styleUrls: ['./input-check-switch.component.scss']
})
export class InputCheckSwitchComponent extends ReactiveComponentBase {

  @Input('isCheckBox') isCheckBox = false;
  @Input('label') label: string;
  @Input('labelCheck') labelCheck: string;

  constructor() {
    super();
  }

  getLabelCheck() {
    return this.labelCheck ? this.labelCheck : '';
  }
}
