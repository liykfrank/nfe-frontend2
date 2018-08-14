import { NgModule } from '@angular/core';
import { TranslationModule } from 'angular-l10n';
import { SharedModule } from './../../shared.module';
import { ReasonsSelectorComponent } from './components/reasons-selector/reasons-selector.component';
import { ReasonsService } from './services/reasons.service';

@NgModule({
  imports: [SharedModule, TranslationModule],
  declarations: [
    ReasonsSelectorComponent,
  ],
  providers: [ReasonsService],
  exports: [ReasonsSelectorComponent]
})
export class ReasonsModule {}
