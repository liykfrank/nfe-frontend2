import { SftpAccountsPasswordResource } from './services/resources/sftp-accounts-password-resource';
import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { SftpAccountsService } from "./services/sftp-accounts.service";
import { jqxTextAreaComponent } from "../../../node_modules/jqwidgets-scripts/jqwidgets-ts/angular_jqxtextarea";
import { SftpAccountComponent } from "./components/sftp-account/sftp-account.component";
import { ReactiveFormsModule, FormsModule } from "@angular/forms";
import { SftpAccountResource } from "./services/resources/sftp-account-resource";
import { SftpAccountsResource } from "./services/resources/sftp-accounts-resource";

@NgModule({
  imports: [SharedModule, ReactiveFormsModule, FormsModule],
  declarations: [
    SftpAccountComponent,
  ],
  entryComponents: [
    SftpAccountComponent
  ],
  providers: [SftpAccountResource, SftpAccountsResource, SftpAccountsPasswordResource, SftpAccountsService]
})
export class SftpAccountsModule {}
