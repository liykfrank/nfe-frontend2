import 'rxjs/add/observable/of';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Configuration } from './../../models/configuration';
import { ConfigurationService } from './../../services/configuration.service';
import { UploadFilesComponent } from './upload-files.component';
import { SharedModule } from '../../../../shared/shared.module';
import { FileUploadModule } from 'primeng/primeng';
import { TranslationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { FilesService } from '../../services/files.service';
import { of } from 'rxjs/observable/of';
import { HttpModule } from '@angular/http';
import { HttpClient } from '@angular/common/http';

describe('UploadFilesComponent', () => {
  let component: UploadFilesComponent;
  let fixture: ComponentFixture<UploadFilesComponent>;

  const file1: any = { name: 'abc.xyz', size: 1025 };
  const file2: any = { name: 'abcd', size: 1024 };
  const file3: any = { name: 'ESec111111.xxx', size: 1024 };
  const file4: any = { name: 'abc.xxx', size: 1025 };
  const file5: any = { name: 'ESec111111', size: 1024 };
  const file6: any = { name: 'abc.xyz', size: 1024 };
  const file7: any = { name: 'ESec111111.xyz', size: 1024 };

  const expectedConfiguration: Configuration = {
    fileNamePatterns: ['^abc.xyz$', '^[A-Z][A-Z0-9][a-z0-9]{2}.*$'],
    allowedFileExtensions: ['xyz', ''],
    maxDownloadTotalFileSizeForMultipleFiles: 5242880,
    maxUploadFilesNumber: 2,
    maxDownloadFilesNumber: 3,
    maxUploadFileSize: '1KB'
  };

  const expectedResponse = [
      {
        id: null,
        status: 200,
        message: 'OK',
        subject: 'abc.xyz',
        path: null
      }, {
        id: null,
        status: 200,
        message: 'OK',
        subject: 'ESec111111.xyz',
        path: null
      }
    ];

  const ConfigurationServiceSpy = jasmine.createSpyObj('ConfigurationService', [
    'get'
  ]);
  ConfigurationServiceSpy.get.and.returnValue(
    of(expectedConfiguration)
  );

  const filesServiceSpy = jasmine.createSpyObj <FilesService>('FilesService', [
    'uploadFiles'
  ]);
  filesServiceSpy.uploadFiles.and.returnValue(
    of(expectedResponse)
  );

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule,
        FileUploadModule,
        HttpModule,
        TranslationModule.forRoot(l10nConfig)
      ],
      declarations: [UploadFilesComponent],
      providers: [
        { provide: ConfigurationService, useValue: ConfigurationServiceSpy },
        { provide: FilesService, useValue: filesServiceSpy },
        { provide: HttpClient, useValue: HttpClient }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('COMPONENT can load instance', () => {
    expect(component).toBeTruthy();
  });

  it('should contain configuration', async () => {
    component.ngOnInit();
    fixture.whenRenderingDone().then(() => {
      expect(component.configuration).toEqual(expectedConfiguration);
    });
  });

  describe('FUNCTION: ', () => {
    it('FUNCTION isValidFileExtension() ', () => {
      const errSummary = `${file3.name}: Invalid file extension.`;
      // const errDetail = `allowed file extensions: '${component.configuration.allowedFileExtensions.join('\', \'').toString()}'`;
      const errDetail = '';
      expect(component.isValidFileExtension(file1)).toBe(true);
      expect(component.isValidFileExtension(file3)).toBe(false);
      expect(component.msgs[0]).toEqual({
        severity: 'error',
        summary: errSummary,
        detail: errDetail
      });
    });

    it('FUNCTION isValidNumFiles() ', () => {
      component.files = [file1, file2];
      expect(component.isValidNumFiles()).toBeTruthy();

      component.files = [file1, file2, file3];
      expect(component.isValidNumFiles()).toBeFalsy();

      const errMsg = 'Maximum number of allowable file uploads has been exceeded.';
      component.files = [file1, file2, file3];
      component.isValidNumFiles();
      expect(component.msgs[0]).toEqual({
        severity: 'error',
        summary: errMsg,
        detail: ''
      });
    });

    it('FUNCTION isValidFileSize() ', () => {
      expect(component.isValidFileSize(file5)).toBe(true);
      expect(component.isValidFileSize(file1)).toBe(false);
      const file: any = { name: file5.name, size: 1024 * 1024 };

      component.ngOnInit();
      component.configuration.maxUploadFileSize = '1MB';
      expect(component.isValidFileSize(file)).toBe(true);
      component.configuration.maxUploadFileSize = '1KB';

      const file_2: any = { name: file5.name, size: 1024 * 1024 + 1 };

      component.configuration.maxUploadFileSize = '1MB';
      expect(component.isValidFileSize(file_2)).toBe(false);
      component.configuration.maxUploadFileSize = '1KB';
    });


    it('FUNCTION isValidFileName() ', () => {
      expect(component.isValidFileName(file1)).toBe(true);
      expect(component.isValidFileName(file2)).toBe(false);

      const errSummary = `${file2.name}: Invalid file name.`;
      const errDetail = `The file should have a valid name.`;

      expect(component.msgs[0]).toEqual({
        severity: 'error',
        summary: errSummary,
        detail: errDetail
      });
    });

    it('FUNCTION onSelect() ', () => {
      const files: any[] = [file1, file2, file3, file4, file5, file6, file7];
      const event: any = { files: files };

      component.ngOnInit();
      // Multiple upload

      component.onSelect(event);

      expect(component.files.length).toEqual(expectedConfiguration.maxUploadFilesNumber);
      expect(component.files[0]).toEqual(file6);
      expect(component.files[1]).toEqual(file7);
    });

    it('FUNCTION sendUpload() ', () => {
      filesServiceSpy.uploadFiles.calls.reset();

      const files: any[] = [file6, file7];

      component.ngOnInit();

      fixture.detectChanges();

      component.sendUpload();
      component.files = files;
      component.sendUpload();
      expect(component.uploadedFiles).toEqual(expectedResponse);
      expect(filesServiceSpy.uploadFiles.calls.count()).toBe(1);
    });

    it('FUNCTION cancelUpdate() ', () => {
      component.cancelUpdate();
      expect(component.files).toEqual([]);
      expect(component.msgs).toEqual([]);
    });

    it('FUNCTION removeMessage() ', () => {

      let errSummary = `File: Invalid file size.`;
      let errDetail = `Maximum upload size is 1024`;

      const msg1 = { severity: 'error', summary: errSummary, detail: errDetail };

      errSummary = `File: Invalid file name.`;
      errDetail = `The file should have a valid name.`;

      const msg2 = { severity: 'error', summary: errSummary, detail: errDetail };

      component.msgs.push(msg1);
      component.msgs.push(msg2);

      expect(component.msgs.length).toEqual(2);

      component.removeMessage(msg2);

      expect(component.msgs.length).toEqual(1);

      component.removeMessage(msg1);

      expect(component.msgs.length).toEqual(0);
    });

    it('FUNCTION removeAllMessages() ', () => {

      let errSummary = `File: Invalid file size.`;
      let errDetail = `Maximum upload size is 1024`;

      const msg1 = { severity: 'error', summary: errSummary, detail: errDetail };

      errSummary = `File: Invalid file name.`;
      errDetail = `The file should have a valid name`;

      const msg2 = { severity: 'error', summary: errSummary, detail: errDetail };

      component.msgs.push(msg1);
      component.msgs.push(msg2);

      expect(component.msgs.length).toEqual(2);

      component.removeAllMessages();

      expect(component.msgs.length).toEqual(0);
      expect(component.msgs).toEqual([]);
    });

  });
});
