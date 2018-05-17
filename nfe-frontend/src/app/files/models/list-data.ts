import { Pagination } from "./pagination";

export class ListData<T> {

  constructor(public listData: Array<T>,
   public pagination: Pagination
  ){

  }
}
