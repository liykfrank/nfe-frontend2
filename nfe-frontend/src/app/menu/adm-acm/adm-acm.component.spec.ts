import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { InputAmountServer } from './models/input-amount-server.model';
import { Router } from '@angular/router';
import { TranslationService } from 'angular-l10n';
import { AlertsService } from './../../core/services/alerts.service';
import { UtilsService } from './../../shared/services/utils.service';
import { Acdm } from './models/acdm.model';
import { AcdmsService } from './services/acdms.service';
import { AcdmConfigurationService } from './services/acdm-configuration.service';
import { CountryService } from './services/country.service';
import { AdmAcmComponent } from './adm-acm.component';
import { ScreenType } from '../../shared/enums/screen-type.enum';

describe('AdmAcmComponent', () => {
    let comp: AdmAcmComponent;
    let fixture: ComponentFixture<AdmAcmComponent>;

    beforeEach(() => {
        const inputAmountServerStub = {
            commission: {},
            fare: {},
            spam: {},
            tax: {},
            taxOnCommission: {}
        };
        const routerStub = {
            routerState: {
                snapshot: {
                    url: {}
                }
            }
        };
        const translationServiceStub = {
            translate: () => ({})
        };
        const alertsServiceStub = {
            setAlertTranslate: () => ({}),
            setAlert: () => ({}),
            getAccept: () => ({
                subscribe: () => ({
                    unsubscribe: () => ({})
                })
            }),
            getDismiss: () => ({
                subscribe: () => ({
                    unsubscribe: () => ({})
                })
            })
        };
        const utilsServiceStub = {
            touchAllForms: () => ({}),
            setBackErrorsOnForms: () => ({})
        };
        const acdmStub = {
            regularized: {}
        };
        const acdmsServiceStub = {
            postAcdm: () => ({
                subscribe: () => ({})
            })
        };
        const acdmConfigurationServiceStub = {};
        const countryServiceStub = {
            get: () => ({
                subscribe: () => ({})
            })
        };
        TestBed.configureTestingModule({
            declarations: [ AdmAcmComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
                { provide: InputAmountServer, useValue: inputAmountServerStub },
                { provide: Router, useValue: routerStub },
                { provide: TranslationService, useValue: translationServiceStub },
                { provide: AlertsService, useValue: alertsServiceStub },
                { provide: UtilsService, useValue: utilsServiceStub },
                { provide: Acdm, useValue: acdmStub },
                { provide: AcdmsService, useValue: acdmsServiceStub },
                { provide: AcdmConfigurationService, useValue: acdmConfigurationServiceStub },
                { provide: CountryService, useValue: countryServiceStub }
            ]
        });
        fixture = TestBed.createComponent(AdmAcmComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('screen defaults to: ScreenType.CREATE', () => {
        expect(comp.screen).toEqual(ScreenType.CREATE);
    });

    it('id_acdm defaults to: acdm-master-container', () => {
        expect(comp.id_acdm).toEqual('acdm-master-container');
    });

    it('ticketDocuments defaults to: []', () => {
        expect(comp.ticketDocuments).toEqual([]);
    });

    describe('onReturnIssue', () => {
        it('makes expected calls', () => {
            const alertsServiceStub: AlertsService = fixture.debugElement.injector.get(AlertsService);
            const utilsServiceStub: UtilsService = fixture.debugElement.injector.get(UtilsService);
            spyOn(alertsServiceStub, 'setAlertTranslate');
            spyOn(utilsServiceStub, 'touchAllForms');
            comp.onReturnIssue();
            expect(alertsServiceStub.setAlertTranslate).toHaveBeenCalled();
            expect(utilsServiceStub.touchAllForms).toHaveBeenCalled();
        });
    });

});
