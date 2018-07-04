import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TabsFileComponent } from './components/tabs-file/tabs-file.component';
import { FilesUploadComponent } from './components/files-upload/files-upload.component';

const routes: Routes = [
  /* {
    path: 'files',
    component: FilesListComponent
  }, */
  {
    path: 'tabs',
    component: TabsFileComponent
  },
  {
    path: 'upload',
    component: FilesUploadComponent
  },
 ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: []
})
export class FilesRoutingModule { }
