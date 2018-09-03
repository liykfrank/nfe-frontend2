import { UserMaintenanceService } from './services/user-maintenance.service';
import { TemplateService } from '../../services/template.service';
import { UserComponent } from './user.component';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { UserRoutingModule } from './user-routing.module';
import { SharedModule } from '../../../../shared/shared.module';
import { NgModule } from '@angular/core';
import { CountryTerritoryService } from './services/country-territory.service';
import { UtilsService } from '../../../../shared/services/utils.service';

@NgModule({
  imports: [
    SharedModule,
    UserRoutingModule,
    IssueSharedModule
  ],
  declarations: [
    UserComponent
  ],
  providers: [TemplateService, CountryTerritoryService, UserMaintenanceService,
    UtilsService]
})
export class UserModule { }
