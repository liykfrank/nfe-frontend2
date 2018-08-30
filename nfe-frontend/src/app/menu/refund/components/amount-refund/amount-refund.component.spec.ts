import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { Currency } from '../../../../shared/components/currency/models/currency.model';
import { CurrencyService } from '../../../../shared/components/currency/services/currency.service';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { RefundConfigurationService } from '../../services/refund-configuration.service';
import { AmountRefundFormModel } from './../../models/amount-refund-form.model';
import { RefundConfiguration } from './../../models/refund-configuration.model';
import { AmountRefundComponent } from './amount-refund.component';

describe('AmountRefundComponent', () => {
  let comp: AmountRefundComponent;
  let fixture: ComponentFixture<AmountRefundComponent>;

  const refundConfigurationServiceStub = jasmine.createSpyObj<RefundConfigurationService>
                                            ('RefundConfigurationService', ['getConfiguration', 'getCountCuponsState']);
  refundConfigurationServiceStub.getConfiguration.and.returnValue(of(new RefundConfiguration()));
  refundConfigurationServiceStub.getCountCuponsState.and.returnValue(of(0));

  const currencyServiceStub = jasmine.createSpyObj<CurrencyService>('CurrencyService', ['getCurrencyState']);
  currencyServiceStub.getCurrencyState.and.returnValue(of(new Currency()));

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [AmountRefundComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: CurrencyService, useValue: currencyServiceStub },
        {
          provide: RefundConfigurationService,
          useValue: refundConfigurationServiceStub
        }
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AmountRefundComponent);
    comp = fixture.componentInstance;

    comp.model = new AmountRefundFormModel();
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('errorPartialRefund defaults to: false', () => {
    expect(comp.errorPartialRefund).toEqual(false);
  });

});
