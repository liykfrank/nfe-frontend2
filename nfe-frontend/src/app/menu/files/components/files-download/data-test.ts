import { FileNw } from "../../models/file";
import { Pagination } from "../../models/pagination";

export class DataTest {
  fileList:FileNw[];
  statusCodes:string[];
  pagination: Pagination;
  constructor(){
    this.statusCodes=['UNREAD','DELETED','DOWNLOADED'];
    this.fileList=[new FileNw({id: 1,
      name: 'file1',
      type: 'type1',
      bytes: 100,
      uploadDateTime: '',
      status: 'UNREAD'}),
      new FileNw({id: 2,
        name: 'file2',
        type: 'type1',
        bytes: 100,
        uploadDateTime: '',
        status: 'UNREAD'}),
      new FileNw({id: 3,
        name: 'file3',
        type: 'type1',
        bytes: 100,
        uploadDateTime: '',
        status: 'UNREAD'}) ];
        this.pagination= new Pagination({});
        this.pagination.totalPages= 2;
  }

}
