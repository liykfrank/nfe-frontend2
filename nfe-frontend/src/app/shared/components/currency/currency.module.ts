import { NgModule } from '@angular/core';
import { TranslationModule } from 'angular-l10n';

import { SharedModule } from './../../shared.module';
import { CurrencyComponent } from './currency.component';
import { CurrencyService } from './services/currency.service';

@NgModule({
  imports: [SharedModule, TranslationModule],
  providers: [CurrencyService],
  declarations: [CurrencyComponent],
  exports: [CurrencyComponent]
})
export class CurrencyModule {}
