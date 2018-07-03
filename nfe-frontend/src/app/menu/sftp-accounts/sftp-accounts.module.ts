
import { SharedModule } from "../../shared/shared.module";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { SftpAccountComponent } from "./components/sftp-account/sftp-account.component";

@NgModule({
  imports: [CommonModule, SharedModule],
  declarations: [ SftpAccountComponent ],
  entryComponents: [ SftpAccountComponent ]
})
export class SftpAccountsModule {}
