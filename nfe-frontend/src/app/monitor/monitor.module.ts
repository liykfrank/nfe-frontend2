import { SharedModule } from "./../shared/shared.module";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MonitorUrlComponent } from "./monitor-url/monitor-url.component";

@NgModule({
  imports: [CommonModule, SharedModule],
  entryComponents: [MonitorUrlComponent],
  declarations: [MonitorUrlComponent]
})
export class MonitorModule {}
