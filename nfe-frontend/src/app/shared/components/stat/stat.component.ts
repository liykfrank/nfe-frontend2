import { Component, OnInit, Input, Output, EventEmitter, OnDestroy } from '@angular/core';
import { FormControl, FormGroup } from '../../../../../node_modules/@angular/forms';


@Component({
  selector: 'bspl-stat',
  templateUrl: './stat.component.html',
  styleUrls: ['./stat.component.scss']
})
export class StatComponent implements OnInit {

  inputFormControl = new FormControl('', []);
  inputForm: FormGroup;
  formControlParent: FormControl;
  error = '';
  value = '';

  @Input() freeStat = false;
  @Input() set defaultStat(con) {
    if (con && con != '') {
      this.value = con;
      this.createInputForm();
    }
  }

  @Input() label: string;
  @Input() select = false;
  @Input() domesticLabel: string;
  @Input() internacionalLabel: string;

  @Input('formGroupParent') formGroupParent: FormGroup;
  @Input('formControlParentName') formControlParentName: string;

  constructor() { }

  ngOnInit() {
    this.label = this.label = '' ? '&nbsp;' : this.label;
    this.createInputForm();
  }

  createInputForm(): void {

    if (this.freeStat) {
      this.value = this.value.substr(0, 1).toUpperCase();
    }

    if (this.formControlParentName) {
      this.formControlParent = (this.formGroupParent.get(this.formControlParentName) as FormControl);
      this.formControlParent.patchValue(this.value);
    } else {
      this.formControlParent = new FormControl(this.value, []);
    }

    this.inputForm = new FormGroup({
      inputFormControl: this.formControlParent
    });

    if (this.formControlParentName) {
      this.formGroupParent.setControl(this.formControlParentName, this.formControlParent);
    }
  }

  onError(err) {
    if (err) {
      this.error = 'The stat field must start with the characters D or I and its length can not be greater than 3';
    } else {
      this.error = '';

    }
  }

}
