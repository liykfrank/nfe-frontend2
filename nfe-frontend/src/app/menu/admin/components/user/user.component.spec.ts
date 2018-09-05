import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { of } from 'rxjs/observable/of';
import { asyncError } from '../../../../../testing/async-observable-helpers';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { GLOBALS } from '../../../../shared/constants/globals';
import { ROUTES } from '../../../../shared/constants/routes';
import { EnvironmentType } from '../../../../shared/enums/environment-type.enum';
import { UserViews } from '../../../../shared/enums/users.enum';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { Country } from '../../../adm-acm/models/country.model';
import { AlertsService } from './../../../../core/services/alerts.service';
import { Agent } from './../../../../shared/components/agent/models/agent.model';
import { AgentService } from './../../../../shared/components/agent/services/agent.service';
import { UtilsService } from './../../../../shared/services/utils.service';
import { TemplateService } from './../../services/template.service';
import { NewUserModel } from './models/new-user.model';
import { CountryTerritoryService } from './services/country-territory.service';
import { UserMaintenanceService } from './services/user-maintenance.service';
import { UserComponent } from './user.component';
import { ModSubUserView } from './views/mod-sub-user.view';
import { ModUserView } from './views/mod-user.view';
import { NewSubUserView } from './views/new-sub-user.view';
import { NewUserView } from './views/new-user.view';
import { UserAddress } from './models/api/user-address.model';
import { User, UserInterface } from './models/api/user.model';



/////////////////////////////////// UserComponent//////////////////////////////////////////
describe('UserComponent', () => {
  let comp: UserComponent;
  let fixture: ComponentFixture<UserComponent>;


  beforeEach(() => {
    const userMaintenanceServiceStub = {};
    const utilsServiceStub = {};
    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };
    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);
    const translationServiceStub = {};
    const templateServiceStub = {};
    const agentServiceStub = {};
    const alertServiceStub = {};
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: UserMaintenanceService,
          useValue: userMaintenanceServiceStub
        },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: AgentService, useValue: agentServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        {
          provide: CountryTerritoryService,
          useValue: countryTerritoryServiceStub
        }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
  });

  it('can load instance', () => {
    expect(comp).toBeTruthy();
  });

  it('typesOfScreens defaults to: UserViews', () => {
    expect(comp.typesOfScreens).toEqual(UserViews);
  });

  it('patterns defaults to: GLOBALS.HTML_PATTERN', () => {
    expect(comp.patterns).toEqual(GLOBALS.HTML_PATTERN);
  });

  describe('ngOnInit', () => {
    it('makes expected calls', () => {
      spyOn(comp, 'loadView');
      comp.ngOnInit();

      expect(comp.loadView).toHaveBeenCalled();
    });
  });

  describe('loadView', () => {
    it('makes expected calls', () => {
      spyOn(comp, 'setScreenType');
      comp.loadView();

      expect(comp.setScreenType).toHaveBeenCalled();
    });

    it('loads NewUserView', () => {
      comp.router.routerState.snapshot.url = ROUTES.NEW_USER.URL;

      comp.loadView();
      expect(comp.view).toEqual(jasmine.any(NewUserView));
    });

    it('loads ModUserView', () => {
      comp.router.routerState.snapshot.url = ROUTES.MOD_USER.URL;

      comp.loadView();
      expect(comp.view).toEqual(jasmine.any(ModUserView));
    });

    it('loads NewSubUserView', () => {
      comp.router.routerState.snapshot.url = ROUTES.NEW_SUB_USER.URL;

      comp.loadView();
      expect(comp.view).toEqual(jasmine.any(NewSubUserView));
    });

    it('loads ModSubUserView', () => {
      comp.router.routerState.snapshot.url = ROUTES.MOD_SUB_USER.URL;

      comp.loadView();
      expect(comp.view).toEqual(jasmine.any(ModSubUserView));
    });

  });

  it('isView(view) returns true if view is equal to screentype else return false', () => {
    comp.screenType = comp.typesOfScreens.NEW_USER;
    const view = comp.isView(comp.typesOfScreens.NEW_USER);
    expect(view).toBeTruthy();
  });

  it('setScreenType(url) else path', () => {
    comp.screenType = comp.typesOfScreens.NEW_USER;
    comp.setScreenType('');
    expect(comp.screenType).toBe(undefined);
  });

});


