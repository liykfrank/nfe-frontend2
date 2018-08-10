import { TranslationService } from 'angular-l10n';
import { Injectable } from '@angular/core';
import { ReactiveComponentBase } from '../../../../base/reactive-component-base';
import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import { FormGroup } from '@angular/forms';

import { ReactiveFormHandler } from '../../../../base/reactive-form-handler';
import { ReasonDetailForm } from '../../models/reason-detail-form.model';

@Component({
  selector: 'bspl-reasons-text-area',
  templateUrl: './reasons-text-area.component.html',
  styleUrls: ['./reasons-text-area.component.scss']
})
@Injectable()
export class ReasonsTextAreaComponent extends ReactiveComponentBase
  implements OnInit {

  @Input() textAreaTitle: string;
  @Input() textAreaContent: string;


  constructor(_translationService: TranslationService) {
    super(_translationService);
  }

  ngOnInit() {
    this.createForm();
  }

  // getError(): string {
  //   return this.getValidatorsErrors();
  // }
}
