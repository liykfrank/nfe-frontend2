
import { Component, Injector, Input, ElementRef, ViewChild, HostBinding } from '@angular/core';
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

  constructor(private _injector: Injector) {
    super(_injector);
  }

  // TODO: Revisar esta función, el switch no se vincula bien con JQXExpander
  //      "al hacer doble-click se acciona correctamente el Switch, 
  //       pero el vinculo con la propiedad expanded no opera."
  onShowHide() {
    console.log('onShowHide', this.title);
    this.open = !this.open;
    console.log(this.open);
  }
}
