import { Content } from "./contract/list-files";

export class FileNw {
  id: number;
	name: string;
	type: string;
	bytes: number;
	uploadDate: Date;
  status: string;

  constructor(content?: Content){
    if(content){
      let obj= Object.assign(this,content);
      delete obj.uploadDate;
      this.uploadDate= new Date(content.uploadDateTime);
    }
  }

}
