
import { DownFilesResource } from './services/resources/downfiles.resource';
import { FilesRoutingModule } from './files-routing.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilesFilterComponent } from './components/files-filter/files-filter.component';
import { ListFilesResource } from './services/resources/listfiles.resopurce';
import { ListFilesService } from './services/list-files.service';
import { DownFileResource } from './services/resources/downfile.resource';
import { RemoveFilesResource } from './services/resources/removefiles.resource';
import { RemoveFileResource } from './services/resources/removefile.resource';
import { UtilsService } from '../../shared/services/utils.service';
import { ConfigurationService } from '../files/services/configuration.service';
import { FormsModule } from '@angular/forms';
import { QueryFilesComponent } from './components/query-files/query-files.component';
import { UploadFilesComponent } from './components/upload-files/upload-files.component';
import { TablePaginationComponent } from '../../shared/components/table-pagination/table-pagination.component';
import { FilterCrumbsComponent } from '../../shared/components/filter-crumbs/filter-crumbs.component';
import { jqxButtonComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxbuttons';
import { jqxDropDownListComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdropdownlist';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';
import { GrowlModule } from 'primeng/growl';
import { FileUploadModule } from 'primeng/fileupload';
import { jqxInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxinput';
import { jqxDateTimeInputComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxdatetimeinput';
import { JqxNwComboComponent } from '../../shared/components/jqx-nw-combo/jqx-nw-combo.component';
import { DropdownModule } from 'primeng/dropdown';
import { TranslationModule } from 'angular-l10n';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    FilesRoutingModule,
    GrowlModule,
    FileUploadModule,
    DropdownModule,
    TranslationModule
  ],
  declarations: [
    QueryFilesComponent,
    UploadFilesComponent,
    FilesFilterComponent,
    TablePaginationComponent,
    FilterCrumbsComponent,
    jqxGridComponent,
    jqxButtonComponent,
    jqxDropDownListComponent,
    jqxInputComponent,
    jqxDateTimeInputComponent,
    JqxNwComboComponent,
  ],
  entryComponents: [
    QueryFilesComponent,
    UploadFilesComponent,
  ],
  providers: [
    ListFilesResource,
    DownFilesResource,
    DownFileResource,
    RemoveFilesResource,
    RemoveFileResource,
    ListFilesService,
    ConfigurationService,
    UtilsService
  ]
})
export class FilesModule {
 }
