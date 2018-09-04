import { Component, OnInit } from '@angular/core';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../models/configuration';

import { Message } from 'primeng/components/common/api';
import { FilesService } from '../../services/files.service';

@Component({
  selector: 'bspl-upload-files',
  templateUrl: './upload-files.component.html',
  styleUrls: ['./upload-files.component.scss']
})

export class UploadFilesComponent implements OnInit {
  configuration: Configuration;
  multipleUpload: boolean;
  chooseLabel: string;
  customUpload: boolean;
  files: any[];
  uploadedFiles: any[];

  // TODO: revisar este sistema de mensajes Toast y acondicionarlo al actual.
  msgs: Message[];

  MAX_LENGHT = 40;

  constructor(
    private configurationService: ConfigurationService,
    private _FilesService: FilesService
  ) {  }

  ngOnInit() {
    this.multipleUpload = true;
    this.chooseLabel = 'Select Files';
    this.customUpload = false;
    this.uploadedFiles = [];
    this.files = [];
    this.msgs = [];
    this.getConfiguration();
  }

  private getConfiguration(): void {
    this.configurationService.get().subscribe(
      data => { this.configuration = data; },
      err => console.error(err),
      () => console.log('done loading configuration' + this.configuration.maxUploadFilesNumber)
    );
  }

  sendUpload() {
    if (this.files && this.files.length > 0) {

      this._FilesService.uploadFiles(this.files)
        .finally(() => {
          this.files = [];
        })
        .subscribe(
          data => {
            this.uploadedFiles = data;
          },
          err => { }
        );
    }
  }

  cancelUpdate() {
    this.files = [];
  }

  onSelect(event): void {
    this.uploadedFiles = [];
    this.checkErrors(event.files);
  }

  getFileIndexInArray(file, array): number {
    return array.map(function (f) {
      return f.name + f.type + f.size;
    }).indexOf(file.name + file.type + file.size);
  }

  isValidFileExtension(file: any): boolean {
    const filename: string = file.name;
    const fileExtension: string = filename.slice((Math.max(0, filename.lastIndexOf('.')) || Infinity) + 1);
    const validExtension: boolean = this.configuration.allowedFileExtensions.
        some( elem => elem == fileExtension);
    let errSummary: string;
    let errDetail: string;


    if (!validExtension) {
      errSummary = `${file.name}: Invalid file extension.`;
      // errDetail = `allowed file extensions: '${allowedExtensions.join('\', \'').toString()}'`;
      errDetail = '';
      this.msgs.push({ severity: 'error', summary: errSummary, detail: errDetail });
    }

    return validExtension;
  }

  isValidNumFiles(): boolean {
    const errMsg = 'Maximum number of allowable file uploads has been exceeded.';
    let isValidNumFiles = true;

    if (this.files.length > this.configuration.maxUploadFilesNumber) {
      isValidNumFiles = false;
      this.msgs.push({ severity: 'error', summary: errMsg, detail: '' });
    }

    return isValidNumFiles;
  }

  isValidFileSize(file: any): boolean {
    const maxUploadFileSize: number = this.convertIntoBytes(this.configuration.maxUploadFileSize);
    let validSize = true;
    let errSummary: string;
    let errDetail: string;

    if (file.size > maxUploadFileSize) {
      validSize = false;
      errSummary = `${file.name}: Invalid file size.`;
      errDetail = `Maximum upload size is ${file.size}`;
      this.msgs.push({ severity: 'error', summary: errSummary, detail: errDetail });
    }

    return validSize;
  }

  isValidFileName(file: any): boolean {
    const filenamePatterns: string[] = this.configuration.fileNamePatterns;
    const validName: boolean = filenamePatterns.some(function (regExStr, index, array) {
      const regEx: RegExp = new RegExp(regExStr);
      return regEx.test(file.name);
    });
    let errSummary: string;
    let errDetail: string;


    if (!validName) {
      errSummary = `${file.name}: Invalid file name.`;
      errDetail = `The file should have a valid name.`;
      this.msgs.push({ severity: 'error', summary: errSummary, detail: errDetail });
    }

    return validName;
  }

  checkErrors(selectedFiles: any[]): void {
    let isValidNumFiles,
        isValidFileSize,
        isValidFileExtension,
        isValidFileName: boolean;
    // this.msgs = [];

    for (let index = 0; index < selectedFiles.length; index++) {
      isValidNumFiles = this.isValidNumFiles();
      isValidFileName = this.isValidFileName(selectedFiles[index]);
      isValidFileExtension = this.isValidFileExtension(selectedFiles[index]);
      isValidFileSize = this.isValidFileSize(selectedFiles[index]);

      if (isValidNumFiles && isValidFileName && isValidFileExtension && isValidFileSize) {
        this.files.push(selectedFiles[index]);
      }
    }

    for (const file of this.files) {
      isValidNumFiles = this.isValidNumFiles();
      if (!isValidNumFiles) {
        this.removeSelectedFile(file);
      }
    }
  }

  private removeSelectedFile(file: any): void {
    const index = this.getFileIndexInArray(file, this.files);

    if (index > -1) {
      this.files.splice(index, 1);
    }
  }

  removeMessage(message: any): void {
    const index = this.msgs.indexOf(message);

    if (index > -1) {
      this.msgs.splice(index, 1);
    }
  }

  removeAllMessages() {
    this.msgs = [];
  }

  private convertIntoBytes(fileSize: string): number {
    const unit = fileSize.substr(fileSize.length - 2, fileSize.length);
    let fileSizeNumb = parseInt(fileSize.substr(0, fileSize.length - 2), 10);

    if (unit == 'KB') {
      fileSizeNumb = fileSizeNumb * 1024;
    } else {
      fileSizeNumb = fileSizeNumb * 1024 * 1024;
    }

    return fileSizeNumb;
  }

  checkMessagesLength() {
    return this.msgs.length == 0;
  }

  checkFilesLength() {
    return this.files.length == 0;
  }
}
