import { DownFilesResource } from './services/resources/downfiles.resource';
import { FilesRoutingModule } from './files-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilesListComponent } from './components/files-list/files-list.component';
import { TabsFileComponent } from './components/tabs-file/tabs-file.component';
import { FilesUploadComponent } from './components/files-upload/files-upload.component';
import { SharedModule } from '../shared/shared.module';
import { FilesDownloadComponent } from './components/files-download/files-download.component';
import { FilesFilterComponent } from './components/files-filter/files-filter.component';
import { ListFilesResource } from './services/resources/listfiles.resopurce';
import { ListFilesService } from './services/list-files.service';
import { DownFileResource } from './services/resources/downfile.resource';
import { RemoveFilesResource } from './services/resources/removefiles.resource';
import { RemoveFileResource } from './services/resources/removefile.resource';
import { UtilsService } from '../shared/services/utils.service';
import { ConfigurationService } from '../files/services/configuration.service';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [CommonModule, FormsModule, SharedModule, FilesRoutingModule],
  declarations: [
    FilesListComponent,
    TabsFileComponent,
    FilesUploadComponent,
    FilesDownloadComponent,
    FilesFilterComponent
  ],
  entryComponents: [
    FilesListComponent,
    FilesUploadComponent,
    FilesDownloadComponent,
    FilesFilterComponent
  ],
  providers: [
    ListFilesResource,
    DownFileResource,
    DownFilesResource,
    RemoveFileResource,
    RemoveFilesResource,
    ListFilesService,
    ConfigurationService
  ]
})
export class FilesModule {}
