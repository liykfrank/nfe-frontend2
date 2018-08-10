import { Component, EventEmitter, Injector, Input, Output } from '@angular/core';
import { GLOBALS } from '../../constants/globals';

@Component({
  selector: 'div-collapsable',
  templateUrl: './div-collapsable.component.html',
  styleUrls: ['./div-collapsable.component.scss']
})
export class DivCollapsableComponent {

  @Input('idCollapsable') idCollapsable: string;
  @Input('open') open: boolean = true;
  @Input('title') title: string;
  @Output() showHide: EventEmitter<boolean> = new EventEmitter(this.open);

  ICO_COLLAPSE_SRC: string = 'assets/images/utils/ico_collapse.svg';
  ICO_EXPAND_SRC: string = 'assets/images/utils/ico_expand.svg';

  constructor() {
  }

  onShowHide() {
    this.open = !this.open;
    this.showHide.next(this.open);
  }

  checkOpen(): boolean {
    return this.open;
  }

  getOpenClass(): string {
    if (this.open) {
      return 'show';
    }
    return '';
  }

  getCollapseImgSrc() {
    return this.open ? GLOBALS.ICONS.COLLAPSE_SRC : GLOBALS.ICONS.EXPAND_SRC;
  }

}
