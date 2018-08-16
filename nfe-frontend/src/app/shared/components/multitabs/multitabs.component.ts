import { Component, DoCheck, EventEmitter, Injector, Input, OnInit, Output } from '@angular/core';
import { MessageService } from 'primeng/components/common/messageservice';
import { ScreenType } from '../../enums/screen-type.enum';
import { CommentDataServer } from './models/comment-data-server.model';
import { FileDataServer } from './models/file-data-server.model';
import { HistoryModel } from './models/history.model';
import { TicketDocument } from './models/ticket-document.model';
import { CommentCommunicationService } from './services/resources/comment-communication.service';
import { FileCommunicationService } from './services/resources/file-communication.service';

@Component({
  selector: 'bspl-multitabs',
  templateUrl: './multitabs.component.html',
  styleUrls: ['./multitabs.component.scss']
})
export class MultitabsComponent implements OnInit, DoCheck {
  private _modelView: ScreenType;
  private _documents: TicketDocument[] = [];

  @Input() url: string;
  @Input() idPk = -1;

  @Input()
  set modelView(type: ScreenType) {
    if (
      type === ScreenType.DETAIL &&
      this._modelView === ScreenType.CREATE &&
      this.idPk >= 0
    ) {

      this.enable_tab[0] = true; // TODO download history

      this._postComment();
      this._postFiles();
    }

    this._modelView = type;
  }

  @Input() listExcludesOnHistory: string[] = [];

  @Input() disabledDocuments: boolean = false;
  @Input() documents: TicketDocument[] = [];

  @Output() removeTicket = new EventEmitter();
  @Output() showTicket: EventEmitter<boolean> = new EventEmitter<boolean>();

  tab_selected = 0;
  badge_val_tab = [0, 0, 0, 0, 0];
  enable_tab = [true, true, true, true, false];

  history: HistoryModel[] = [];

  display = false;

  files: FileDataServer[] = [];
  filesToUpload: File[] = [];

  comments: CommentDataServer[] = [];
  commentToUpload: CommentDataServer;
  disabledComment = false;

  constructor(
    private _CommentCommunicationService: CommentCommunicationService,
    private _FileCommunicationService: FileCommunicationService,
    private _MessageService: MessageService,
    private _Injector: Injector
  ) {
    this._documents = this.documents;
  }

  ngOnInit() {
    if (this._modelView === ScreenType.CREATE) {
      this.enable_tab[0] = false;
      this.tab_selected = 1;
    } else {
      console.log('load all info here');
    }

    if (this.disabledDocuments) {
      this.enable_tab[1] = false;
      this.tab_selected = 2;
    }
  }

  ngDoCheck() {
    if (this._documents.length != this.documents.length && this.tab_selected != 1) {
      this.badge_val_tab[1] = this._documents.length - this.documents.length;
      this._documents = this.documents;
    } else {
      this.badge_val_tab[1] = 0;
    }
  }

  colorTab(index: string): string {
    return this.enable_tab[index] ? 'blue' : 'grey';
  }

  selectTab(index: number) {
    this.tab_selected = this.enable_tab[index] ? index : this.tab_selected;
  }

  isSelected(index: number) {

    if (this.tab_selected === index) {
      this.badge_val_tab[index] = 0;
    }

    return this.tab_selected === index;
  }

  status(index: number): string {
    let aux = this.isSelected(index) ? 'active' : '';
    aux = !this.enable_tab[index] ? 'disabled' : aux;
    return aux;
  }

  // Documents component
  onRemoveTicket(data) {
    this.removeTicket.emit(data);
  }

  onShowTicket(ticket: TicketDocument) { // TODO el ticket se le pasará al pop-up en un futuro
    this.showTicket.emit(true);
    this.display = true;
  }

  onHideTicket() {
    this.showTicket.emit(false);
    this.display = false;
  }

  // Files component
  onPushFiles(data) {
    this.filesToUpload.push(data);

    if (this._modelView === ScreenType.DETAIL) {
      this._postFiles();
    }
  }

  // Comments component
  onSaveComment(event) {
    const aux: CommentDataServer = new CommentDataServer();
    aux.text = event;

    this.commentToUpload = aux;
    this.disabledComment = true;

    if (this._modelView === ScreenType.DETAIL) {
      this._postComment();
    }
  }

  // TODO Gets from server

  // Utils
  private _postFiles() {
    if (this.filesToUpload.length > 0) {
      const formData: FormData = new FormData();

      for (const file of this.filesToUpload) {
        formData.append('file', file, file.name);
      }

      this._FileCommunicationService.configureUrl(this.url);
      this._FileCommunicationService.configureUrl(
        this._FileCommunicationService.getUrl([this.idPk.toString(), 'files'])
      );

      this._FileCommunicationService
        .postSingle<any[]>(formData)
        .finally(() => {
          this._FileCommunicationService.configureUrl(this.url);
          this.filesToUpload = [];
        })
        .subscribe(
          data => {
            this._pushFiles(data);
          },
          err => {
            this._pushFiles(err);
          }
        );
    }
  }

  private _pushFiles(elems: any[]) {
    for (const aux of elems) {
      if (aux.status == 200) {
        const file = new FileDataServer()
        file.id = aux.id;
        file.name = aux.subject;
        file.path = aux.path;

        this.files.push(file);
      } else {
        this._MessageService.add({ severity: 'error', summary: aux.subject});
      }
    }
  }

  private _postComment() {
    if (this.commentToUpload) {
      this._CommentCommunicationService.configureUrl(this.url);
      this._CommentCommunicationService.configureUrl(
        this._CommentCommunicationService.getUrl([
          this.idPk.toString(),
          'comments'
        ])
      );

      this._CommentCommunicationService
        .postSingle<CommentDataServer>(this.commentToUpload)
        .finally(() => {
          this._CommentCommunicationService.configureUrl(this.url);
          this.commentToUpload = null;
          this.disabledComment = false;
        })
        .subscribe(
          data => {
            this.comments.push(data);
          },
          err => {
            this._MessageService.add({
              // severity: 'error',
              // summary: this.translation.translate(
              //   'CommentCommunicationService.tittle'
              // ),
              // // detail: this.translation.translate(
              // //   'CommentCommunicationService.err'
              // // )
            });
          }
        );
    }
  }
}
