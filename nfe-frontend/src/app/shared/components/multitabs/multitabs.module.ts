import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslationModule } from 'angular-l10n';
import { FileUploadModule } from 'primeng/fileupload';
import { DialogModule } from 'primeng/primeng';
import { ScrollPanelModule } from 'primeng/scrollpanel';

import { CommentsComponent } from './components/comments/comments.component';
import { DocumentPopUpComponent } from './components/document-pop-up/document-pop-up.component';
import { DocumentsComponent } from './components/documents/documents.component';
import { FilesComponent } from './components/files/files.component';
import { HistoryComponent } from './components/history/history.component';
import { MultitabsComponent } from './multitabs.component';
import { CommentCommunicationService } from './services/resources/comment-communication.service';
import { FileCommunicationService } from './services/resources/file-communication.service';

@NgModule({
  imports: [
    CommonModule,
    ScrollPanelModule,
    FileUploadModule,
    DialogModule,
    FormsModule,
    TranslationModule
  ],
  providers: [
    CommentCommunicationService,
    FileCommunicationService
   ],
  declarations: [
    HistoryComponent,
    DocumentsComponent,
    FilesComponent,
    CommentsComponent,
    MultitabsComponent,
    DocumentPopUpComponent
  ],
  exports: [
    MultitabsComponent
  ]
})
export class MultitabsModule { }