/////////////////////////////////// ReactiveFormHandler//////////////////////////////////////////
describe('ReactiveFormHandler', () => {
  let comp: UserComponent;
  let view: NewUserView;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const userMaintenanceServiceStub = {};
    const utilsServiceStub = {};
    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };
    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);
    const translationServiceStub = {};
    const templateServiceStub = {};
    const agentServiceStub = {};
    const alertServiceStub = {};
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: UserMaintenanceService,
          useValue: userMaintenanceServiceStub
        },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: AgentService, useValue: agentServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        {
          provide: CountryTerritoryService,
          useValue: countryTerritoryServiceStub
        }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
    comp.router.routerState.snapshot.url = ROUTES.NEW_USER.URL;
    comp.loadView();
    view = comp.view;


  });

  it('enableControl enables an AbstractControl', () => {
    const control: AbstractControl = new FormControl();
    spyOn(control, 'enable');

    view.enableControl(control);

    expect(control.enable).toHaveBeenCalledWith(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  });

  it('disableControl disables an AbstractControl', () => {
    const control: AbstractControl = new FormControl();
    spyOn(control, 'disable');

    view.disableControl(control);

    expect(control.disable).toHaveBeenCalledWith(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  });

  it('resetControl resets an AbstractControl', () => {
    const control: AbstractControl = new FormControl();
    spyOn(control, 'reset');

    view.resetControl(control);

    expect(control.reset).toHaveBeenCalledWith('', GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  });

  it('enableFormGroup enables a FormGroup without FormControls', () => {
    const formGroup: FormGroup = new FormGroup({});

    spyOn(view, 'enableControl');
    spyOn(view, 'disableControl');

    view.enableFormGroup(formGroup);

    expect(view.enableControl).toHaveBeenCalledWith(formGroup);
    expect(view.disableControl).toHaveBeenCalledTimes(0);
  });

  it('enableFormGroup enables a FormGroup and disables two of its FormControls', () => {
    const control1: AbstractControl = new FormControl();
    const control2: AbstractControl = new FormControl();
    const control3: AbstractControl = new FormControl();
    const formGroup: FormGroup = new FormGroup({ control1: control1, control2: control2, control3: control3 });

    spyOn(view, 'enableControl');
    spyOn(view, 'disableControl');

    view.enableFormGroup(formGroup, [control1, control2]);

    expect(view.enableControl).toHaveBeenCalledWith(formGroup);
    expect(view.disableControl).toHaveBeenCalledWith(control1);
    expect(view.disableControl).toHaveBeenCalledWith(control2);
    expect(view.disableControl).toHaveBeenCalledTimes(2);
  });

  it('disableFormGroup disables a FormGroup without FormControls', () => {
    const formGroup: FormGroup = new FormGroup({});

    spyOn(view, 'disableControl');
    spyOn(view, 'enableControl');

    view.disableFormGroup(formGroup);

    expect(view.disableControl).toHaveBeenCalledWith(formGroup);
    expect(view.enableControl).toHaveBeenCalledTimes(0);
  });

  it('disableFormGroup disables a FormGroup and enables two of its FormControls', () => {
    const control1: AbstractControl = new FormControl();
    const control2: AbstractControl = new FormControl();
    const control3: AbstractControl = new FormControl();
    const formGroup: FormGroup = new FormGroup({ control1: control1, control2: control2, control3: control3 });

    spyOn(view, 'disableControl');
    spyOn(view, 'enableControl');

    view.disableFormGroup(formGroup, [control1, control3]);

    expect(view.disableControl).toHaveBeenCalledWith(formGroup);
    expect(view.enableControl).toHaveBeenCalledWith(control1);
    expect(view.enableControl).toHaveBeenCalledWith(control3);
    expect(view.enableControl).toHaveBeenCalledTimes(2);
  });

});


/////////////////////////////////// ArrayTemplateModel //////////////////////////////////////////
describe('ArrayTemplateModel', () => {
  let comp: UserComponent;
  let view: NewUserView;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const userMaintenanceServiceStub = {};
    const utilsServiceStub = {};
    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };
    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);

    const templateServiceStub = jasmine.createSpyObj<TemplateService>('TemplateService', [
      'removeTemplate',
      'restoreTemplate',
      'reset'
    ]);
    const translationServiceStub = {};
    const agentServiceStub = {};
    const alertServiceStub = {};
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: UserMaintenanceService,
          useValue: userMaintenanceServiceStub
        },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: AgentService, useValue: agentServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        {
          provide: CountryTerritoryService,
          useValue: countryTerritoryServiceStub
        }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
    comp.router.routerState.snapshot.url = ROUTES.NEW_USER.URL;
    comp.loadView();
    view = comp.view;
  });

  it('getCountriesHtml() countries size > 10', async(() => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.selectedTemplate = 'Template 1';
    const event: Country[] = [];
    for (let i = 1; i <= 11; i++) {
      const country = new Country();
      country.isoCountryCode = 'AA';
      country.name = 'AA';
      event.push(country);
    }
    view.onReturnCountryTerritory(event);
    view.addTemplate();

    expect(view.templates.length).toBe(1);
    expect(view.templates[0].getCountriesHtml().length > 0).toBeTruthy();
    expect(view.templates[0].agent).toBe(false);
  }));

  it('getCountriesHtml() countries size < 10', async(() => {

    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    const event: Country[] = [];
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    event.push(country);
    view.onReturnCountryTerritory(event);
    view.addTemplate();

    expect(view.templates.length).toBe(1);
    expect(view.templates[0].getCountriesHtml().length > 0).toBeTruthy();
    expect(view.templates[0].agent).toBe(true);
  }));

});

