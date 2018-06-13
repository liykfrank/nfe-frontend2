import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { defer } from 'q';
import { of } from 'rxjs/observable/of';

import { TabsStateService } from '../../../../core/services/tabs-state.service';
import { SharedModule } from '../../../../shared/shared.module';
import { FileNw } from '../../models/file';
import { ListData } from '../../models/list-data';
import { ListFilesFilter } from '../../models/list-files-filter';
import { ListFilesService } from '../../services/list-files.service';
import { FilesFilterComponent } from '../files-filter/files-filter.component';
import { DataTest } from './data-test';
import { FilesDownloadComponent } from './files-download.component';

function asyncData<T>(data: T) {
  const d = defer();
  //Promise.resolve(data);
  //d.=Promise.resolve(data);
  d.resolve(Promise.resolve(data));
  return d;
  //return defer(() => Promise.resolve(data));
}

xdescribe('FilesDownloadComponent', () => {
  let component: FilesDownloadComponent;
  let fixture: ComponentFixture<FilesDownloadComponent>;
  let fileService, spyList: jasmine.Spy;
  const dataTest = new DataTest();

  beforeEach(async(() => {
    // Make the spy return a synchronous Observable with the test data
    fileService = jasmine.createSpyObj('ListFilesService', [
      'listFilesData',
      'getStatusCodes',
      'removeFile',
      'removeFiles',
      'downloadFile',
      'downloadFiles',
      'TabsStateService'
    ]);
    const list = new ListData<FileNw>(dataTest.fileList, dataTest.pagination);
    spyList = fileService.listFilesData.and.returnValue(of(list));
    fileService.getStatusCodes.and.returnValue(dataTest.statusCodes);

    // Make the spy return a synchronous Observable with the test data
    spyList = fileService.listFilesData.and.returnValue(of(list));
    fileService.getStatusCodes.and.returnValue(dataTest.statusCodes);

    TestBed.configureTestingModule({
      imports: [SharedModule, BrowserAnimationsModule],
      declarations: [FilesDownloadComponent, FilesFilterComponent],
      providers: [
        {
          provide: ListFilesService,
          useValue: fileService
        },
        TabsStateService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesDownloadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create table pagination', () => {
    expect(component.tablePagination).toBeTruthy();
  });

  it('should create table pagination grid', () => {
    expect(component.tablePagination.table).toBeTruthy();
  });

  it('should onInit in proper way', () => {
    component.ngOnInit();
  });

  it('status list defined and coontains elements', () => {
    component.ngOnInit();
    expect(component.statusList).toBeDefined();
    expect(component.statusList.length).toBeGreaterThan(0);
  });

  it(' filter ini created-   ', () => {
    component.ngOnInit();
    expect(component.dataFilterIni).toBeDefined();
    expect(component.dataFilterIni.minUploadDate).toBeDefined();
    expect(component.dataFilterIni.maxUploadDate).toBeDefined();
    expect(component.dataFilterIni.name).toBeUndefined();
  });

  it(' filter current created   ', () => {
    component.ngOnInit();
    expect(component.dataFilterCurrent).toBeDefined();
    expect(component.dataFilterCurrent.minUploadDate).toBeDefined();
    expect(component.dataFilterCurrent.maxUploadDate).toBeDefined();
    expect(component.dataFilterCurrent.name).toBeUndefined();
  });

  it(' filter load created   ', () => {
    component.ngOnInit();
    expect(component.dataFilterLoad).toBeDefined();
    expect(component.dataFilterLoad.minUploadDate).toBeDefined();
    expect(component.dataFilterLoad.maxUploadDate).toBeDefined();
    expect(component.dataFilterLoad.name).toBeUndefined();
  });

  it('after view in proper way ', () => {
    component.ngOnInit();
    component.ngAfterViewInit();
    expect(component).toBeTruthy();
  });

  it('load data files on view init', async(() => {
    fixture.detectChanges();

    expect(spyList.calls.any()).toBe(true, 'spi list called');
    expect(component.dataFiles).toBeDefined();
    expect(component.dataFiles.length).toBeGreaterThan(0);
    console.log('size files ' + component.dataFiles.length);
  }));

  it('load data files on table', async(() => {
    fixture.detectChanges();
    expect(component.tablePagination.table).toBeDefined();
    expect(component.tablePagination.table.getrows().length).toBe(3);
    expect(component.tablePagination.table.getrowdata(1)).toBeDefined();
    expect(component.tablePagination.table.getrowdata(5)).toBeUndefined();
  }));

  it('selection data files on table', async(() => {
    fixture.detectChanges();
    expect(component.tablePagination.table).toBeDefined();
    expect(component.tablePagination.table.getrows().length).toBe(3);
    component.tablePagination.table.selectedrowindex(1);
    expect(component.tablePagination.table.getselectedrowindex()).toBe(1);
    expect(component.tablePagination.table.getselectedrowindex()).not.toBe(2);
  }));

  it('delete from selection data files ', () => {
    const spyRemove = fileService.removeFile.and.returnValue(of({}));

    fixture.detectChanges();
    expect(component.tablePagination.table).toBeDefined();
    expect(component.tablePagination.table.getrows().length).toBe(3);
    component.tablePagination.table.selectedrowindex(1);
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('#deleteFiles button'));
    expect(btn).toBeDefined();
    btn.nativeElement.click();

    fixture.detectChanges();

    expect(spyRemove.calls.any()).toBe(true, 'spi remove file called');
  });

  it('delete multiple  from  selection data files ', () => {
    const spyRemove = fileService.removeFiles.and.returnValue(of({}));
    fixture.detectChanges();
    expect(component.tablePagination.table).toBeDefined();
    expect(component.tablePagination.table.getrows().length).toBe(3);
    component.tablePagination.table.selectedrowindex(1);
    component.tablePagination.table.selectedrowindex(2);
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('#deleteFiles button'));
    expect(btn).toBeDefined();
    btn.nativeElement.click();
    fixture.detectChanges();
    expect(spyRemove.calls.any()).toBe(true, 'spi removes file called');
  });

  it('download  from  selection data files ', () => {
    const spyDownload = fileService.downloadFile.and.returnValue(of({}));

    fixture.detectChanges();
    expect(component.tablePagination.table).toBeDefined();
    expect(component.tablePagination.table.getrows().length).toBe(3);
    component.tablePagination.table.selectedrowindex(1);
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('#downloadFiles button'));
    expect(btn).toBeDefined();
    btn.nativeElement.click();
    fixture.detectChanges();
    expect(spyDownload.calls.any()).toBe(true, 'spi download file called');
  });

  it('download files  from  selection data files ', () => {
    const spyDownload = fileService.downloadFiles.and.returnValue(of({}));

    fixture.detectChanges();
    expect(component.tablePagination.table).toBeDefined();
    expect(component.tablePagination.table.getrows().length).toBe(3);
    component.tablePagination.table.selectedrowindex(1);
    component.tablePagination.table.selectedrowindex(2);
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('#downloadFiles button'));
    expect(btn).toBeDefined();
    btn.nativeElement.click();
    fixture.detectChanges();
    expect(spyDownload.calls.any()).toBe(true, 'spi download filess called');
  });

  it('should search on click button    ', () => {
    const searhSpy = spyOn(component, 'searchCl').and.callThrough();
    fixture.detectChanges();
    const btn = fixture.debugElement.query(By.css('#searchFiles button'));
    expect(btn).toBeTruthy();
    const element = fixture.debugElement.query(By.css('app-files-filter'));
    expect(element).toBeTruthy();
    expect(element.componentInstance.form).toBeTruthy();
    element.componentInstance.form.form.markAsDirty();
    component.dataFilterLoad = new ListFilesFilter();
    fixture.detectChanges();
    btn.nativeElement.click();
    fixture.detectChanges();
    expect(component.searchCl).toHaveBeenCalled();
    expect(spyList.calls.count()).toBe(2, 'spi list called');
    console.log('list calls ' + spyList.calls.count());
  });

  it('should reset All Filters    ', () => {
    const searhSpy = spyOn(component, 'searchCl').and.callThrough();
    const crumbsSpy = spyOn(component, 'updateFilterCrumbs').and.callThrough();

    fixture.detectChanges();
    component.resetAllFilters();
    expect(component.searchCl).toHaveBeenCalled();
    expect(component.updateFilterCrumbs).toHaveBeenCalled();
    expect(spyList.calls.count()).toBe(2, 'spi list called');
  });

  it('should reset reset Filter Bread    ', () => {
    const searhSpy = spyOn(component, 'resetFilterBread').and.callThrough();
    fixture.detectChanges();
    component.resetFilterBread(filter => filter);
    expect(component.resetFilterBread).toHaveBeenCalled();
    expect(spyList.calls.count()).toBe(2, 'spi list called');
  });

  it('elements Filters reset   ', () => {
    const crumbsSpy = spyOn(component, 'updateFilterCrumbs').and.callThrough();
    fixture.detectChanges();
    component.resetAllFilters();
    component.elementsFilter[0].resetFilter();
    component.elementsFilter[1].resetFilter();
    component.elementsFilter[2].resetFilter();
    expect(component.updateFilterCrumbs).toHaveBeenCalled();
  });

  it('page changes  ', () => {
    const updateDataSpy = spyOn(
      component.tablePagination,
      'updatePaginationData'
    ).and.callThrough();
    fixture.detectChanges();
    component.pageChange(1);
    expect(updateDataSpy).toHaveBeenCalled();
    expect(updateDataSpy.calls.argsFor(0)[1]).toBe(1);
  });

});
