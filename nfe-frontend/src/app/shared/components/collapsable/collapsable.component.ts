import { Component, EventEmitter, Input, Output } from '@angular/core';

import { GLOBALS } from '../../constants/globals';

@Component({
  selector: 'bspl-collapsable',
  templateUrl: './collapsable.component.html',
  styleUrls: ['./collapsable.component.scss']
})
export class CollapsableComponent {
  @Input('idCollapsable') idCollapsable: string;
  @Input('open') open = true;
  @Input('title') title: string;

  @Output() showHide: EventEmitter<boolean> = new EventEmitter(this.open);

  constructor() {}

  onShowHide() {
    this.open = !this.open;
    this.showHide.next(this.open);
  }

  checkOpen(): boolean {
    return this.open;
  }

  getOpenClass(): string {
    return this.open ? 'show' : '';
  }

  getCollapseImgSrc() {
    return this.open ? GLOBALS.ICONS.COLLAPSE_SRC : GLOBALS.ICONS.EXPAND_SRC;
  }
}
