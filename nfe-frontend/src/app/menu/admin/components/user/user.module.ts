import { NgModule } from '@angular/core';
import { CalendarModule } from 'primeng/primeng';

import { CustomDropdownsModule } from '../../../../shared/components/custom-dropdowns/custom-dropdowns.module';
import { UtilsService } from '../../../../shared/services/utils.service';
import { SharedModule } from '../../../../shared/shared.module';
import { TemplateService } from '../../services/template.service';
import { AgentModule } from './../../../../shared/components/agent/agent.module';
import { AirlineModule } from './../../../../shared/components/airline/airline.module';
import { CountryTerritoryService } from './services/country-territory.service';
import { UserMaintenanceService } from './services/user-maintenance.service';
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './user.component';

@NgModule({
  imports: [
    SharedModule,
    UserRoutingModule,
    AgentModule,
    AirlineModule,
    CalendarModule,
    CustomDropdownsModule
  ],
  declarations: [UserComponent],
  providers: [
    TemplateService,
    CountryTerritoryService,
    UserMaintenanceService,
    UtilsService
  ]
})
export class UserModule {}
