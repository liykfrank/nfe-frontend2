import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdmAcmComponent } from './components/adm-acm/adm-acm.component';
import { SharedModule } from '../../shared/shared.module';
import { BasicInfoComponent } from './components/basic-info/basic-info.component';
import { AmountComponent } from './components/amount/amount.component';
import { DetailsComponent } from './components/details/details.component';
import { ResumeBarComponent } from './components/resume-bar/resume-bar.component';
import { MultitabsComponent } from './components/multitabs/multitabs.component';

import { ConfigurationService } from './services/resources/configuration.service';
import { AgentService } from './services/resources/agent.service';
import { CompanyService } from './services/resources/company.service';
import { BasicInfoService } from './services/basic-info.service';
import { AmountService } from './services/amount.service';
import { AdmAcmService } from './services/adm-acm.service';
import { UtilsService } from '../../shared/services/utils.service';
import { CountryService } from './services/resources/country.service';
import { PeriodService } from './services/resources/period.service';

import { DetailsService } from './services/details.service';
import { CurrencyService } from './services/resources/currency.service';
import { TocaService } from './services/resources/toca.service';
import { DocumentsComponent } from './components/documents/documents.component';
import { CommentsComponent } from './components/comments/comments.component';

import { FilesComponent } from './components/files/files.component';
import { DocumentPopUpComponent } from './components/document-pop-up/document-pop-up.component';

@NgModule({
  imports: [CommonModule, SharedModule],
  declarations: [
    AdmAcmComponent,
    BasicInfoComponent,
    AmountComponent,
    DetailsComponent,
    ResumeBarComponent,
    MultitabsComponent,
    CommentsComponent,
    FilesComponent,
    DocumentsComponent,
    DocumentPopUpComponent

  ],

  entryComponents: [AdmAcmComponent],
  providers: [
    ConfigurationService,
    AgentService,
    CompanyService,
    CountryService,
    CurrencyService,
    TocaService,
    PeriodService,
    BasicInfoService,
    AmountService,
    DetailsService,
    AdmAcmService
  ]
})

export class AdmAcmModule {}
