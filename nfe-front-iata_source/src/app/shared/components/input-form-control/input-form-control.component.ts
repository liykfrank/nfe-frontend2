import { ReactiveComponentBase } from '../../base/reactive-component-base';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { Component, EventEmitter, Injectable, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'bspl-input-form-control',
  templateUrl: './input-form-control.component.html',
  styleUrls: ['./input-form-control.component.scss']
})
@Injectable()
export class InputFormControlComponent extends ReactiveComponentBase implements OnInit {

  @Input('error') error: string;
  @Input('currency') currency?: string;
  @Input('label') label;
  @Input('wrapText') wrapText: boolean;
  @Input('type') type?: string;
  @Input('typingPattern') typingPattern?: string;
  @Input('decimalsNumber') decimalsNumber?: number;
  @Input('inputMaxTextLength') inputMaxTextLength ? = 500;
  @Input('disabledInput') disabledInput = false;
  @Input('inputValue') inputValue?: string;

  constructor(_translationService: TranslationService) {
    super(_translationService);
  }

  ngOnInit() {
    this.label = this.label = '' ? '&nbsp;' : this.label;
    this.createForm();

    if (this.disabledInput) {
      this.formControlParent.disable();
    }

    if (this.inputValue) {
      this.formControlParent.setValue(this.inputValue);
    }
  }



}
