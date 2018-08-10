import { EnumTypesFilter } from "./enum-types-filter.enum";

export interface IElementFilter {
  id: any;
  name: string;
  initValue: any;
  newValue: any;
  resetFilter(): void;
  type?: EnumTypesFilter;
}
