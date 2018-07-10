import { BasicInfoModel } from './../../models/basic-info.model';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeBarComponent } from './resume-bar.component';
import { AdmAcmService } from '../../services/adm-acm.service';
import { AmountService } from '../../services/amount.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { Observable } from 'rxjs/Observable';
import { ScreenType } from '../../../../shared/models/screen-type.enum';
import { SharedModule } from '../../../../shared/shared.module';
import { KeyValue } from '../../../../shared/models/key.value.model';

describe('ResumeBarComponent', () => {
  let component: ResumeBarComponent;
  let fixture: ComponentFixture<ResumeBarComponent>;

  const countries = [
    {'isoCountryCode': 'AA', 'name': 'Country AA'},
    {'isoCountryCode': 'BB', 'name': 'Country BB'},
    {'isoCountryCode': 'CC', 'name': 'Country CC'}
  ];

  const list: KeyValue[] = [];
  list.push({code: 'I', description: 'I'});
  list.push({code: 'R', description: 'R'});
  list.push({code: 'X', description: 'X'});
  list.push({code: 'E', description: 'E'});

  const bi = new BasicInfoModel();
  bi.currency = null;

  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService', ['getScreenType', 'getCurrency', 'getDateOfIssue']);
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService', ['getTotal']);
  const _BasicInfoService = jasmine.createSpyObj<BasicInfoService>('BasicInfoService', ['getCountries', 'getBasicInfo', 'getSPDRCombo']);

  _BasicInfoService.getCountries.and.returnValue(Observable.of(countries));
  _AdmAcmService.getCurrency.and.returnValue(Observable.of(2));
  _AdmAcmService.getDateOfIssue.and.returnValue(new Date().toISOString());
  _AdmAcmService.getScreenType.and.returnValue(Observable.of(ScreenType.CREATE));
  _AmountService.getTotal.and.returnValue(Observable.of(1));

  _BasicInfoService.getBasicInfo.and.returnValues(Observable.of(bi));
  _BasicInfoService.getSPDRCombo.and.returnValue(list);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule ],
      providers: [
        {provide: AdmAcmService, useValue: _AdmAcmService},
        {provide: AmountService, useValue: _AmountService},
        {provide: BasicInfoService, useValue: _BasicInfoService}
      ],
      declarations: [ ResumeBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResumeBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});

describe('ResumeBarComponent 2', () => {
  let component: ResumeBarComponent;
  let fixture: ComponentFixture<ResumeBarComponent>;

  const countries = [
    {'isoCountryCode': 'AA', 'name': 'Country AA'},
    {'isoCountryCode': 'BB', 'name': 'Country BB'},
    {'isoCountryCode': 'CC', 'name': 'Country CC'}
  ];

  const list: KeyValue[] = [];
  list.push({code: 'I', description: 'I'});
  list.push({code: 'R', description: 'R'});
  list.push({code: 'X', description: 'X'});
  list.push({code: 'E', description: 'E'});

  const bi = new BasicInfoModel();
  bi.agent.iataCode = 'TEXT';

  const _AdmAcmService = jasmine.createSpyObj<AdmAcmService>('AdmAcmService', ['getScreenType', 'getCurrency', 'getDateOfIssue']);
  const _AmountService = jasmine.createSpyObj<AmountService>('AmountService', ['getTotal']);
  const _BasicInfoService = jasmine.createSpyObj<BasicInfoService>('BasicInfoService', ['getCountries', 'getBasicInfo', 'getSPDRCombo']);

  _BasicInfoService.getCountries.and.returnValue(Observable.of(countries));
  _AdmAcmService.getCurrency.and.returnValue(Observable.of({ name: 'AA', numDecimals: 2 }));
  _AdmAcmService.getDateOfIssue.and.returnValue(new Date().toISOString());
  _AdmAcmService.getScreenType.and.returnValue(Observable.of(ScreenType.CREATE));
  _AmountService.getTotal.and.returnValue(Observable.of(1));

  _BasicInfoService.getBasicInfo.and.returnValues(Observable.of(bi));
  _BasicInfoService.getSPDRCombo.and.returnValue(list);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ SharedModule ],
      providers: [
        {provide: AdmAcmService, useValue: _AdmAcmService},
        {provide: AmountService, useValue: _AmountService},
        {provide: BasicInfoService, useValue: _BasicInfoService}
      ],
      declarations: [ ResumeBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResumeBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('with values', () => {
    expect(component.agent_code == 'TEXT').toBe(true, 'agent_code');
  });

});
