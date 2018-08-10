import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTES } from '../../shared/constants/routes';
import { QueryFilesComponent } from './components/query-files/query-files.component';
import { UploadFilesComponent } from './components/upload-files/upload-files.component';


const routes: Routes = [
   {
    path: ROUTES.QUERY_FILES.PATH,
    component: QueryFilesComponent ,
    data: { tab: ROUTES.QUERY_FILES }
  },
  {
    path: ROUTES.QUERY_FILES_READ_ONLY.PATH,
    component: QueryFilesComponent,
    data: { tab: ROUTES.QUERY_FILES_READ_ONLY }
  },
  {
    path: ROUTES.UPLOAD_FILES.PATH,
    component: UploadFilesComponent,
    data: { tab: ROUTES.UPLOAD_FILES }
  },
 ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FilesRoutingModule { }
