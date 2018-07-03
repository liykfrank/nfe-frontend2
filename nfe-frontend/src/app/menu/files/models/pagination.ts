import { Pageable, Sort } from "./contract/list-files";

export class Pagination {
  pageable: Pageable;
	last: boolean;
	totalPages: number;
	totalElements: number;
	size: number;
	number: number;
	sort: Sort;
	numberOfElements: number;
  first: boolean;
    constructor(objPagination){
       let obj= Object.assign(this,objPagination);
       delete obj.content;
      }
}
