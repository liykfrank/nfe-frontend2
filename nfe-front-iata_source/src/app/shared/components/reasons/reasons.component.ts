import { ReactiveComponentBase } from '../../base/reactive-component-base';
import {
  Component,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  EventEmitter
} from '@angular/core';

import { Alignment } from '../../enums/alignment.enum';
import { EnvironmentType } from './../../enums/environment-type.enum';
import { Reason } from './models/reason.model';
import { ReasonsService } from './services/reasons.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'bspl-reasons',
  templateUrl: './reasons.component.html',
  styleUrls: ['./reasons.component.scss']
})
export class ReasonsComponent implements OnInit, OnChanges {
  @Input() dropdownTitle: string;
  @Input() textAreaTitle: string;
  @Input() type: EnvironmentType;
  @Input() isoCode: string;
  @Input() alignment: Alignment;
  @Input() formGroupParent: FormGroup;
  @Input() formControlParentName: string;


  @Output() returnForm: EventEmitter<FormGroup> = new EventEmitter<FormGroup>();

  reasonsSelectOptions: Reason[] = [];
  reasonsDetails = '';

  constructor(private _reasonsService: ReasonsService) {
    console.log("-----------------------------------");
    console.log(this.formGroupParent);
  }

  reactiveFormCreated(): boolean {

        return this.formGroupParent != undefined && this.formControlParentName != undefined;
      }

  ngOnInit() {
    if (!this.type) {
      throw new Error('Obligatorio Campo type en componente');
    } else {
      this._findReasonList();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.isoCode) {
      this._findReasonList();
    }
  }

  onSelectorChange(event) {
    this.reasonsDetails += event + '\n';
  }

  onTextAreaChange(event) {
    this.reasonsDetails = event;
  }

  checkHorizontalAlignment() {
    return this.alignment === Alignment.HORIZONTAL;
  }

  onReturnForm(value: FormGroup) {
    this.reasonsDetails = value.get('issueReason').value;
    this.returnForm.emit(value);
  }

  private _findReasonList() {
    if (this.isoCode && this.isoCode.length > 0) {
      this._reasonsService
        .getWithParams(this.isoCode, this.type)
        .subscribe(elems => {
          this.reasonsSelectOptions = elems;
          this.reasonsDetails = '';
        });
    }
  }
}
