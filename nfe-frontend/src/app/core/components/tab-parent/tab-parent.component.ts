import { TabsStateService } from "./../../services/tabs-state.service";
import {
  Component,
  OnInit,
  AfterViewInit,
  Renderer2,
  ComponentFactoryResolver,
  ViewContainerRef,
  ViewChild,
  Injector
} from "@angular/core";
import { TabsProvider } from "./conf/tabs-provider";
import { ActionsEnum } from "../../../shared/models/actions-enum.enum";
import { jqxTabsComponent } from "jqwidgets-scripts/jqwidgets-ts/angular_jqxtabs";
import { NwAbstractComponent } from "../../../shared/base/abstract-component";

@Component({
  selector: "app-tab-parent",
  templateUrl: "./tab-parent.component.html",
  styleUrls: ["./tab-parent.component.scss"]
})
export class TabParentComponent extends NwAbstractComponent
  implements OnInit, AfterViewInit {
  index: number = 1;
  tabsProvider: any;
  ID_ACTION = "action_";

  @ViewChild("tabsReference") myTabs: jqxTabsComponent;

  constructor(
    private renderer: Renderer2,
    private cfResolver: ComponentFactoryResolver,
    public vcRef: ViewContainerRef,
    private tabsService: TabsStateService,
    protected injector: Injector
  ) {
    super(injector);
    this.tabsProvider = new TabsProvider();
    this.log.info("Tab parent created");
  }

  ngOnInit() {
    this.confTabs();
  }

  ngAfterViewInit() {
    this.loadTab(ActionsEnum.DASHBOARD);
    this.myTabs.removeFirst(); //close Init tab, the init tab is necessary for component
  }

  loadTab(action: ActionsEnum) {
    if (this.isActionIncluded(action)) {
      this.showActionIncluded(action);
      return;
    }

    const componentRef = this.tabsProvider
      .getTabProv(action)
      .getComp(this.cfResolver, this.vcRef);

    const d =
      "<div height='100%'>" +
      this.tabsProvider.getTabProv(action).title +
      "</div>";
    this.myTabs.addLast(
      d,
      "<div id=" + this.ID_ACTION + action + " height='100%'></div>"
    );
    document
      .getElementById(this.ID_ACTION + action)
      .appendChild(componentRef.location.nativeElement);
    this.index++;
  }

  isActionIncluded(action: ActionsEnum) {
    return document.getElementById(this.ID_ACTION + action) != null;
  }

  showActionIncluded(action: ActionsEnum) {
    const el: HTMLElement = document.getElementById(this.ID_ACTION + action);
    const indexTab = this.getIndexNode(el.parentNode);
    console.log("position tab " + this.getIndexNode(el.parentNode));
    this.myTabs.ensureVisible(indexTab);
    //this.myTabs.enableAt(indexTab);
    this.myTabs.select(indexTab);
  }

  /* getTi() {
    const el: HTMLElement = document.getElementById("2");

    console.log("position tab "+ this.getIndexNode(el.parentNode))
    const tabs = this.myTabs.length();
    for (var i = 0; i < tabs; i++) {
      const titleTab = this.myTabs.getTitleAt(i);
      console.log(titleTab);
    }
    //this.myTabs.elementRef.nativeElement.
  }
 */
  getIndexNode(el): number {
    var i = 0;
    while ((el = el.previousSibling) != null) i++;
    return i;
  }

  confTabs(): any {
    this.tabsService.getObserTabs().subscribe(action => {
      if (action) this.loadTab(action);
    });
  }

  add($event) {
    const m = this.myTabs.length();
    this.myTabs.ensureVisible(m - 1);
    console.log($event);
  }
}
