import { NgModule } from '@angular/core';
import { TranslationModule } from 'angular-l10n';

import { SharedModule } from './../../shared.module';
import { ReasonsSelectorComponent } from './components/reasons-selector/reasons-selector.component';
import { ReasonsTextAreaComponent } from './components/reasons-text-area/reasons-text-area.component';
import { ReasonsComponent } from './reasons.component';
import { ReasonsService } from './services/reasons.service';

@NgModule({
  imports: [SharedModule, TranslationModule],
  declarations: [
    ReasonsComponent,
    ReasonsSelectorComponent,
    ReasonsTextAreaComponent
  ],
  providers: [ReasonsService],
  exports: [ReasonsComponent]
})
export class ReasonsModule {}
