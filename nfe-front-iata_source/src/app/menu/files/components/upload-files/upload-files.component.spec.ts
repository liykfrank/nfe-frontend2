import 'rxjs/add/observable/of';

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { SharedModule } from '../../../../shared/shared.module';
import { Configuration } from './../../models/configuration';
import { ConfigurationService } from './../../services/configuration.service';
import { UploadFilesComponent } from './upload-files.component';

xdescribe('UploadFilesComponent', () => {
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

  const ConfigurationServiceSpy = jasmine.createSpyObj('ConfigurationService', [
    'get'
  ]);
  ConfigurationServiceSpy.get.and.returnValue(
    Observable.of(expectedConfiguration)
  );

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [UploadFilesComponent],
      providers: [
        { provide: ConfigurationService, useValue: ConfigurationServiceSpy }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain configuration', async () => {
    component.ngOnInit();
    fixture.whenRenderingDone().then(() => {
      expect(component.configuration).toEqual(expectedConfiguration);
    });
  });

  it('#isValidFileExtension should return true', () => {
    expect(component.isValidFileExtension(file1)).toBe(true);
  });

  it('#isValidFileExtension should return false and save an error message', () => {
    expect(component.isValidFileExtension(file3)).toBe(false);
  });

  it('#isValidFileExtension should save an error message', () => {
    const errSummary = `${file3.name}: Invalid file extension,`;
    // const errDetail = `allowed file extensions: '${component.configuration.allowedFileExtensions.join('\', \'').toString()}'`;
    const errDetail = '';
    component.isValidFileExtension(file3);
    expect(component.msgs[0]).toEqual({
      severity: 'error',
      summary: errSummary,
      detail: errDetail
    });
  });

  it('#isValidNumFiles should return true', () => {
    component.myFileUpload.files = [file1, file2];
    expect(component.isValidNumFiles()).toBe(true);
  });

  it('#isValidNumFiles should return false', () => {
    component.myFileUpload.files = [file1, file2, file3];
    expect(component.isValidNumFiles()).toBe(false);
  });

  it('#isValidNumFiles should save an error message', () => {
    const errMsg = 'Maximum number of allowable file uploads has been exceeded';
    component.myFileUpload.files = [file1, file2, file3];
    component.isValidNumFiles();
    expect(component.msgs[0]).toEqual({
      severity: 'error',
      summary: errMsg,
      detail: ''
    });
  });

  it('#isValidFileSize should return true (KB)', () => {
    expect(component.isValidFileSize(file5)).toBe(true);
  });

  it('#isValidFileSize should return false (KB)', () => {
    expect(component.isValidFileSize(file1)).toBe(false);
  });

  it('#isValidFileSize should return true (MB)', () => {
    const file: any = { name: file5.name, size: 1024 * 1024 };
    component.ngOnInit();
    component.configuration.maxUploadFileSize = '1MB';
    expect(component.isValidFileSize(file)).toBe(true);
    component.configuration.maxUploadFileSize = '1KB';
  });

  it('#isValidFileSize should return false (MB)', () => {
    const file: any = { name: file5.name, size: 1024 * 1024 + 1 };

    component.ngOnInit();
    component.configuration.maxUploadFileSize = '1MB';
    expect(component.isValidFileSize(file)).toBe(false);
    component.configuration.maxUploadFileSize = '1KB';
  });

  it('#isValidFileName should return true', () => {
    const file: any = {};
    file.name = 'abc.xyz';
    expect(component.isValidFileName(file1)).toBe(true);
  });

  it('#isValidFileName should return false', () => {
    expect(component.isValidFileName(file2)).toBe(false);
  });

  it('#isValidFileName should save an error message', () => {
    const errSummary = `${file2.name}: Invalid file name,`;
    const errDetail = `the file should have a valid name`;
    component.isValidFileName(file2);
    expect(component.msgs[0]).toEqual({
      severity: 'error',
      summary: errSummary,
      detail: errDetail
    });
  });

  it('#onSelect should select 2 files', () => {
    const files: any[] = [file1, file2, file3, file4, file5, file6, file7];
    const event: any = { files: files };

    component.ngOnInit();
    // Multiple upload
    component.myFileUpload.files = [
      file1,
      file2,
      file3,
      file4,
      file5,
      file6,
      file7
    ];
    component.onSelect(event);

    expect(component.myFileUpload.files.length).toEqual(2);
    expect(component.myFileUpload.files[0]).toEqual({
      name: 'abc.xyz',
      size: 1024
    });
    expect(component.myFileUpload.files[1]).toEqual({
      name: 'ESec111111.xyz',
      size: 1024
    });

    console.log('Uploaded files:');
    console.log(component.myFileUpload.files);
  });

  it('#onUpload should upload 2 files', () => {
    const files1: any[] = [file3];
    const event1: any = { files: files1 };
    const files2: any[] = [file5, file6];
    const event2: any = { files: files2 };

    component.ngOnInit();
    component.onUpload(event1);
    component.onUpload(event2);
    expect(component.uploadedFiles).toEqual(files2);
  });
});
