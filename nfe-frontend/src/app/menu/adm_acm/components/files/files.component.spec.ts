import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesComponent } from './files.component';
import { FilesService } from '../../services/files.service';
import { SharedModule } from '../../../../shared/shared.module';
import { Observable } from 'rxjs/Observable';

describe('FilesComponent', () => {
  let component: FilesComponent;
  let fixture: ComponentFixture<FilesComponent>;

  const _FilesService = jasmine.createSpyObj<FilesService>('FilesService',
    ['getFiles', 'getFilesToUpload', 'setFileToUpload']);

  _FilesService.getFiles.and.returnValue(Observable.of([]));
  _FilesService.getFilesToUpload.and.returnValue(Observable.of([]));

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule ],
      providers: [
        {provide: FilesService, useValue: _FilesService}
      ],
      declarations: [ FilesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onUploadHandler', () => {
    _FilesService.setFileToUpload.and.returnValue(Observable.of({}));
    const event: any = {};
    event.files = [new File([], 'test')];
    component.onUploadHandler(event);
    expect(_FilesService.setFileToUpload.calls.count()).toBe(1);
  });

});
