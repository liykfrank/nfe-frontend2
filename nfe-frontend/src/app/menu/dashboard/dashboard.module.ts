import { DashboardComponent } from './dashboard.component';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { DashBoardRoutingModule } from './dashboard-routing.module';
import { DashboardService } from '../../core/services/dashboard.service';
import { jqxDockingComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdocking';


@NgModule({
  imports: [CommonModule, DashBoardRoutingModule],
  declarations: [
    DashboardComponent,
    jqxDockingComponent
  ],
  providers: [DashboardService],
  entryComponents: [DashboardComponent],
})

export class DashBoardModule {}
