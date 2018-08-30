import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslationModule } from 'angular-l10n';

import { BsplInterceptor } from '../bspl-interceptor';
import { FieldErrorComponent } from './components/field-error/field-error.component';
import { InputCheckSwitchComponent } from './components/input-check-switch/input-check-switch.component';
import { InputFormControlComponent } from './components/input-form-control/input-form-control.component';
import { DecimalsFormatterDirective } from './directives/decimals-formatter.directive';
import { ForceWidthDirective } from './directives/force-width.directive';
import { IdentifierDirective } from './directives/identifier.directive';
import { InputRegexDirective } from './directives/input-regex.directive';
import { PatternDirective } from './directives/pattern.directive';
import { AbsolutePipe } from './pipes/absolute.pipe';
import { DecimalsFormatterPipe } from './pipes/decimals-formatter.pipe';
import { SafeUrlPipe } from './pipes/safe-url.pipe';

@NgModule({
  imports: [CommonModule, TranslationModule, ReactiveFormsModule, FormsModule],
  declarations: [
    AbsolutePipe,
    DecimalsFormatterPipe,
    SafeUrlPipe,
    DecimalsFormatterDirective,
    ForceWidthDirective,
    IdentifierDirective,
    InputRegexDirective,
    InputCheckSwitchComponent,
    PatternDirective,
    InputFormControlComponent,
    FieldErrorComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: BsplInterceptor,
      multi: true
    }
  ],
  exports: [
    TranslationModule,
    CommonModule,
    AbsolutePipe,
    DecimalsFormatterPipe,
    SafeUrlPipe,
    DecimalsFormatterDirective,
    ForceWidthDirective,
    IdentifierDirective,
    InputRegexDirective,
    InputCheckSwitchComponent,
    PatternDirective,
    InputFormControlComponent,
    FieldErrorComponent,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class SharedModule {}
