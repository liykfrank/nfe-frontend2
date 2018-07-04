import { Observable } from 'rxjs/Rx';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentsComponent } from './comments.component';
import { SharedModule } from '../../../../shared/shared.module';
import { AdmAcmService } from '../../services/adm-acm.service';
import { AcdmsService } from '../../services/resources/acdms.service';
import { CommentsService } from '../../services/comments.service';

describe('CommentsComponent', () => {
  let component: CommentsComponent;
  let fixture: ComponentFixture<CommentsComponent>;

  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService', ['getScreenType']);
  const _CommentsService = jasmine.createSpyObj<CommentsService>('CommentsService', ['getComments', 'setCommentToUpload']);

  _AdmAcmService.getScreenType.and.returnValue(Observable.of({}));
  _CommentsService.getComments.and.returnValue(Observable.of([]));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule ],
      providers: [
        {provide: AdmAcmService, useValue: _AdmAcmService},
        {provide: CommentsService, useValue: _CommentsService}
      ],
      declarations: [ CommentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('saveComment', () => {
    _CommentsService.setCommentToUpload.calls.reset();
    _CommentsService.setCommentToUpload.and.returnValue(Observable.of({}));

    component.texto = 'AAA';
    component.saveComment();
    expect(_CommentsService.setCommentToUpload.calls.count()).toBe(1);
    expect(component.texto == null).toBe(true);
  });

});
