import { Component, OnInit, ElementRef, Renderer2, ChangeDetectionStrategy, forwardRef } from '@angular/core';
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

export const CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => JqxNwComboComponent),
  multi: true
}
@Component({
  selector: 'jqxDropDownListNw',
  template: '<div><ng-content></ng-content></div>',
  providers: [CUSTOM_INPUT_CONTROL_VALUE_ACCESSOR],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class JqxNwComboComponent extends jqxDropDownListComponent implements OnInit {

  constructor(private el: ElementRef, private rend: Renderer2) {super(el); }

  writeValue(value: any): void {
    console.log('new data='+ value);
    this.selectItem(value as jqwidgets.DropDownListItem);
    super.writeValue(value);
}

}
