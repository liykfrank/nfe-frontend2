import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesFilterComponent } from './files-filter.component';
import { SharedModule } from '../../../shared/shared.module';
import { ListFilesFilter } from '../../models/list-files-filter';
import { By } from '@angular/platform-browser';

describe('FilesFilterComponent', () => {
  let component: FilesFilterComponent;
  let fixture: ComponentFixture<FilesFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [ FilesFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FilesFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should status list fill ', () => {
    component.statusList=['READ','UNREAD'];
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.statusList).toBeTruthy();
    expect(component.statusListItems).toBeTruthy();
    expect(component.statusListItems.length).toBeGreaterThan(0);
    expect(component.downStatusComp.getItems().length).toBe(2);
  });

  it('should date range setted ', () => {
    const filter=new ListFilesFilter();
    filter.minUploadDate= new Date();
    filter.maxUploadDate= new Date();
    component.dataFilters= filter;
    component.ngOnInit();
    fixture.detectChanges();
    expect(component.myDateTimeInput).toBeTruthy();
    expect(component.myDateTimeInput.getRange({})).toBeTruthy();
    expect(component.myDateTimeInput.getRange({}).from).toBeTruthy();
    expect(component.myDateTimeInput.getRange({}).to).toBeTruthy();
  });

  it('should search', () => {
    const spySearch =spyOn(component,'search');
    component.search();
    expect(spySearch).toHaveBeenCalledTimes(1);
  });

  it('should search when click', () => {
    const spySearch =spyOn(component,'search').and.callThrough();
    const spyEmitSearch =spyOn(component.searchCl,'emit');
    const btn = fixture.debugElement.query(By.css('#searchFiles button'));
    expect(btn).toBeTruthy();
    component.ngOnInit();
    component.form.form.markAsDirty();
    fixture.detectChanges();
    btn.nativeElement.click();
    fixture.detectChanges();
    expect(spySearch).toHaveBeenCalledTimes(1);
    expect(spyEmitSearch).toHaveBeenCalledTimes(1);
  });

  it('should update Widgets', () => {
    const spyUpdate =spyOn(component,'updateWidgets');
    component.updateWidgets();
    expect(spyUpdate).toHaveBeenCalledTimes(1);
  });

  it('should reset', () => {
    const spyEmitReset =spyOn(component.resetCl,'emit');
    const spyReset =spyOn(component,'reset').and.callThrough();;
    component.reset();
    expect(spyReset).toHaveBeenCalled();
    expect(spyEmitReset).toHaveBeenCalled();
  });

  it('should date change', () => {
    const spyChange =spyOn(component,'dateOnChange');
    component.dateOnChange();
    expect(spyChange).toHaveBeenCalledTimes(1);
  });

});
