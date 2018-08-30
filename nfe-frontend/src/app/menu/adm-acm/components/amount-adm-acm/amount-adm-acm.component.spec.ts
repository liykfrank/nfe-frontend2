import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { AdmAcmConfiguration } from '../../models/adm-acm-configuration.model';
import { AcdmConfigurationService } from '../../services/acdm-configuration.service';
import { BasicInfoService } from '../../services/basic-info.service';
import { AcdmAmountForm } from './../../models/acdm-amount-form.model';
import { AmountAdmAcmComponent } from './amount-adm-acm.component';

describe('AmountAdmAcmComponent', () => {
  let comp: AmountAdmAcmComponent;
  let fixture: ComponentFixture<AmountAdmAcmComponent>;

  const acdmConfigurationServiceStub = jasmine.createSpyObj<AcdmConfigurationService>('AcdmConfigurationService', ['getConfiguration']);
  acdmConfigurationServiceStub.getConfiguration.and.returnValue(of(new AdmAcmConfiguration()));

  const basicInfoServiceStub = jasmine.createSpyObj<BasicInfoService>(
    'BasicInfoService',
    [
      'getConcernsIndicator',
      'getShowSpam',
      'getSubType',
      'getCurrency',
      'getToca'
    ]
  );
  basicInfoServiceStub.getConcernsIndicator.and.returnValue(of('I'));
  basicInfoServiceStub.getShowSpam.and.returnValue(of(false));
  basicInfoServiceStub.getSubType.and.returnValue(of('ADMA'));
  basicInfoServiceStub.getCurrency.and.returnValue(of(new Currency()));
  basicInfoServiceStub.getToca.and.returnValue(of(''));

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslationModule.forRoot(l10nConfig), IssueSharedModule],
      declarations: [AmountAdmAcmComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: AcdmConfigurationService,
          useValue: acdmConfigurationServiceStub
        },
        { provide: BasicInfoService, useValue: basicInfoServiceStub }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AmountAdmAcmComponent);
    comp = fixture.componentInstance;
    comp.isAdm = true;
    comp.model = new AcdmAmountForm();
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('amountForm defaults to: acdmAmountModel._amountModelGroup', () => {
    expect(comp.amountForm).not.toBeNull();
    expect(comp.agentCalcFormGroup).not.toBeNull();
    expect(comp.airlineCalcFormGroup).not.toBeNull();
  });

  it('totalAgent defaults to: 0', () => {
    expect(comp.totalAgent).toEqual(0);
  });

  it('totalAirline defaults to: 0', () => {
    expect(comp.totalAirline).toEqual(0);
  });

  it('total defaults to: 0', () => {
    expect(comp.total).toEqual(0);
  });

  it('ngOnInit: makes expected calls', () => {
    comp.ngOnInit();
    expect(basicInfoServiceStub.getConcernsIndicator).toHaveBeenCalled();
    expect(basicInfoServiceStub.getShowSpam).toHaveBeenCalled();
    expect(basicInfoServiceStub.getSubType).toHaveBeenCalled();
    expect(basicInfoServiceStub.getCurrency).toHaveBeenCalled();
    expect(basicInfoServiceStub.getToca).toHaveBeenCalled();
  });
});
