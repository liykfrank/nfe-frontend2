import { ISortField } from "../../menu/files/models/isort.model";

export class FilterPaginationAbstract {
  sizePage: number;
  numberPage: number;
  sort: ISortField[];
}