/////////////////////////////////// NewUserViewview //////////////////////////////////////////
describe('NewUserView', () => {
  let comp: UserComponent;
  let view: NewUserView;
  let fixture: ComponentFixture<UserComponent>;
  const templateServiceStub = jasmine.createSpyObj<TemplateService>('TemplateService', [
    'removeTemplate',
    'restoreTemplate',
    'reset'
  ]);
  const translationServiceStub = jasmine.createSpyObj('TranslationService', [
    'translate',
    'translationChanged'
  ]);

  const agentServiceStub = jasmine.createSpyObj('AgentService', [
    'validateAgent'
  ]);

  const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
    'getCountriesAndTerritory',
  ]);

  const userMaintenanceServiceStub = jasmine.createSpyObj<UserMaintenanceService>('UserMaintenanceService', [
    'createUser'
  ]);

  const alertServiceStub = jasmine.createSpyObj<AlertsService>('AlertsService', [
    'setAlert'
  ]);

  const utilsServiceStub = jasmine.createSpyObj<UtilsService>('UtilsService', [
    'setBackErrorsOnForms'
  ]);
  const error_back = {
    validationErrors: [
      {
        fieldName: 'userCode',
        message: 'Airline does not exist'
      }
    ]
  };

  beforeEach(() => {
    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: UserMaintenanceService,
          useValue: userMaintenanceServiceStub
        },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: AgentService, useValue: agentServiceStub },
        {
          provide: CountryTerritoryService,
          useValue: countryTerritoryServiceStub
        }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
    comp.router.routerState.snapshot.url = ROUTES.NEW_USER.URL;
    comp.loadView();
    view = comp.view;
  });

  it('can load instance', () => {
    expect(view).toBeTruthy();
  });

  it('userValid defaults to: false', () => {
    expect(view.userValid).toEqual(false);
  });

  describe('load', () => {
    it('makes the expected calls', () => {
      spyOn(view, 'loadControlForCountryTerritory');
      spyOn(view, 'disableFormGroup');
      templateServiceStub.reset.and.callThrough();

      view._model = new NewUserModel();
      view.load();

      expect(view.loadControlForCountryTerritory).toHaveBeenCalled();
      expect(view.disableFormGroup).toHaveBeenCalledWith(view.model.groupForm, [view.model.userType]);
    });

    it('disables the whole form except userType and userCode when the value of userType changes and reset the form', fakeAsync(() => {
      const userTypeExpected = 'test';
      const model = new NewUserModel();

      spyOn(view, 'loadControlForCountryTerritory');
      spyOn(view, 'disableFormGroup');
      spyOn(view, 'resetControl');

      templateServiceStub.reset.and.callThrough();
      translationServiceStub.translate.and.returnValue(userTypeExpected);
      translationServiceStub.translationChanged.and.callThrough();

      view._model = model;
      view.load();
      model.userType.setValue(userTypeExpected);
      tick();

      expect(view.disableFormGroup).toHaveBeenCalledWith(model.groupForm, [model.userType, model.userCode]);
      expect(view.resetControl).toHaveBeenCalledWith(model.groupForm);
      expect(view.userValid).toBeFalsy();
      expect(view.templates).toEqual([]);
      expect(view.isoCountry.value).toBe('');
      expect(view.countries).toEqual([]);
      expect(model.userType.value).toBe(userTypeExpected);
    }));

    it('calls switchUserType when userCode changes', fakeAsync(() => {
      const userCode = 'test';
      spyOn(view, 'switchUserType');

      view._model.userCode.setValue(userCode);
      tick();

      expect(view.switchUserType).toHaveBeenCalled();
    }));
  });

  describe('switchUserType', () => {
    it('calls caseUserTypeIsAgent when there is userCode and it is valid and userType is an agent', () => {
      const model = new NewUserModel();
      spyOn(view, 'caseUserTypeIsAgent');

      model.userCode.setValue('1111111');
      model.userType.setValue(GLOBALS.TYPES_OF_USER.AGENT);
      view._model = model;
      view.switchUserType();

      expect(view.caseUserTypeIsAgent).toHaveBeenCalledWith(model.userCode.value);
    });

    it('calls caseUserTypeIsAirline when there is userCode and it is valid and userType is an airline', () => {
      const model = new NewUserModel();
      spyOn(view, 'caseUserTypeIsAirline');

      model.userCode.setValue('111');
      model.userType.setValue(GLOBALS.TYPES_OF_USER.AIRLINE);
      view._model = model;
      view.switchUserType();

      expect(view.caseUserTypeIsAirline).toHaveBeenCalledWith(model.userCode.value);
    });

    it('calls caseUserTypeIsGds when there is userCode and it is valid and it is valid and userType is a GDS', () => {
      const model = new NewUserModel();
      spyOn(view, 'caseUserTypeIsGds');

      model.userCode.setValue('test');
      model.userType.setValue(GLOBALS.TYPES_OF_USER.GDS);
      view._model = model;
      view.switchUserType();

      expect(view.caseUserTypeIsGds).toHaveBeenCalledWith(model.userCode.value);
    });

    it('calls caseUserTypeIsOther when there is userCode and it is valid and it is valid and userType is other', () => {
      const model = new NewUserModel();
      spyOn(view, 'caseUserTypeIsOther');

      model.userCode.setValue('test');
      model.userType.setValue(GLOBALS.TYPES_OF_USER.IATA);
      view._model = model;
      view.switchUserType();

      expect(view.caseUserTypeIsOther).toHaveBeenCalledWith(model.userCode.value);
    });

    it('disables the form except usertype and userCode and defaults it and reset isoCountry', fakeAsync(() => {
      const model = new NewUserModel();
      const isoCountry = new FormControl('test');
      spyOn(view, 'disableFormGroup');
      spyOn(view, 'resetControl');

      view.isoCountry = isoCountry;
      view._model = model;
      view.switchUserType();

      expect(view.resetControl).toHaveBeenCalledWith(isoCountry);
      expect(view.disableFormGroup).toHaveBeenCalledWith(model.groupForm, [model.userType, model.userCode]);
      expect(view.userValid).toBeFalsy();
      expect(view.templates).toEqual([]);
      expect(view.isoCountry.value).toBe('');
      expect(view.countries).toEqual([]);
    }));

  });

  describe('caseUserTypeIsAgent', () => {
    it('calls validateAgent from AgentService with a valid agent', () => {
      const userCode = '0000001';
      const agentMock: Agent = new Agent();
      const model: NewUserModel = new NewUserModel();
      agentMock.isoCountryCode = 'tt';
      agentServiceStub.validateAgent.and.returnValue(of(agentMock));
      spyOn(view, 'enableFormGroup');

      view._model = model;
      view.caseUserTypeIsAgent(userCode);

      expect(agentServiceStub.validateAgent).toHaveBeenCalledWith(EnvironmentType.MASTER_AGENT, view.agentCodeCd(userCode));
      expect(view.enableFormGroup).toHaveBeenCalledWith(model.groupForm);
      expect(view.userValid).toBeTruthy();
      expect(view.isoCountry.value).toBe(agentMock.isoCountryCode);
    });

    it('calls validateAgent from AgentService with an invalid agent', fakeAsync(() => {
      const userCode = '0000001';
      const model: NewUserModel = new NewUserModel();
      const error: HttpErrorResponse = new HttpErrorResponse({
      });

      agentServiceStub.validateAgent.and.returnValue(asyncError(error));
      spyOn(view, 'disableFormGroup');
      spyOn(view, 'resetControl');

      view._model = model;
      view.isoCountry = new FormControl('tt');
      view.caseUserTypeIsAgent(userCode);
      tick();


      expect(view.disableFormGroup).toHaveBeenCalledWith(model.groupForm, [model.userType, model.userCode]);
      expect(view.resetControl).toHaveBeenCalledWith(view.isoCountry);
      expect(view.userValid).toBeFalsy();
    }));


    it('caseUserTypeIsAirline enables the form', () => {
      const userCode = '111';
      spyOn(view, 'enableFormGroup');

      view.caseUserTypeIsAirline(userCode);

      expect(view.userValid).toBeTruthy();
      expect(view.enableFormGroup).toHaveBeenCalled();
      expect(countryTerritoryServiceStub.getCountriesAndTerritory).toHaveBeenCalled();
    });

    ///////////////////////////////////////////////// new
    it('caseUserTypeIsAirline userValid is false', () => {
      const userCode = '';
      view.caseUserTypeIsAirline(userCode);
      expect(view.userValid).toBeFalsy();
    });

    it('caseUserTypeIsGds enables the form', () => {
      const userCode = 'test';
      const model: NewUserModel = new NewUserModel();
      spyOn(view, 'enableFormGroup');

      view.caseUserTypeIsGds(userCode);

      expect(view.userValid).toBeTruthy();
      expect(view.enableFormGroup).toHaveBeenCalled();
      expect(countryTerritoryServiceStub.getCountriesAndTerritory).toHaveBeenCalled();
    });

    ////////////////////////////////////////////// new
    it('caseUserTypeIsGds userValid is false', () => {
      const userCode = '';
      view.caseUserTypeIsGds(userCode);
      expect(view.userValid).toBeFalsy();
    });

    it('caseUserTypeIsOther enables the form', () => {
      const userCode = 'test';
      spyOn(view, 'enableFormGroup');

      view.caseUserTypeIsOther(userCode);

      expect(view.userValid).toBeTruthy();
      expect(view.enableFormGroup).toHaveBeenCalled();
      expect(countryTerritoryServiceStub.getCountriesAndTerritory).toHaveBeenCalled();
    });

    ///////////////////////////////////////////// new
    it('caseUserTypeIsOther userValid is false', () => {
      const userCode = '';
      view.caseUserTypeIsOther(userCode);
      expect(view.userValid).toBeFalsy();
    });

  });

  describe('callApi', () => {
    it('calls createUser from UserMaintenanceService with a valid User (Agent)', fakeAsync(() => {
      const model: NewUserModel = new NewUserModel();
      model.email.setValue('test@test.com');
      model.lastname.setValue('test');
      model.name.setValue('test');
      model.organization.setValue('test');
      model.telephone.setValue('12345678');
      model.userCode.setValue('12345678');
      model.userType.setValue(view.types_of_users.AGENT);
      model.username.setValue('test@test.com');
      const expectedUser: UserInterface = new User(
        new UserAddress('', '', '', '', ''),
        model.email.value,
        '',
        '',
        model.lastname.value,
        model.name.value,
        model.organization.value,
        model.registerDate.value,
        model.telephone.value,
        view.agentCodeCd(model.userCode.value),
        model.userType.value.toUpperCase(),
        model.username.value
      );
      userMaintenanceServiceStub.createUser.and.returnValue(of({}));

      view._model = model;
      view.callApi();
      tick();

      //expect(expectedUser).toEqual();
      expect(userMaintenanceServiceStub.createUser).toHaveBeenCalledWith(expectedUser);
    }));

    // it('calls createUser from UserMaintenanceService with an invalid User', fakeAsync(() => {
    //   userMaintenanceServiceStub.createUser.and.returnValue(asyncError(new HttpErrorResponse({})));

    //   view.callApi();
    //   tick();
    // }));

    // it('calls validateAgent from AgentService with an invalid agent', fakeAsync(() => {
    //   const userCode = '0000001';
    //   const model: NewUserModel = new NewUserModel();
    //   const error: HttpErrorResponse = new HttpErrorResponse({
    //   });

    //   agentServiceStub.validateAgent.and.returnValue(asyncError(error));
    //   spyOn(view, 'disableFormGroup');
    //   spyOn(view, 'resetControl');

    //   view._model = model;
    //   view.isoCountry = new FormControl('tt');
    //   view.caseUserTypeIsAgent(userCode);
    //   tick();


    //   expect(view.disableFormGroup).toHaveBeenCalledWith(model.groupForm, [model.userType, model.userCode]);
    //   expect(view.resetControl).toHaveBeenCalledWith(view.isoCountry);
    //   expect(view.userValid).toBeFalsy();
    // }));

  });










































































































































































































































































































  /////////////////////////////////////////////////// Prohibido escribir zona Felipe //////////////////////////////////////////////////////////////

  it('ngOnDestroy()', async(() => {
    view.ngOnDestroy();
    expect(view.subsType).toBe(undefined);
  }));

  it('checkType(type) returns true if type is equal to model.userType else return false', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    const type = view.checkType(view.types_of_users.AIRLINE);
    expect(type).toBeTruthy();
  });

  it('toggleEdit()', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    view.isoCountry.setValue('AA');
    view.addTemplate();
    view.toogleEdit(0);
    expect(view.templates[0].modeEdit).toBeTruthy();
  });

  it('showTemplate()', () => {
    const showTemplate = view.showTemplate();
    const userValid = view.userValid;
    expect(showTemplate).toBe(userValid);
  });

  it('onSelectedTemplate change value', () => {
    view.onSelectedTemplate('prueba');
    expect(view.selectedTemplate).toEqual('prueba');
  });

  it('add template with arr countries', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.selectedTemplate = 'Template 1';
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(1);
    expect(view.selectedTemplate).toBe(undefined);
    expect(view.templateService.removeTemplate).toHaveBeenCalled();
    expect(view.countries.length).toBe(0);
  });

  it('add template with isoCountry', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    view.isoCountry.setValue('AA');
    view.addTemplate();
    expect(view.templates.length).toBe(1);
    expect(view.selectedTemplate).toBe(undefined);
    expect(view.templateService.removeTemplate).toHaveBeenCalled();
    expect(view.countries.length).toBe(0);
  });

  it('add template with arr countries else path', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(0);
  });

  it('removeTemplate', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    view.isoCountry.setValue('AA');
    view.addTemplate();
    const sizeTemplate = view.templates.length;
    view.removeTemplate(0);
    expect(view.templateService.restoreTemplate).toHaveBeenCalled();
    expect(view.templates.length).toBe(sizeTemplate - 1);
  });

  it('removeTemplate else path', () => {
    view.removeTemplate(0);
    expect(view.templates.length).toBe(0);
  });

  it('isUserCodeValidate()', () => {
    const isUserCodeValidate = view.isUserCodeValidate();
    const userValid = view.userValid;
    expect(isUserCodeValidate).toBe(userValid);
  });

  it('isAgent()', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    const isAgent = view.isAgent();
    expect(isAgent).toBeTruthy();
  });

  it('getTypeOfUsersArray()', () => {
    const arr_types_transformed = Object.values(view._types_of_users);
    const getTypeOfUsersArray = view.getTypeOfUsersArray();
    expect(getTypeOfUsersArray).toEqual(arr_types_transformed);
  });

  it('getTextButton() return string', () => {
    const text = view.getTextButton();
    async(() => {
      expect(text.lenght > 0).toBeTruthy();
    });
  });

  it('setErrors()', () => {
    utilsServiceStub.setBackErrorsOnForms.and.returnValue(error_back.validationErrors[0]);
    const setErrors = view.setErrors(error_back);
    expect(utilsServiceStub.setBackErrorsOnForms).toHaveBeenCalled();
    expect(translationServiceStub.translate).toHaveBeenCalled();
    expect(alertServiceStub.setAlert).toHaveBeenCalled();
    expect(setErrors).toBeTruthy();
  });


  it('setErrors() with message', () => {
    utilsServiceStub.setBackErrorsOnForms.and.returnValue([error_back.validationErrors[0]]);
    const setErrors = view.setErrors(error_back);
    expect(utilsServiceStub.setBackErrorsOnForms).toHaveBeenCalled();
    expect(translationServiceStub.translate).toHaveBeenCalled();
    expect(alertServiceStub.setAlert).toHaveBeenCalled();
    expect(setErrors).toBeTruthy();
  });

  it('setErrors() else path', () => {
    const setErrors = view.setErrors('');
    expect(setErrors).toBeFalsy();
  });

});

