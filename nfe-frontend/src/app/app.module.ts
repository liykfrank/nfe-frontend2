import { MonitorModule } from "./monitor/monitor.module";
import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AppComponent } from "./app.component";
import { CoreModule } from "./core/core.module";
import { FilesModule } from "./files/files.module";
import { RoomModule } from "./room/room.module";
import { L10nLoader } from "angular-l10n";
import {
  communInterceptorProvider,
  CommunInterceptor
} from "./core/interceptors/commun.interceptor";
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { SftpAccountsModule } from "./sftp-accounts/sftp-accounts.module";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FilesModule,
    SftpAccountsModule,
    CoreModule,
    RoomModule,
    MonitorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
