// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { NO_ERRORS_SCHEMA } from '@angular/core';
// import { UserMaintenanceService } from './services/user-maintenance.service';
// import { Router } from '@angular/router';
// import { TranslationService } from 'angular-l10n';
// import { TemplateService } from '../../services/template.service';
// import { AgentService } from './../../../../shared/components/agent/services/agent.service';
// import { CountryTerritoryService } from './services/country-territory.service';
// import { UserViews } from '../../../../shared/enums/users.enum';
// import { UserComponent } from './user.component';
// import { GLOBALS } from '../../../../shared/constants/globals';

// xdescribe('UserComponent', () => {
//     let comp: UserComponent;
//     let fixture: ComponentFixture<UserComponent>;

//     beforeEach(() => {
//         const userMaintenanceServiceStub = {};
//         const routerStub = {
//             routerState: {
//                 snapshot: {
//                     url: {}
//                 }
//             }
//         };
//         const translationServiceStub = {};
//         const templateServiceStub = {};
//         const agentServiceStub = {};
//         const countryTerritoryServiceStub = {};
//         TestBed.configureTestingModule({
//             declarations: [ UserComponent ],
//             schemas: [ NO_ERRORS_SCHEMA ],
//             providers: [
//                 { provide: UserMaintenanceService, useValue: userMaintenanceServiceStub },
//                 { provide: Router, useValue: routerStub },
//                 { provide: TranslationService, useValue: translationServiceStub },
//                 { provide: TemplateService, useValue: templateServiceStub },
//                 { provide: AgentService, useValue: agentServiceStub },
//                 { provide: CountryTerritoryService, useValue: countryTerritoryServiceStub }
//             ]
//         });
//         fixture = TestBed.createComponent(UserComponent);
//         comp = fixture.componentInstance;
//     });

//     it('can load instance', () => {
//         expect(comp).toBeTruthy();
//     });

//     it('typesOfScreens defaults to: UserViews', () => {
//         expect(comp.typesOfScreens).toEqual(UserViews);
//     });

//     it('patterns defaults to: GLOBALS.HTML_PATTERN', () => {
//         expect(comp.patterns).toEqual(GLOBALS.HTML_PATTERN);
//     });

//     describe('ngOnInit', () => {
//         it('makes expected calls', () => {
//             spyOn(comp, 'loadView');
//             comp.ngOnInit();
//             expect(comp.loadView).toHaveBeenCalled();
//         });
//     });

//     describe('loadView', () => {
//         it('makes expected calls', () => {
//             spyOn(comp, 'getView');
//             comp.loadView();
//             expect(comp.getView).toHaveBeenCalled();
//         });
//     });

// });