/////////////////////////////////// ModUserViewview //////////////////////////////////////////
describe('ModUserView', () => {
  let comp: UserComponent;
  let view: ModUserView;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const userMaintenanceServiceStub = {};
    const alertServiceStub = {};
    const utilsServiceStub = {};

    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };

    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);

    const translationServiceStub = jasmine.createSpyObj<TranslationService>('TranslationService', [
      'translate',
      'translationChanged'
    ]);

    const templateServiceStub = jasmine.createSpyObj<TemplateService>('TemplateService', [
      'removeTemplate',
      'restoreTemplate'
    ]);

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: UserMaintenanceService, useValue: userMaintenanceServiceStub },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: CountryTerritoryService, useValue: countryTerritoryServiceStub }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
    comp.router.routerState.snapshot.url = ROUTES.MOD_USER.URL;
    comp.loadView();
    view = comp.view;
  });

  it('patterns have value', () => {
    expect(comp.patterns).toEqual(GLOBALS.HTML_PATTERN);
  });

  it('typesOfScreens have value', () => {
    expect(comp.typesOfScreens).toEqual(UserViews);
  });

  it('onSelectedTemplate change value', () => {
    view.onSelectedTemplate('prueba');
    expect(view.selectedTemplate).toEqual('prueba');
  });

  describe('load', () => {

    it('makes the expected calls', () => {
      spyOn(view, 'loadControlForCountryTerritory');
      spyOn(view, 'disableControl');
      view.load();
      expect(view.loadControlForCountryTerritory).toHaveBeenCalled();
      expect(view.disableControl).toHaveBeenCalled();
    });

  });

  it('add template with arr countries', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.selectedTemplate = 'Template 1';
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(1);
    expect(view.selectedTemplate).toBe(undefined);
    expect(view.templateService.removeTemplate).toHaveBeenCalled();
    expect(view.countries.length).toBe(0);
  });

  it('add template with isoCountry', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    view.isoCountry.setValue('AA');
    view.addTemplate();
    expect(view.templates.length).toBe(1);
    expect(view.selectedTemplate).toBe(undefined);
    expect(view.templateService.removeTemplate).toHaveBeenCalled();
    expect(view.countries.length).toBe(0);
  });

  it('add template with arr countries else path', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(0);
  });

  it('removeTemplate', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    view.isoCountry.setValue('AA');
    view.addTemplate();
    const sizeTemplate = view.templates.length;
    view.removeTemplate(0);
    expect(view.templateService.restoreTemplate).toHaveBeenCalled();
    expect(view.templates.length).toBe(sizeTemplate - 1);
  });

  it('removeTemplate else path', () => {
    view.removeTemplate(0);
    expect(view.templates.length).toBe(0);
  });

  it('onReturnCountryTerritory', () => {
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    expect(view.countries.length > 0).toBeTruthy();
  });

  it('toggleEdit()', () => {
    view._model.userType.setValue(view.types_of_users.AGENT);
    view.selectedTemplate = 'Template 1';
    view.isoCountry.setValue('AA');
    view.addTemplate();
    view.toogleEdit(0);
    expect(view.templates[0].modeEdit).toBeTruthy();
  });

  it('showTemplate()', () => {
    const showTemplate = view.showTemplate();
    expect(showTemplate).toBeTruthy();
  });

  it('getTextButton() return string', () => {
    const text = view.getTextButton();
    async(() => {
      expect(text.lenght > 0).toBeTruthy();
    });
  });

  it('callApi()', () => {
    const call = view.callApi();
    expect(call).toBeTruthy();
  });

  it('get model()', () => {
    const m = view.model;
    expect(m).toBeTruthy();
  });

  it('get types_of_users()', () => {
    const t = view.types_of_users;
    expect(t).toBeTruthy();
    expect(t).toEqual(GLOBALS.TYPES_OF_USER);
  });

});


