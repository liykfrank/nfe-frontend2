import { Component, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { TranslationService } from 'angular-l10n';
import { ValidationErrors } from '@angular/forms';

@Component({
  selector: 'bspl-field-error',
  templateUrl: './field-error.component.html',
  styleUrls: ['./field-error.component.scss']
})
export class FieldErrorComponent implements OnChanges {

  @Input() errors?: ValidationErrors;

  public errorString: string;

  constructor(private _translationService: TranslationService) { }

  ngOnChanges(changes: SimpleChanges) {
    this.errorString = this._getValidatorsErrors();
   }

  hasErrorClass(): string {
    return this.errors ? 'show' : 'hide';
  }


  private _getValidatorsErrors(): string {
    let errorString = '';
    if (this.errors.required) {
      errorString += this._translationService.translate('FORM_CONTROL_VALIDATORS.required') + '\n';
    }

    if (this.errors.pattern) {
      errorString += this._translationService.translate('FORM_CONTROL_VALIDATORS.pattern') + '\n';
    }

    if (this.errors.maxlength) {
      errorString += this._translationService.translate('FORM_CONTROL_VALIDATORS.maxLength') + '\n';
    }

    if (this.errors.minlength) {
      errorString += this._translationService.translate('FORM_CONTROL_VALIDATORS.minLength') + '\n';
    }

    if (this.errors.customError) {
      errorString += this._translationService.translate(this.errors.customError.message) + '\n';
    }

    if (this.errors.backendError) {
      errorString += this.errors.backendError.message + '\n';
    }

    return errorString;
  }

}
