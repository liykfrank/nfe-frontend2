import { EmptyTestModule } from "./test/empty-test/empty-test.module";

import { DashboardTestModule } from "./test/dashboard-test/dashboard-test.module";
import { MonitorUrlTestModule } from "./test/monitor-url-test/monitor-url-test.module";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { TabParentComponent } from "./tab-parent.component";
import { SharedModule } from "../../../shared/shared.module";
import { TabsStateService } from "../../services/tabs-state.service";
import { DashboardComponent } from "../dashboard/dashboard.component";
import { EmptyComponent } from "./../empty/empty.component";
import { MonitorUrlComponent } from "./../../../monitor/monitor-url/monitor-url.component";
import { ActionsEnum } from "../../../shared/models/actions-enum.enum";
import { TabsProvider } from "./conf/tabs-provider";
import { ComponentFactoryResolver } from "@angular/core";
import { inject } from "@angular/core/src/render3";

describe("TabParentComponent", () => {
  let component: TabParentComponent;
  let fixture: ComponentFixture<TabParentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule,
        DashboardTestModule,
        MonitorUrlTestModule,
        EmptyTestModule
      ],
      declarations: [
        TabParentComponent,
        DashboardComponent,
        MonitorUrlComponent,
        EmptyComponent
      ],
      providers: [TabsStateService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabParentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("loadTab", () => {
    let elems = component.index;

    component.loadTab(ActionsEnum.MONITOR);
    expect(component.index).toEqual(elems + 1);

    component.loadTab(ActionsEnum.MONITOR);
    expect(component.index).toEqual(elems + 1);
  });

});