/////////////////////////////////// NewSubUserViewview //////////////////////////////////////////
describe('NewSubUserView', () => {
  let comp: UserComponent;
  let view: NewSubUserView;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const userMaintenanceServiceStub = {};
    const alertServiceStub = {};
    const utilsServiceStub = {};

    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };

    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);

    const translationServiceStub = jasmine.createSpyObj<TranslationService>('TranslationService', [
      'translate',
      'translationChanged'
    ]);

    const templateServiceStub = jasmine.createSpyObj<TemplateService>('TemplateService', [
      'removeTemplate',
      'restoreTemplate'
    ]);

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: UserMaintenanceService, useValue: userMaintenanceServiceStub },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: CountryTerritoryService, useValue: countryTerritoryServiceStub }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
    comp.router.routerState.snapshot.url = ROUTES.NEW_SUB_USER.URL;
    comp.loadView();
    view = comp.view;
  });

  it('can load instance', () => {
    expect(view).toBeTruthy();
  });

  it('patterns have value', () => {
    expect(comp.patterns).toEqual(GLOBALS.HTML_PATTERN);
  });

  it('typesOfScreens have value', () => {
    expect(comp.typesOfScreens).toEqual(UserViews);
  });

  it('onSelectedTemplate change value', () => {
    view.onSelectedTemplate('prueba');
    expect(view.selectedTemplate).toEqual('prueba');
  });

  describe('load', () => {

    it('makes the expected calls', () => {
      spyOn(view, 'loadControlForCountryTerritory');
      view.load();
      expect(view.loadControlForCountryTerritory).toHaveBeenCalled();
    });

  });

  it('add template with arr countries', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.selectedTemplate = 'Template 1';
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(1);
    expect(view.selectedTemplate).toBe(undefined);
    expect(view.templateService.removeTemplate).toHaveBeenCalled();
    expect(view.countries.length).toBe(0);
  });


  it('add template with arr countries else path', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(0);
  });

  it('removeTemplate', () => {
    view.selectedTemplate = 'Template 1';
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    const sizeTemplate = view.templates.length;
    view.removeTemplate(0);
    expect(view.templates.length).toBe(sizeTemplate - 1);
  });

  it('removeTemplate else path', () => {
    view.removeTemplate(0);
    expect(view.templates.length).toBe(0);
  });

  it('onReturnCountryTerritory', () => {
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    expect(view.countries.length > 0).toBeTruthy();
  });

  it('toggleEdit()', () => {
    view.selectedTemplate = 'Template 1';
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    view.toogleEdit(0);
    expect(view.templates[0].modeEdit).toBeTruthy();
  });

  it('showTemplate()', () => {
    const showTemplate = view.showTemplate();
    expect(showTemplate).toBeTruthy();
  });

  it('getTextButton() return string', () => {
    const text = view.getTextButton();
    async(() => {
      expect(text.lenght > 0).toBeTruthy();
    });
  });

  it('callApi()', () => {
    const call = view.callApi();
    expect(call).toBeTruthy();
  });

  it('get model()', () => {
    const m = view.model;
    expect(m).toBeTruthy();
  });

  it('get types_of_users()', () => {
    const t = view.types_of_users;
    expect(t).toBeTruthy();
    expect(t).toEqual(GLOBALS.TYPES_OF_USER);
  });

});


