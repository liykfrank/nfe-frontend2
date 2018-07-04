
import { Component, Injector, Input, Output, ElementRef, ViewChild, HostBinding, EventEmitter } from '@angular/core';
/*
import { jqxExpanderComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxExpander';
 */
import { NwAbstractComponent } from '../../base/abstract-component';


@Component({
  selector: 'div-collapsable',
  templateUrl: './div-collapsable.component.html',
  styleUrls: ['./div-collapsable.component.scss']
})
export class DivCollapsableComponent extends NwAbstractComponent {

  @Input('open') open: boolean = true;
  @Input('title') title: string;
  @Output() showHide: EventEmitter<boolean> = new EventEmitter(this.open);

  constructor(private _injector: Injector) {
    super(_injector);
  }

  onShowHide() {
    this.open = !this.open;
    this.showHide.next(this.open);
  }

  checkOpen() {
    return this.open;
  }
}
