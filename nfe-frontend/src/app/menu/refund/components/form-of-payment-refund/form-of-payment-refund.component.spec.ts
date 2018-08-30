import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { CurrencyService } from '../../../../shared/components/currency/services/currency.service';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { RefundConfiguration } from '../../models/refund-configuration.model';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { FormOfPaymentRefundComponent } from './form-of-payment-refund.component';
import { FormOfPaymentRefundFormModel } from '../../models/form-of-payment-refund-form.model';

describe('FormOfPaymentRefundComponent', () => {
  let comp: FormOfPaymentRefundComponent;
  let fixture: ComponentFixture<FormOfPaymentRefundComponent>;

  const currencyServiceStub = jasmine.createSpyObj<CurrencyService>('CurrencyService', ['getCurrencyState']);
  currencyServiceStub.getCurrencyState.and.returnValue(of(new Currency()));

  const refundConfigurationServiceStub = jasmine.createSpyObj<RefundConfigurationService>('RefundConfigurationService', ['getConfiguration']);
  refundConfigurationServiceStub.getConfiguration.and.returnValue(of(new RefundConfiguration()));

  const translationServiceStub = jasmine.createSpyObj<TranslationService>('TranslationService', ['translate']);
  translationServiceStub.translate.and.returnValue(of('TEST'));

  const alertsServiceStub = jasmine.createSpyObj<AlertsService>('AlertsService', ['setAlertTranslate', 'setAlert']);
  alertsServiceStub.setAlertTranslate.and.returnValue(of());
  alertsServiceStub.setAlert.and.returnValue(of());

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [FormOfPaymentRefundComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: TranslationService, useValue: translationServiceStub },
        {
          provide: RefundConfigurationService,
          useValue: refundConfigurationServiceStub
        },
        { provide: AlertsService, useValue: alertsServiceStub },
        { provide: CurrencyService, useValue: currencyServiceStub }
      ]
    });
    fixture = TestBed.createComponent(FormOfPaymentRefundComponent);
    comp = fixture.componentInstance;
    comp.model = new FormOfPaymentRefundFormModel();
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('type defaults to: EnvironmentType.REFUND_INDIRECT', () => {
    expect(comp.type).toEqual(EnvironmentType.REFUND_INDIRECT);
  });

  it('validFopSelected defaults to: true', () => {
    expect(comp.validFopSelected).toEqual(true);
  });

  describe('ngOnInit', () => {
    it('makes expected calls', () => {
      comp.ngOnInit();
      expect(refundConfigurationServiceStub.getConfiguration).toHaveBeenCalled();
      expect(currencyServiceStub.getCurrencyState).toHaveBeenCalled();
    });
  });

});
