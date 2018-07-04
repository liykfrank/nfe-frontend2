import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { Acdm } from './../models/acdm.model';
import { AttachedFile } from './../models/attached-file.model';

@Injectable()
export class FilesService {

  private $files = new BehaviorSubject<AttachedFile[]>([]);
  private $files_to_upload = new BehaviorSubject<File[]>([]);

  constructor() { }

  public copyToAdcm(acdm: Acdm) {
    acdm.attachedFiles = this.$files.getValue();
  }

  public getFiles(): Observable<AttachedFile[]> {
    return this.$files.asObservable();
  }

  public setFiles(file: AttachedFile): void {
    const files = this.$files.getValue();
    files.push(file);
    this.$files.next(files);
  }

  public getFilesToUpload(): Observable<File[]> {
    return this.$files_to_upload.asObservable();
  }

  public setFileToUpload(file: File): void {
    const files = this.$files_to_upload.getValue();
    files.push(file);
    this.$files_to_upload.next(files);
  }
}
