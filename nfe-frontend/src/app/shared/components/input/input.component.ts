import { Component, Input } from '@angular/core';
import { jqxInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxinput';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html'
})

export class InputComponent {

  @Input('title') title: string;
  @Input('width') width: number;
  @Input('height') height: number;
  @Input('model') model: Object;

  constructor() {}

}
