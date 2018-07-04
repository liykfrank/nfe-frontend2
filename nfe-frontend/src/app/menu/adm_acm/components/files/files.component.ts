import { Component, OnInit } from '@angular/core';
import { FilesService } from './../../services/files.service';
import { AttachedFile } from './../../models/attached-file.model';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent implements OnInit {

  files: AttachedFile[];
  filesToUpload: File[];

  constructor(
    private _FilesService: FilesService
  ) {
    this._FilesService.getFiles().subscribe(files => {
      this.files = files;
    });

    this._FilesService.getFilesToUpload().subscribe(files => {
      this.filesToUpload = files;
    });
  }

  ngOnInit() {

  }

  onUploadHandler(event) {
    this._FilesService.setFileToUpload(event.files[0]);
  }

}
