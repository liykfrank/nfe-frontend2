import { EmptyTestModule } from "./test/empty-test/empty-test.module";

import { DashboardTestModule } from "./test/dashboard-test/dashboard-test.module";
import { MonitorUrlTestModule } from "./test/monitor-url-test/monitor-url-test.module";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { TabParentComponent } from "./tab-parent.component";
import { SharedModule } from "../../../shared/shared.module";
import { TabsStateService } from "../../services/tabs-state.service";
import { DashboardComponent } from "../dashboard/dashboard.component";
import { EmptyComponent } from "./../empty/empty.component";
import { MonitorUrlComponent } from "../../../menu/monitor/monitor-url/monitor-url.component";
import { ActionsEnum } from "../../../shared/models/actions-enum.enum";
import { TabsProvider } from "./conf/tabs-provider";
import { ComponentFactoryResolver } from "@angular/core";
import { inject } from "@angular/core/src/render3";
import { DashboardService } from "../../services/dashboard.service";
import { CardIf } from "../../models/card.model";
import { Observable } from "rxjs/Observable";

xdescribe("TabParentComponent", () => {
  let component: TabParentComponent;
  let fixture: ComponentFixture<TabParentComponent>;

  const IMG_PATH = '../../../../assets/images/dashboard/';
  const STATS1 = IMG_PATH + 'stats1.png';
  const STATS2 = IMG_PATH + 'stats2.png';
  const card1: CardIf = {'title': 'test1', 'imgPath': IMG_PATH + STATS1};
  const card2: CardIf = {'title': 'test2', 'imgPath': IMG_PATH + STATS2};
  const cardsMock: CardIf[] = [card1, card2];

  const dashboardServiceSpy = jasmine.createSpyObj('DashboardService', ['getCards']);

  dashboardServiceSpy.getCards.and.returnValue(Observable.of(cardsMock));

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
      providers: [TabsStateService, {provide: DashboardService, useValue: dashboardServiceSpy}]
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
    console.log('catapum')
    let elems = component.index;

    console.log(elems);
    component.loadTab(ActionsEnum.MONITOR);
    expect(component.index).toEqual(elems + 1);
    console.log(component.index);
    component.loadTab(ActionsEnum.MONITOR);
    expect(component.index).toEqual(elems + 1);
  });

});
