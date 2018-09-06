import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
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
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';

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

    const translationServiceStub = jasmine.createSpyObj<TranslationService>('TranslationService', ['translate', 'translationChanged']);
    translationServiceStub.translate.and.returnValue(of('test'));
    translationServiceStub.translationChanged.and.returnValue(of());

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
            IssueSharedModule,
            BrowserAnimationsModule,
            NoopAnimationsModule
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

        it('changeConfigurationByIso', () => {
            comp.ngOnInit();
            expect(refundConfigurationServiceStub.changeConfigurationByISO).toHaveBeenCalled();
        });

        it('changeAgentAndAirlineSubscription toBeFalsy', () => {

            const data = {
                agentVatNumberEnabled: true,
                companyRegistrationNumberEnabled: true,
                airlineVatNumberEnabled: true

            };
            refundConfigurationServiceStub.getConfiguration.and.returnValue(of(data));
            comp.ngOnInit();
            expect(refundConfigurationServiceStub.getConfiguration).toHaveBeenCalled();
            expect(comp.basicInfoRefundFormModel.agent.agentVatNumber.disabled).toBeFalsy();
            expect(comp.basicInfoRefundFormModel.agent.agentRegistrationNumber.disabled).toBeFalsy();
            expect(comp.basicInfoRefundFormModel.airline.airlineVatNumber.disabled).toBeFalsy();
            expect(comp.basicInfoRefundFormModel.airline.airlineRegistrationNumber.disabled).toBeFalsy();
        });

        it('changeAgentAndAirlineSubscription toBeTruthy', () => {

            const data = {
                agentVatNumberEnabled: false,
                companyRegistrationNumberEnabled: false,
                airlineVatNumberEnabled: false

            };
            refundConfigurationServiceStub.getConfiguration.and.returnValue(of(data));
            comp.ngOnInit();
            expect(refundConfigurationServiceStub.getConfiguration).toHaveBeenCalled();
            expect(comp.basicInfoRefundFormModel.agent.agentVatNumber.disabled).toBeTruthy();
            expect(comp.basicInfoRefundFormModel.agent.agentRegistrationNumber.disabled).toBeTruthy();
            expect(comp.basicInfoRefundFormModel.airline.airlineVatNumber.disabled).toBeTruthy();
            expect(comp.basicInfoRefundFormModel.airline.airlineRegistrationNumber.disabled).toBeTruthy();
        });


        it('resumeBarSubscriptions with real value', fakeAsync(() => {
            comp.ngOnInit();
            comp.basicInfoRefundFormModel.agent.agentCode.setValue('7820001');
            comp.basicInfoRefundFormModel.agent.agentControlDigit.setValue('1');
            comp.basicInfoRefundFormModel.airline.airlineCode.setValue('123');
            tick();
            expect((comp.elementsResumeBar[3] as any).value).toBe('78200011');
            expect((comp.elementsResumeBar[2] as any).value).toBe('123');
        }));

        it('resumeBarSubscriptions with value -', fakeAsync(() => {
            comp.ngOnInit();
            comp.basicInfoRefundFormModel.agent.agentCode.setValue('7820');
            comp.basicInfoRefundFormModel.agent.agentControlDigit.setValue('1');
            comp.basicInfoRefundFormModel.airline.airlineCode.setValue('1');
            tick();
            expect((comp.elementsResumeBar[3] as any).value).toBe('-');
            expect((comp.elementsResumeBar[2] as any).value).toBe('-');
        }));

        it('totalAmountSubscription', fakeAsync(() => {
            comp.ngOnInit();
            comp.formOfPaymentRefundFormModel.totalAmount.setValue('1');
            tick();
            expect((comp.elementsResumeBar[6] as any).value.trim()).toBe('1');
        }));

        it('currencyStateSubscription', () => {
            comp.ngOnInit();
            expect(currencyServiceStub.getCurrencyState).toHaveBeenCalled();
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
