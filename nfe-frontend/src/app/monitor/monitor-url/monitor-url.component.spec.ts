import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { MonitorUrlComponent } from "./monitor-url.component";
import { SafeUrlPipe } from "./../../shared/components/pipes/safe-url.pipe";

describe("MonitorUrlComponent", () => {
  let component: MonitorUrlComponent;
  let fixture: ComponentFixture<MonitorUrlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MonitorUrlComponent, SafeUrlPipe]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitorUrlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
