import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { jqxRadioButtonComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxradiobutton';

@Component({
  selector: 'app-radiobutton',
  templateUrl: './radiobutton.component.html'
})
export class RadiobuttonComponent implements OnInit {

  @Input('title') title: string;
  @Input('checked') isChecked: boolean;
  @ViewChild('myRadioButton') radioButton: jqxRadioButtonComponent;
  @Input('parentObject') parentObject: Object;
  @Input('parentKey') parentKey: string;

  constructor() { }

  ngOnInit() { }

  setCheckedRadioButton() {
    //this.parentObject['value'] = this.radioButton.val();
    for (let key of Object.keys(this.parentObject)) {
      if (key == this.parentKey) {
        this.parentObject[key] = this.radioButton.val();
      }
    }
  }


}
