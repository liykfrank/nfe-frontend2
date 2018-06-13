import { Component, OnInit, ViewChild } from "@angular/core";
import { jqxTabsComponent } from "jqwidgets-scripts/jqwidgets-ts/angular_jqxtabs";

@Component({
  selector: "app-tabs-file",
  templateUrl: "./tabs-file.component.html",
  styleUrls: ["./tabs-file.component.scss"]
})
export class TabsFileComponent implements OnInit {
  @ViewChild("tabsReference") myTabs: jqxTabsComponent;

  constructor() {}

  ngOnInit() {}

  onChangeAnimation(event: any): void {
    let checked = event.args.checked;
    this.myTabs.selectionTracker(checked);
  }
  onChangeContentAnimation(event: any): void {
    let checked = event.args.checked;
    if (checked) {
      this.myTabs.animationType("fade");
    } else {
      this.myTabs.animationType("none");
    }
  }
}
