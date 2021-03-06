import { DownFileResource } from "./resources/downfile.resource";
import { HttpParams } from "@angular/common/http";
import { FileNw } from "./../models/file";
import { ListData } from "./../models/list-data";
import { Observable } from "rxjs/Observable";
import { ListFilesFilter } from "./../models/list-files-filter";
import { ListFilesResource } from "./resources/listfiles.resopurce";
import { Injectable, Injector } from "@angular/core";
import { NwBaseAbstract } from "../../shared/base/nw-base-abstract";
import { Pagination } from "../models/pagination";
import { UtilsService } from "../../shared/services/utils.service";
import { RemoveFilesResource } from "./resources/removefiles.resource";
import { RemoveFileResource } from "./resources/removefile.resource";
import { ParamsEnum } from "../models/params.enum";
import { DownFilesResource } from "./resources/downfiles.resource";
import { SortType } from "../models/sort-type.enum";

@Injectable()
export class ListFilesService extends NwBaseAbstract {
  constructor(
    injector: Injector,
    private listFilesRes: ListFilesResource,
    private downFileRes: DownFileResource,
    private downFilesRes: DownFilesResource,
    private remFilesRes: RemoveFilesResource,
    private remFileRes: RemoveFileResource,
    private utils: UtilsService
  ) {
    super(injector);
  }

  listFiles(filter: ListFilesFilter) {
    return this.listFilesRes.get();
  }

  listFilesData(filter: ListFilesFilter): Observable<ListData<FileNw>> {
    const params = this.convertFilterToParams(filter);
    return this.listFilesRes.get(params).map(data => {
      return new ListData(
        data.content.map(contFile => new FileNw(contFile)),
        new Pagination(data)
      );
    });
  }

  downloadFile(file: FileNw) {
    let params = new HttpParams();
    //params = params.append(ParamsEnum.ID, file.id.toString());
    return this.downFileRes.getFile(params, file.id.toString());
  }

  downloadFiles(files: FileNw[]) {
    const params = this.convertFilesToParams(files);
    return this.downFilesRes.getFile(params);
  }

  removeFile(file: FileNw) {
    let params = new HttpParams();
    //params = params.append(ParamsEnum.ID, file.id.toString());
    return this.remFileRes.deleteFile(file.id.toString());
  }

  removeFiles(files: FileNw[]) {
    const params = this.convertFilesToParams(files);
    return this.remFilesRes.delete(params);
  }

  getStatusCodes(): Array<string> {
    return ["ALL STATUS", "DELETED", "DOWNLOADED", "UNREAD"];
  }
  convertFilterToParams(filter: ListFilesFilter): HttpParams {
    let params = new HttpParams();

    if (filter.maxUploadDate) {
      filter.maxUploadDate.setDate(filter.maxUploadDate.getDate() + 1);
    }

    this.utils.execFn(
      filter.id,
      () => (params = params.append(ParamsEnum.ID, filter.id.toString()))
    );
    this.utils.execFn(
      filter.maxUploadDate,
      () =>
        (params = params.append(
          ParamsEnum.MAX_UPLOAD_DATE,
          filter.maxUploadDate.toISOString()
        ))
    );
    this.utils.execFn(
      filter.minUploadDate,
      () =>
        (params = params.append(
          ParamsEnum.MIN_UPLOAD_DATE,
          filter.minUploadDate.toISOString()
        ))
    );
    this.utils.execFn(
      filter.name,
      () => (params = params.append(ParamsEnum.NAME, filter.name))
    );
    this.utils.execFn(
      filter.numberPage,
      () =>
        (params = params.append(ParamsEnum.PAGE, filter.numberPage.toString()))
    );
    this.utils.execFn(
      filter.sizePage,
      () =>
        (params = params.append(ParamsEnum.SIZE, filter.sizePage.toString()))
    );
    this.utils.execFn(filter.sort, () => {
      let sortStr = '';
      filter.sort.forEach(sortField => {
        if (sortField.type == SortType.ASC) {
          sortStr += sortField.name + ',asc';
        } else {
          sortStr += sortField.name + ',desc';
        }
      });
      params = params.append(ParamsEnum.SORT, sortStr);
    });
    this.utils.execFn(
      filter.type,
      () => (params = params.append(ParamsEnum.TYPE, filter.type))
    );
    this.utils.execFn(
      filter.status && filter.status != "ALL STATUS",
      () => (params = params.append(ParamsEnum.STATUS, filter.status))
    );
    return params;
  }

  convertFilesToParams(files: FileNw[]): HttpParams {
    let params = new HttpParams();
    files.forEach(function(file) {
      params = params.append(ParamsEnum.ID, file.id.toString());
    });

    return params;
  }
}
