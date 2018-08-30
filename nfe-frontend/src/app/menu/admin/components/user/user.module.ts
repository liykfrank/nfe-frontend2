import { TemplateService } from '../../services/template.service';
import { UserComponent } from './user.component';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { UserRoutingModule } from './user-routing.module';
import { SharedModule } from '../../../../shared/shared.module';
import { NgModule } from '@angular/core';
import { CountryTerritoryService } from './services/country-territory.service';

@NgModule({
  imports: [
    SharedModule,
    UserRoutingModule,
    IssueSharedModule
  ],
  declarations: [
    UserComponent
  ],
  providers: [TemplateService, CountryTerritoryService]
})
export class UserModule { }
