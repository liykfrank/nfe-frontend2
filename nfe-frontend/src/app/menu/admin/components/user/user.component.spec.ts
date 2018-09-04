import { TemplateService } from './../../services/template.service';
import { ArrayTemplateModel } from './models/array-template.model';
import { UtilsService } from './../../../../shared/services/utils.service';
import { AlertsService } from './../../../../core/services/alerts.service';
import { FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { NewUserModel } from './models/new-user.model';
import { ModSubUserView } from './views/mod-sub-user.view';
import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed, fakeAsync, tick, async } from '@angular/core/testing';
import { Router } from '@angular/router';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { GLOBALS } from '../../../../shared/constants/globals';
import { UserViews } from '../../../../shared/enums/users.enum';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { AgentService } from './../../../../shared/components/agent/services/agent.service';
import { CountryTerritoryService } from './services/country-territory.service';
import { UserMaintenanceService } from './services/user-maintenance.service';
import { UserComponent } from './user.component';
import { ROUTES } from '../../../../shared/constants/routes';
import { NewUserView } from './views/new-user.view';
import { ModUserView } from './views/mod-user.view';
import { NewSubUserView } from './views/new-sub-user.view';
import { ArrayObservable } from 'rxjs/observable/ArrayObservable';
import { detectChanges } from '@angular/core/src/render3';
import { Country } from '../../../adm-acm/models/country.model';


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


/////////////////////////////////// NewUserViewview //////////////////////////////////////////
describe('NewUserView', () => {
  let comp: UserComponent;
  let view: NewUserView;
  let fixture: ComponentFixture<UserComponent>;

  beforeEach(() => {
    const userMaintenanceServiceStub = {};
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
    const utilsServiceStub = {};
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

  it('_model defaults to: new NewUserModel()', () => {
    expect(comp.patterns).toEqual(GLOBALS.HTML_PATTERN);
  });

  describe('load', () => {
    it('makes the expected calls', () => {
      spyOn(view, 'loadControlForCountryTerritory');
      spyOn(view, 'disableFormGroup');

      view.load();

      expect(view.loadControlForCountryTerritory).toHaveBeenCalled();
      expect(view.disableFormGroup).toHaveBeenCalled();
      // expect(view.resetControl).toHaveBeenCalled();
      // expect(view.disableControl).toHaveBeenCalled();
      // expect(view.switchUserType).toHaveBeenCalled();
    });

    // it('disable the whole form except user type and user code', () => {
    //   let modelExpected = new NewUserModel();
    //   //view.disableFormGroup(modelExpected.groupForm);

    //   //view.load();
    //   console.log(view._model.groupForm.value);
    //   expect(view._model.groupForm.value).toEqual(modelExpected.groupForm.value);
    // });
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
