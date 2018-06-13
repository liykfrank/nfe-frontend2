import { forEach } from '@angular/router/src/utils/collection';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { ConfigurationService } from '../../services/configuration.service';
import { Configuration } from '../../models/configuration';
import { environment } from '../../../../../environments/environment';
import { Message } from 'primeng/components/common/api';
import { any } from 'codelyzer/util/function';

const API_URL = environment.files.api.apiUpload;
const BASE_URL = environment.files.basePath;

@Component({
  selector: 'app-files-upload',
  templateUrl: './files-upload.component.html',
  styleUrls: ['./files-upload.component.scss']
})

export class FilesUploadComponent implements OnInit, AfterViewInit {
  @ViewChild('uploadComp') myFileUpload: any;
  configuration: Configuration;
  url: string;
  multipleUpload: boolean;
  name: string;
  chooseLabel: string;
  customUpload: boolean;
  uploadedFiles: any[];

  // TODO: revisar este sistema de mensajes Toast y acondicionarlo al actual.
  msgs: Message[];

  constructor(private configurationService: ConfigurationService) {
  }

  ngOnInit() {
    this.url = BASE_URL + API_URL;
    this.multipleUpload = true;
    this.name = 'file';
    this.chooseLabel = 'Select Files';
    this.customUpload = false;
    this.uploadedFiles = [];
    this.msgs = [];
    this.getconfiguration();
  }


  ngAfterViewInit(): void { }

  private getconfiguration(): void {
    this.configurationService.get().subscribe(
      data => { this.configuration = data; },
      err => console.error(err),
      () => console.log('done loading configuration' + this.configuration.maxUploadFilesNumber)
    );
    console.log('configuration ' + this.configuration);
  }

  onBeforeUpload(event) {
    this.myFileUpload.customUpload = true;
  }

  onUpload(event): void {
    this.myFileUpload.customUpload = false;
    this.uploadedFiles = event.files;
  }

  onSelect(event): void {
    this.checkErrors(event.files);
  }

  getFileIndexInArray(file, array): number {
    return array.map(function (f) {
      return f.name + f.type + f.size;
    }).indexOf(file.name + file.type + file.size);
  }

  isValidFileExtension(file: any): boolean {
    const filename: string = file.name;
    const allowedExtensions: string[] = this.configuration.allowedFileExtensions;
    const fileExtension: string = filename.slice((Math.max(0, filename.lastIndexOf('.')) || Infinity) + 1);
    const validExtension: boolean = this.configuration.allowedFileExtensions.some(function (element, index, array) {
      return element == fileExtension;
    });
    let errSummary: string;
    let errDetail: string;


    if (!validExtension) {
      errSummary = `${file.name}: Invalid file extension,`;
      // errDetail = `allowed file extensions: '${allowedExtensions.join('\', \'').toString()}'`;
      errDetail = '';
      this.msgs.push({ severity: 'error', summary: errSummary, detail: errDetail });
    }

    return validExtension;
  }

  isValidNumFiles(): boolean {
    const errMsg = 'Maximum number of allowable file uploads has been exceeded';
    let isValidNumFiles = true;

    if (this.myFileUpload.files.length > this.configuration.maxUploadFilesNumber) {
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
      errSummary = `${file.name}: Invalid file size,`;
      errDetail = `maximum upload size is ${file.size}`;
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
      errSummary = `${file.name}: Invalid file name,`;
      errDetail = `the file should have a valid name`;
      this.msgs.push({ severity: 'error', summary: errSummary, detail: errDetail });
    }

    return validName;
  }

  checkErrors(selectedFiles: any): void {
    let isValidNumFiles, isValidFileSize, isValidFileExtension, isValidFileName: boolean;
    this.msgs = [];

    for (const file of selectedFiles) {
      isValidNumFiles = this.isValidNumFiles();
      isValidFileName = this.isValidFileName(file);
      isValidFileExtension = this.isValidFileExtension(file);
      isValidFileSize = this.isValidFileSize(file);

      if (!isValidFileName || !isValidFileExtension || !isValidFileSize) {
        this.removeSelectedFile(file);
      }
    }

    for (const file of this.myFileUpload.files) {
      isValidNumFiles = this.isValidNumFiles();
      if (!isValidNumFiles) {
        this.removeSelectedFile(file);
      }
    }

  }

  private removeSelectedFile(file: any): void {
    const index = this.getFileIndexInArray(file, this.myFileUpload.files);

    if (index > -1) {
      this.myFileUpload.files.splice(index, 1);
    }
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

}
