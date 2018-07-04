import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { FilesService } from './../files.service';
import { AttachedFile } from '../../models/attached-file.model';

import { FileStatusServer } from './../../models/file-status-server.model';

@Injectable()
export class FileService extends NwRepositoryAbstract<any, Object> {

  private $messagesFromUploadFiles = new BehaviorSubject<FileStatusServer[]>([]);
  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.acdm;

  constructor(
    private _FilesService: FilesService,
    private http: HttpClient,
    injector: Injector) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
      environment.adm_acm.api.acdm,
      injector
    );
  }

  public postFile(id: number, fileItem: File[]): void {
    const formData: FormData = new FormData();

    for (let file of fileItem) {
      formData.append('file', file, file.name);
    }

    this.configureUrl(this.getUrl([id.toString(), 'files']));

    this.postSingle<FileStatusServer[]>(formData)
      .finally(() => this.configureUrl(this.base))
      .subscribe(data => {
        this.$messagesFromUploadFiles.next(data);
      });
  }

  public getFiles_ID(id: number): void {
    this.configureUrl(this.getUrl([id.toString()]));

    this.getSingle<AttachedFile>()
      .finally(() => this.configureUrl(this.base))
      .subscribe(resp => {
        this._FilesService.setFiles(resp);
      });
  }

  public getMessagesFromUploadFiles(): Observable<FileStatusServer[]> {
    return this.$messagesFromUploadFiles.asObservable();
  }

}
