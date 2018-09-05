import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { AlertsService } from '../../core/services/alerts.service';
import { UserService } from '../../core/services/user.service';
import { l10nConfig } from '../../shared/base/conf/l10n.config';
import { Currency } from '../../shared/components/currency/models/currency.model';
import { IssueSharedModule } from '../../shared/issue-shared.module';
import { UtilsService } from '../../shared/services/utils.service';
import { CurrencyService } from './../../shared/components/currency/services/currency.service';
import { RefundAmount } from './models/api/refund-amount.model';
import { Refund } from './models/api/refund.model';
import { RefundComponent } from './refund.component';
import { RefundConfigurationService } from './services/refund-configuration.service';
import { RefundIndirectService } from './services/refund-indirect.service';
import { ScreenType } from '../../shared/enums/screen-type.enum';
import { MessageService } from 'primeng/components/common/messageservice';
import { RefundConfiguration } from './models/refund-configuration.model';

describe('RefundComponent', () => {
    let comp: RefundComponent;
    let fixture: ComponentFixture<RefundComponent>;

    const refundConfigurationServiceStub =
      jasmine.createSpyObj<RefundConfigurationService>('RefundConfigurationService', ['changeConfigurationByISO', 'getConfiguration']);
    refundConfigurationServiceStub.changeConfigurationByISO.and.returnValue(of());
    refundConfigurationServiceStub.getConfiguration.and.returnValue(of());

    const userServiceStub = jasmine.createSpyObj<UserService>('UserService', ['getUser']);
    userServiceStub.getUser.and.returnValue(of({
      id: '2d9429f0-fb99-4237-b2ce-bbfbb3e00ae9',
      name: 'Accelya User',
      lastname: 'Accelya Group',
      email: 'airline@accelya.com',
      isoc: 'AA',
      role: 'AGENT',
      iataCode: '78200021'
    }));

    const currencyServiceStub = jasmine.createSpyObj<CurrencyService>('CurrencyService', ['getCurrencyState']);
    currencyServiceStub.getCurrencyState.and.returnValue(of(new Currency()));

    beforeEach(() => {
        const utilsServiceStub = {
            touchAllForms: () => ({}),
            setBackErrorsOnForms: () => ({})
        };
        const refundStub = {};
        const refundIndirectServiceStub = {
            post: () => ({
                subscribe: () => ({})
            })
        };
        const translationServiceStub = {
            translate: () => ({})
        };
        const alertsServiceStub = {
            setAlertTranslate: () => ({}),
            setAlert: () => ({}),
            getDismiss: () => ({
                subscribe: () => ({
                    unsubscribe: () => ({})
                })
            })
        };
        TestBed.configureTestingModule({
          imports: [
            HttpClientModule,
            TranslationModule.forRoot(l10nConfig),
            IssueSharedModule
          ],
            declarations: [ RefundComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
              MessageService,
                { provide: CurrencyService, useValue: currencyServiceStub },
                { provide: UserService, useValue: userServiceStub },
                { provide: UtilsService, useValue: utilsServiceStub },
                { provide: Refund, useValue: refundStub },
                { provide: RefundConfigurationService, useValue: refundConfigurationServiceStub },
                { provide: RefundIndirectService, useValue: refundIndirectServiceStub },
                { provide: TranslationService, useValue: translationServiceStub },
                { provide: AlertsService, useValue: alertsServiceStub }
            ]
        });
        fixture = TestBed.createComponent(RefundComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('screen defaults to: ScreenType.CREATE', () => {
        expect(comp.screen).toEqual(ScreenType.CREATE);
    });

    it('id_refund defaults to: refund-master-container', () => {
        expect(comp.id_refund).toEqual('refund-master-container');
    });

    describe('ngOnInit', () => {
        it('makes expected calls', () => {
            comp.ngOnInit();
            expect(currencyServiceStub.getCurrencyState).toHaveBeenCalled();
            expect(refundConfigurationServiceStub.changeConfigurationByISO).toHaveBeenCalled();
        });
    });

    describe('onReturnIssue', () => {
        it('makes expected calls', () => {
            const utilsServiceStub: UtilsService = fixture.debugElement.injector.get(UtilsService);
            const alertsServiceStub: AlertsService = fixture.debugElement.injector.get(AlertsService);
            spyOn(utilsServiceStub, 'touchAllForms');
            spyOn(alertsServiceStub, 'setAlertTranslate');
            comp.onReturnIssue();
            expect(utilsServiceStub.touchAllForms).toHaveBeenCalled();
            expect(alertsServiceStub.setAlertTranslate).toHaveBeenCalled();
        });
    });

});
