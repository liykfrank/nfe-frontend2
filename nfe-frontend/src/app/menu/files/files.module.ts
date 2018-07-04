import { environment } from '../../../environments/environment';
import { DownFilesResource } from './services/resources/downfiles.resource';
import { FilesRoutingModule } from './files-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TabsFileComponent } from './components/tabs-file/tabs-file.component';
import { FilesUploadComponent } from './components/files-upload/files-upload.component';
import { SharedModule } from '../../shared/shared.module';
import { FilesDownloadComponent } from './components/files-download/files-download.component';
import { FilesFilterComponent } from './components/files-filter/files-filter.component';
import { ListFilesResource } from './services/resources/listfiles.resopurce';
import { ListFilesService } from './services/list-files.service';
import { DownFileResource } from './services/resources/downfile.resource';
import { RemoveFilesResource } from './services/resources/removefiles.resource';
import { RemoveFileResource } from './services/resources/removefile.resource';
import { UtilsService } from '../../shared/services/utils.service';
import { ConfigurationService } from '../files/services/configuration.service';
import { FormsModule } from '@angular/forms';
import { ListFilesMockResource } from './services/resources/mocks/listfiles.resopurce';
import { DownFileMockResource } from './services/resources/mocks/downfile.resource';

const utils=new UtilsService();
utils.env=environment;
@NgModule({
  imports: [CommonModule, FormsModule, SharedModule, FilesRoutingModule],
  declarations: [
    TabsFileComponent,
    FilesUploadComponent,
    FilesDownloadComponent,
    FilesFilterComponent
  ],
  entryComponents: [
    FilesUploadComponent,
    FilesDownloadComponent,
    FilesFilterComponent
  ],
  providers: [
    utils.getProv(ListFilesResource,ListFilesMockResource),
    utils.getProv(DownFileResource,DownFileMockResource),
    DownFilesResource,
    RemoveFileResource,
    RemoveFilesResource,
    ListFilesService,
    ConfigurationService
  ]
})
export class FilesModule {}