/////////////////////////////////// ModSubUserViewview //////////////////////////////////////////
describe('ModSubUserView', () => {
  let comp: UserComponent;
  let view: ModSubUserView;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const userMaintenanceServiceStub = {};
    const alertServiceStub = {};
    const utilsServiceStub = {};

    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };

    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);

    const translationServiceStub = jasmine.createSpyObj<TranslationService>('TranslationService', [
      'translate',
      'translationChanged'
    ]);

    const templateServiceStub = jasmine.createSpyObj<TemplateService>('TemplateService', [
      'removeTemplate',
      'restoreTemplate'
    ]);

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        IssueSharedModule
      ],
      declarations: [UserComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        { provide: UserMaintenanceService, useValue: userMaintenanceServiceStub },
        { provide: Router, useValue: routerStub },
        { provide: TranslationService, useValue: translationServiceStub },
        { provide: TemplateService, useValue: templateServiceStub },
        { provide: AlertsService, useValue: alertServiceStub },
        { provide: UtilsService, useValue: utilsServiceStub },
        { provide: CountryTerritoryService, useValue: countryTerritoryServiceStub }
      ]
    });
    fixture = TestBed.createComponent(UserComponent);
    comp = fixture.componentInstance;
    comp.router.routerState.snapshot.url = ROUTES.MOD_SUB_USER.URL;
    comp.loadView();
    view = comp.view;
  });

  it('can load instance', () => {
    expect(view).toBeTruthy();
  });

  it('patterns have value', () => {
    expect(comp.patterns).toEqual(GLOBALS.HTML_PATTERN);
  });

  it('typesOfScreens have value', () => {
    expect(comp.typesOfScreens).toEqual(UserViews);
  });

  it('onSelectedTemplate change value', () => {
    view.onSelectedTemplate('prueba');
    expect(view.selectedTemplate).toEqual('prueba');
  });

  describe('load', () => {

    it('makes the expected calls', () => {
      spyOn(view, 'loadControlForCountryTerritory');
      view.load();
      expect(view.loadControlForCountryTerritory).toHaveBeenCalled();
    });

  });

  it('add template with arr countries', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.selectedTemplate = 'Template 1';
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(1);
    expect(view.selectedTemplate).toBe(undefined);
    expect(view.templateService.removeTemplate).toHaveBeenCalled();
    expect(view.countries.length).toBe(0);
  });


  it('add template with arr countries else path', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE);
    view.loadControlForCountryTerritory();
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    expect(view.templates.length).toBe(0);
  });

  it('removeTemplate', () => {
    view.selectedTemplate = 'Template 1';
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    const sizeTemplate = view.templates.length;
    view.removeTemplate(0);
    expect(view.templates.length).toBe(sizeTemplate - 1);
  });

  it('removeTemplate else path', () => {
    view.removeTemplate(0);
    expect(view.templates.length).toBe(0);
  });

  it('onReturnCountryTerritory', () => {
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    expect(view.countries.length > 0).toBeTruthy();
  });

  it('toggleEdit()', () => {
    view.selectedTemplate = 'Template 1';
    const country = new Country();
    country.isoCountryCode = 'AA';
    country.name = 'AA';
    const event = [country];
    view.onReturnCountryTerritory(event);
    view.addTemplate();
    view.toogleEdit(0);
    expect(view.templates[0].modeEdit).toBeTruthy();
  });

  it('showTemplate()', () => {
    const showTemplate = view.showTemplate();
    expect(showTemplate).toBeTruthy();
  });

  it('getTextButton() return string', () => {
    const text = view.getTextButton();
    async(() => {
      expect(text.lenght > 0).toBeTruthy();
    });
  });

  it('callApi()', () => {
    const call = view.callApi();
    expect(call).toBeTruthy();
  });

  it('get model()', () => {
    const m = view.model;
    expect(m).toBeTruthy();
  });

  it('get types_of_users()', () => {
    const t = view.types_of_users;
    expect(t).toBeTruthy();
    expect(t).toEqual(GLOBALS.TYPES_OF_USER);
  });

  it('ngOnDestroy()', () => {
    view.ngOnDestroy();
    expect(view.subsActive).toBe(undefined);
  });

  it('loadSwitchActive disable active', () => {
    view.activeControl.get('active').setValue(true);
    expect(view._model.expiryDate.disabled).toBeTruthy();

  });

  it('loadSwitchActive enable active', () => {
    view.activeControl.get('active').setValue(false);
    expect(view._model.expiryDate.disabled).toBeFalsy();

  });

});
