import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { AcdmBasicInfoFormModel } from '../../models/acdm-basic-info-form.model';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { AcdmConfigurationService } from '../../services/acdm-configuration.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { CurrencyService } from './../../../../shared/components/currency/services/currency.service';
import { PeriodService } from './../../services/period.service';
import { TocaService } from './../../services/toca.service';
import { BasicInfoAdmAcmComponent } from './basic-info-adm-acm.component';

describe('BasicInfoAdmAcmComponent', () => {
  let comp: BasicInfoAdmAcmComponent;
  let fixture: ComponentFixture<BasicInfoAdmAcmComponent>;

  const acdmConfigurationServiceStub = jasmine.createSpyObj<
    AcdmConfigurationService
  >('AcdmConfigurationService', ['getConfiguration', 'getConfigurationByISO']);
  acdmConfigurationServiceStub.getConfiguration.and.returnValue(
    of(new AdmAcmConfiguration())
  );
  acdmConfigurationServiceStub.getConfigurationByISO.and.returnValue(of());

  const basicInfoServiceStub = jasmine.createSpyObj<BasicInfoService>(
    'BasicInfoService',
    [
      'setToca',
      'setSubType',
      'setShowSpam',
      'setConcernsIndicator',
      'setCurrency'
    ]
  );
  basicInfoServiceStub.setToca.and.returnValue(of());
  basicInfoServiceStub.setSubType.and.returnValue(of());
  basicInfoServiceStub.setShowSpam.and.returnValue(of());
  basicInfoServiceStub.setConcernsIndicator.and.returnValue(of());
  basicInfoServiceStub.setCurrency.and.returnValue(of());

  const alertsServiceStub = jasmine.createSpyObj<AlertsService>(
    'AlertsService',
    ['getAccept', 'setAlertTranslate']
  );
  alertsServiceStub.getAccept.and.returnValue(of());
  alertsServiceStub.setAlertTranslate.and.returnValue(of());

  const currencyServiceStub = jasmine.createSpyObj<CurrencyService>(
    'CurrencyService',
    ['getCurrencyState']
  );
  currencyServiceStub.getCurrencyState.and.returnValue(of());

  const periodServiceStub = jasmine.createSpyObj<PeriodService>(
    'PeriodService',
    ['getPeriodWithISO']
  );
  periodServiceStub.getPeriodWithISO.and.returnValue(of());

  const tocaServiceStub = jasmine.createSpyObj<TocaService>('TocaService', [
    'getTocaWithISO'
  ]);
  tocaServiceStub.getTocaWithISO.and.returnValue(of());

  const translationServiceStub = jasmine.createSpyObj<TranslationService>(
    'TranslationService',
    ['translate']
  );
  translationServiceStub.translate.and.returnValue(of('TEXT'));

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslationModule.forRoot(l10nConfig), IssueSharedModule, HttpClientModule
      ],
      declarations: [BasicInfoAdmAcmComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: AcdmConfigurationService,
          useValue: acdmConfigurationServiceStub
        },
        { provide: BasicInfoService, useValue: basicInfoServiceStub },
        { provide: AlertsService, useValue: alertsServiceStub },
        { provide: CurrencyService, useValue: currencyServiceStub },
        { provide: PeriodService, useValue: periodServiceStub },
        { provide: TocaService, useValue: tocaServiceStub },
        { provide: TranslationService, useValue: translationServiceStub }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BasicInfoAdmAcmComponent);
    comp = fixture.componentInstance;
    comp.isAdm = true;
    comp.model = new AcdmBasicInfoFormModel();
    comp.countryList = [
      { isoCountryCode: 'AA', name: 'Country AA' },
      { isoCountryCode: 'BB', name: 'Country BB' },
      { isoCountryCode: 'CC', name: 'Country CC' }
    ];
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('isAdm defaults to: true', () => {
    expect(comp.isAdm).toEqual(true);
  });

  it('type defaults to: EnvironmentType.ACDM', () => {
    expect(comp.type).toEqual(EnvironmentType.ACDM);
  });

  it('forList defaults to: [I, R, X, E]', () => {
    expect(comp.forList).toEqual(['I', 'R', 'X', 'E']);
  });

  it('tocaList defaults to: []', () => {
    expect(comp.tocaList).toEqual([]);
  });

  it('currencyList defaults to: []', () => {
    expect(comp.currencyList).toEqual([]);
  });

  it('periodNumber defaults to: []', () => {
    expect(comp.periodNumber).toEqual([]);
  });

  it('periodMonth defaults to: []', () => {
    expect(comp.periodMonth).toEqual([]);
  });

  it('periodYear defaults to: []', () => {
    expect(comp.periodYear).toEqual([]);
  });

  it('ngOnInit: makes expected calls', () => {
    comp.ngOnInit();
    expect(basicInfoServiceStub.setSubType).toHaveBeenCalled();
    expect(basicInfoServiceStub.setConcernsIndicator).toHaveBeenCalled();
    expect(basicInfoServiceStub.setToca).toHaveBeenCalled();
    expect(currencyServiceStub.getCurrencyState).toHaveBeenCalled();
  });
});
