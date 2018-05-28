import { ListData } from './../../../files/models/list-data';
import { FilterTest } from './test/fiter-test';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TablePaginationComponent } from './table-pagination.component';
import { SharedModule } from '../../shared.module';
import { SortType } from '../../../files/models/sort-type.enum';
import { EventEmitter } from '@angular/core';
import { TestModel } from './test/test-model';
import { Pagination } from '../../../files/models/pagination';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';

describe('TablePaginationComponent', () => {
  let component: TablePaginationComponent;
  let fixture: ComponentFixture<TablePaginationComponent>;
  let table: jasmine.SpyObj<jqxGridComponent>;
  table = jasmine.createSpyObj<jqxGridComponent>('table', ['getrows','getselectedrowindexes',
  'getrowdata','updatebounddata','clearselection','selectrow']);
  table.getrows.and.returnValue([{},{}]);
  table.getselectedrowindexes.and.returnValue([0,1]);
  table.getrowdata.and.returnValue({});

  let model1 =new TestModel('model1',4,'');
  let model2 =new TestModel('model2',4,'');
  let model3 =new TestModel('model3',4,'');
  let pagination=new Pagination({});
  pagination.totalPages=2;
  let listData=new ListData<TestModel>([model1,model2,model3],pagination);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SharedModule],
      declarations: [ ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TablePaginationComponent);
    component = fixture.componentInstance;
    component.table= table;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create table grid', () => {
    expect(component.table).toBeTruthy();
  });

  it('click num page', () => {
    const emitSpy = spyOn(component.pageChange, 'emit');
    component.clNumPage(0);
    expect(emitSpy).toHaveBeenCalledTimes(1);
  });


  it('customer sort func', () => {
    const filterCurrent: FilterTest= new FilterTest();
    const filterIni: FilterTest= new FilterTest() ;
    const emitSpy = spyOn(component.reload, 'emit');
    component.customsortfunc('col1',SortType.ASC,filterCurrent,filterIni);
    expect(emitSpy).toHaveBeenCalledTimes(1);
  });

  it('update pagination data', () => {
    let spyAddRow= spyOn(component.rowsHelper,'addRowPage').and.callThrough();

    component.updatePaginationData(listData,0);

    expect(table.updatebounddata).toHaveBeenCalledTimes(1);
    expect(spyAddRow).toHaveBeenCalledTimes(2);
    expect(component.pagesar.length).toBeGreaterThan(0);
    expect(component.rowsHelper).toBeTruthy();
    expect(component.rowsHelper.listPagesRows.length).toBeGreaterThan(0);
  });

  it('get all selected rows', () => {
    const emitSpy = spyOn(component.reload, 'emit');
    let spyGetAll= spyOn(component.rowsHelper,'getAllData').and.callThrough();
    let rows:TestModel[]= component.getAllSelected();
    expect(rows.length).toBe(2);
    expect(spyGetAll).toHaveBeenCalled();

  });

  it('show alert', () => {
    const alertSpy = spyOn(window, 'alert');
    component.showAlert('bb');
    expect(alertSpy).toHaveBeenCalledTimes(1);

  });
});
