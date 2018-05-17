import { DashboardTestModule } from "./test/dashboard-test/dashboard-test.module";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { TabParentComponent } from "./tab-parent.component";
import { SharedModule } from "../../../shared/shared.module";
import { TabsStateService } from "../../services/tabs-state.service";
import { DashboardComponent } from "../dashboard/dashboard.component";

describe("TabParentComponent", () => {
  let component: TabParentComponent;
  let fixture: ComponentFixture<TabParentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule, DashboardTestModule],
      declarations: [TabParentComponent, DashboardComponent],
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
});
