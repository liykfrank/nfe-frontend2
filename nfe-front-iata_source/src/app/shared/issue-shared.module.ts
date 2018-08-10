import { NgModule } from '@angular/core';
import { CalendarModule } from 'primeng/components/calendar/calendar';

import { AgentModule } from './components/agent/agent.module';
import { AirlineModule } from './components/airline/airline.module';
import { CollapsableComponent } from './components/collapsable/collapsable.component';
import { MultitabsModule } from './components/multitabs/multitabs.module';
import { PillsCollapsableComponent } from './components/pills-collapsable/pills-collapsable.component';
import { ReasonsModule } from './components/reasons/reasons.module';
import { ResumeBarComponent } from './components/resume-bar/resume-bar.component';
import { StatComponent } from './components/stat/stat.component';
import { SharedModule } from './shared.module';
import { CurrencyModule } from './components/currency/currency.module';

@NgModule({
  imports: [
    SharedModule,
    MultitabsModule,
    CalendarModule,
    AirlineModule,
    AgentModule,
    ReasonsModule,
    CurrencyModule
  ],
  declarations: [
    PillsCollapsableComponent,
    ResumeBarComponent,
    CollapsableComponent,
    StatComponent
  ],
  providers: [],
  exports: [
    PillsCollapsableComponent,
    ResumeBarComponent,
    CollapsableComponent,
    AirlineModule,
    AgentModule,
    CalendarModule,
    SharedModule,
    MultitabsModule,
    StatComponent,
    ReasonsModule,
    CurrencyModule
  ]
})
export class IssueSharedModule {}
