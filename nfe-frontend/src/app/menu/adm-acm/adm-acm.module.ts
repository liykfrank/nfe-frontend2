import { NgModule } from '@angular/core';
import { TranslationModule } from 'angular-l10n';

import { IssueSharedModule } from '../../shared/issue-shared.module';
import { DecimalsFormatterPipe } from '../../shared/pipes/decimals-formatter.pipe';
import { AdmAcmRoutingModule } from './adm-acm-routing.module';
import { AdmAcmComponent } from './adm-acm.component';
import { AmountAdmAcmComponent } from './components/amount-adm-acm/amount-adm-acm.component';
import { BasicInfoAdmAcmComponent } from './components/basic-info-adm-acm/basic-info-adm-acm.component';
import { DetailsAdmAcmComponent } from './components/details-adm-acm/details-adm-acm.component';
import { AcdmConfigurationService } from './services/adm-acm-configuration.service';
import { CountryService } from './services/country.service';
import { PeriodService } from './services/period.service';
import { TocaService } from './services/toca.service';
import { BasicInfoService } from './services/basic-info.service';
import { UtilsService } from '../../shared/services/utils.service';
import { AcdmsService } from './services/acdms.service';

@NgModule({
  imports: [AdmAcmRoutingModule, IssueSharedModule],
  providers: [
    DecimalsFormatterPipe,
    AcdmConfigurationService,
    AcdmsService,
    CountryService,
    PeriodService,
    TocaService,
    BasicInfoService,
    UtilsService
  ],
  declarations: [
    AdmAcmComponent,
    BasicInfoAdmAcmComponent,
    AmountAdmAcmComponent,
    DetailsAdmAcmComponent
  ],
  entryComponents: [AdmAcmComponent]
})
export class AdmAcmModule {}
