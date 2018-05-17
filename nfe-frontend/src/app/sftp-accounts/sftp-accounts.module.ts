import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { SftpAccountsService } from "./services/sftp-accounts.service";
import { SftpAccountsQueryComponent } from "./sftp-accounts-query/sftp-accounts-query.component";
import { SftpAccountMaintenanceComponent } from "./sftp-account-maintenance/sftp-account-maintenance.component";
import { SftpAccountGridComponent } from "./sftp-account-grid/sftp-account-grid.component";
import { jqxTextAreaComponent } from "../../../node_modules/jqwidgets-scripts/jqwidgets-ts/angular_jqxtextarea";
import { SftpAccountComponent } from "./sftp-account/sftp-account.component";
import { ReactiveFormsModule } from "@angular/forms";

@NgModule({
  imports: [SharedModule, ReactiveFormsModule],
  declarations: [
    jqxTextAreaComponent,
    SftpAccountsQueryComponent,
    SftpAccountComponent,
    SftpAccountMaintenanceComponent,
    SftpAccountGridComponent
  ],
  entryComponents: [
    jqxTextAreaComponent,
    SftpAccountsQueryComponent,
    SftpAccountMaintenanceComponent
  ],
  providers: [SftpAccountsService]
})
export class SftpAccountsModule {}
