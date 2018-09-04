import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FileDataServer } from '../../models/file-data-server.model';

@Component({
  selector: 'bspl-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent {

  @Input() files: FileDataServer[];
  @Input() filesToUpload: File[];

  @Output() pushFiles = new EventEmitter();

  constructor() {
  }

  onUploadHandler(event) {
    this.pushFiles.emit(event.files[0]);
  }

}
