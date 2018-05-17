import {
  Component,
  OnInit,
  Input,
  OnChanges,
  SimpleChanges,
  Injector
} from "@angular/core";
import { IElementFilter } from "./ielement-filter.model";
import { EnumTypesFilter } from "./enum-types-filter.enum";
import { NwAbstractComponent } from "../../base/abstract-component";

@Component({
  selector: "app-filter-crumbs",
  templateUrl: "./filter-crumbs.component.html",
  styleUrls: ["./filter-crumbs.component.scss"]
})
export class FilterCrumbsComponent extends NwAbstractComponent implements OnInit, OnChanges {
  @Input() elementsFilter: Array<IElementFilter>;

  constructor(injector: Injector) {
    super(injector);
  }

  ngOnInit() {}

  ngOnChanges(changes: SimpleChanges): void {
    // throw new Error("Method not implemented.");
  }

  isElemShowed(elem: IElementFilter): boolean {
    if (elem.type) {
      if (elem.type == EnumTypesFilter.RANGE_DATE) {
        return (
          elem.initValue.from != elem.newValue.from ||
          elem.initValue.to != elem.newValue.to
        );
      }
    }
    return !(elem.initValue === elem.newValue);
  }

  resetElem(elem: IElementFilter) {
    elem.resetFilter();
  }
}
