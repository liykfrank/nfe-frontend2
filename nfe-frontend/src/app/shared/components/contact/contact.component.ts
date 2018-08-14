import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { ReactiveFormHandler } from '../../base/reactive-form-handler';
import { ContactFormModel } from './models/contact-form-model';

@Component({
  selector: 'bspl-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent extends ReactiveFormHandler implements OnInit {

  @Input() contactFormModelGroup: FormGroup;

  private _disabledComponent = false;

  @Input()
  set disabledComponent(bool: boolean) {
    this._disabledComponent = bool;

    if (bool) {
      this.contactFormModelGroup.disable();
    } else {
      this.contactFormModelGroup.enable();
    }
  }

  get disabledComponent(): boolean {
    return this._disabledComponent;
  }

  constructor() {
    super();
  }

  ngOnInit(): void {
    this.subscribe(this.contactFormModelGroup);
  }
}
