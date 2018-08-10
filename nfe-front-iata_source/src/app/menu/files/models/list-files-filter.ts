import { FilterPaginationAbstract } from './../../../shared/base/filter-pagination-abstract';
import { ISortField } from "./isort.model";

export class ListFilesFilter extends FilterPaginationAbstract{

  id: number;
  name: string;
  type: string;
  minUploadDate: Date;
  maxUploadDate: Date;
  status: string;
  constructor(){
    super();
    }
}
