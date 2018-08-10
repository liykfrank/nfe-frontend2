import { HttpClientModule } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslationModule } from 'angular-l10n';
import { MessageService } from 'primeng/components/common/messageservice';
import {
  DialogModule,
  FileUploadModule,
  ScrollPanelModule
} from 'primeng/primeng';
import { Observable } from 'rxjs/Observable';

import { l10nConfig } from '../../base/conf/l10n.config';
import { ScreenType } from '../../enums/screen-type.enum';
import { CommentsComponent } from './components/comments/comments.component';
import { DocumentPopUpComponent } from './components/document-pop-up/document-pop-up.component';
import { DocumentsComponent } from './components/documents/documents.component';
import { FilesComponent } from './components/files/files.component';
import { HistoryComponent } from './components/history/history.component';
import { TicketDocument } from './models/ticket-document.model';
import { MultitabsComponent } from './multitabs.component';
import { CommentCommunicationService } from './services/resources/comment-communication.service';
import { FileCommunicationService } from './services/resources/file-communication.service';

describe('MultitabsComponent Create', () => {
  let component: MultitabsComponent;
  let fixture: ComponentFixture<MultitabsComponent>;

  const _FileCommunicationService = jasmine.createSpyObj<
    FileCommunicationService
  >('FileCommunicationService', ['postSingle', 'configureUrl', 'getUrl']);
  _FileCommunicationService.configureUrl.and.returnValue(Observable.of({}));
  _FileCommunicationService.getUrl.and.returnValue(Observable.of({}));

  const _CommentCommunicationService = jasmine.createSpyObj<
    CommentCommunicationService
  >('CommentCommunicationService', ['postSingle', 'configureUrl', 'getUrl']);
  _CommentCommunicationService.configureUrl.and.returnValue(Observable.of({}));
  _CommentCommunicationService.getUrl.and.returnValue(Observable.of({}));

  const _MessageService = jasmine.createSpyObj<MessageService>(
    'MessageService',
    ['add']
  );
  _MessageService.add.and.returnValue(Observable.of({}));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ScrollPanelModule,
        FileUploadModule,
        DialogModule,
        FormsModule,
        TranslationModule.forRoot(l10nConfig),
        HttpClientModule,
        BrowserAnimationsModule
      ],
      providers: [
        {
          provide: CommentCommunicationService,
          useValue: _CommentCommunicationService
        },
        {
          provide: FileCommunicationService,
          useValue: _FileCommunicationService
        },
        { provide: MessageService, useValue: _MessageService }
      ],
      declarations: [
        CommentsComponent,
        DocumentPopUpComponent,
        DocumentsComponent,
        FilesComponent,
        HistoryComponent,
        MultitabsComponent
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultitabsComponent);
    component = fixture.componentInstance;
    component.modelView = ScreenType.CREATE;
    component.url = 'TEST';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('selectTab', () => {
    component.selectTab(3);
    expect(component.tab_selected).toBe(3);
    component.selectTab(0);
    expect(component.tab_selected).toBe(3);
  });

  it('onRemoveTicket', () => {
    let aux;
    component.removeTicket.subscribe(data => (aux = data));
    component.onRemoveTicket('TEST');
    expect(aux).toBe('TEST');
  });

  it('onShowTicket', () => {
    let aux;
    component.showTicket.subscribe(data => (aux = data));

    const ticket: TicketDocument = {
      relatedTicketDocumentNumber: '11111111111',
      checkDigit: 1
    };
    component.onShowTicket(ticket);
    expect(aux).toBe(true);
    expect(component.display).toBe(true);
  });

  it('onHideTicket', () => {
    let aux;
    component.showTicket.subscribe(data => (aux = data));

    component.onHideTicket();
    expect(aux).toBe(false);
    expect(component.display).toBe(false);
  });

  it('onPushFiles 1', () => {
    _FileCommunicationService.postSingle.calls.reset();
    _FileCommunicationService.postSingle.and.returnValue(Observable.of({}));

    const file1: any = { name: 'abc1.xyz', size: 1025 };

    expect(component.filesToUpload.length).toBe(0);
    component.onPushFiles([file1]);
    expect(component.filesToUpload.length).toBe(1);
    expect(_FileCommunicationService.postSingle.calls.count()).toBe(0);
  });

  it('onPushFiles 2', () => {
    _FileCommunicationService.postSingle.calls.reset();
    _FileCommunicationService.postSingle.and.returnValue(Observable.of({}));
    component.idPk = 0;
    component.modelView = ScreenType.DETAIL;

    const file1: any = { name: 'abc1.xyz', size: 1025 };
    component.onPushFiles([file1]);
    expect(_FileCommunicationService.postSingle.calls.count()).toBe(1);
  });

  it('onSaveComment 1', () => {
    _CommentCommunicationService.postSingle.calls.reset();
    _CommentCommunicationService.postSingle.and.returnValue(Observable.of({}));

    expect(component.commentToUpload).toBeUndefined();
    component.onSaveComment('TEST');
    expect(component.commentToUpload.text).toBe('TEST');
    expect(_CommentCommunicationService.postSingle.calls.count()).toBe(0);
  });

  it('onSaveComment 2', () => {
    _CommentCommunicationService.postSingle.calls.reset();
    _CommentCommunicationService.postSingle.and.returnValue(Observable.of({}));
    component.idPk = 0;
    component.modelView = ScreenType.DETAIL;

    expect(component.commentToUpload).toBeUndefined();
    component.onSaveComment('TEST');
    expect(_CommentCommunicationService.postSingle.calls.count()).toBe(1);
  });

});

describe('MultitabsComponent Detail', () => {
  let component: MultitabsComponent;
  let fixture: ComponentFixture<MultitabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        ScrollPanelModule,
        FileUploadModule,
        DialogModule,
        FormsModule,
        TranslationModule.forRoot(l10nConfig),
        HttpClientModule,
        BrowserAnimationsModule
      ],
      providers: [
        CommentCommunicationService,
        FileCommunicationService,
        MessageService
      ],
      declarations: [
        CommentsComponent,
        DocumentPopUpComponent,
        DocumentsComponent,
        FilesComponent,
        HistoryComponent,
        MultitabsComponent
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MultitabsComponent);
    component = fixture.componentInstance;
    component.modelView = ScreenType.DETAIL;
    component.url = 'TEST';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
