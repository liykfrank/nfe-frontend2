import { environment } from "../../../../environments/environment";
import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-monitor-url",
  templateUrl: "./monitor-url.component.html",
  styleUrls: ["./monitor-url.component.scss"]
})
export class MonitorUrlComponent implements OnInit {
  url: string;

  constructor() {}

  ngOnInit() {
    this.url = environment.monitorUrl;
  }
}
