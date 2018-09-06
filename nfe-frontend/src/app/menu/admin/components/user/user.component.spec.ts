import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { of } from 'rxjs/observable/of';
import { l10nConfig } from '../../../../shared/base/conf/l10n.config';
import { GLOBALS } from '../../../../shared/constants/globals';
import { ROUTES } from '../../../../shared/constants/routes';
import { UserViews } from '../../../../shared/enums/users.enum';
import { IssueSharedModule } from '../../../../shared/issue-shared.module';
import { Country } from '../../../adm-acm/models/country.model';
import { AlertsService } from './../../../../core/services/alerts.service';
import { AgentService } from './../../../../shared/components/agent/services/agent.service';
import { UtilsService } from './../../../../shared/services/utils.service';
import { TemplateService } from './../../services/template.service';
import { UserAddress } from './models/api/user-address.model';
import { User, UserInterface } from './models/api/user.model';
import { CountryTerritoryService } from './services/country-territory.service';
import { UserMaintenanceService } from './services/user-maintenance.service';
import { UserComponent } from './user.component';
import { ModSubUserView } from './views/mod-sub-user.view';
import { ModUserView } from './views/mod-user.view';
import { NewSubUserView } from './views/new-sub-user.view';
import { NewUserView } from './views/new-user.view';

/////////////////////////////////// UserComponent //////////////////////////////////////////
describe('UserComponent', () => {
  let comp: UserComponent;
  let fixture: ComponentFixture<UserComponent>;

  const userMaintenanceServiceStub = jasmine.createSpyObj<UserMaintenanceService>('UserMaintenanceService', [
    'getUser',
  ]);

  const translationServiceStub = jasmine.createSpyObj('TranslationService', [
    'translate',
  ]);

  const agentMock: UserInterface = new User(
    new UserAddress('', '', '', '', ''), '', '', '', '', '', '', '', '', '', '', ''
  );

  userMaintenanceServiceStub.getUser.and.returnValue(of(agentMock));

  beforeEach(() => {
    const countryTerritoryServiceStub = jasmine.createSpyObj<CountryTerritoryService>('CountryTerritoryService', [
      'getCountriesAndTerritory',
    ]);

    const utilsServiceStub = {};
    const routerStub = {
      routerState: {
        snapshot: {
          url: {}
        }
      }
    };
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
      userMaintenanceServiceStub.getUser.and.returnValue(of(agentMock));
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
      userMaintenanceServiceStub.getUser.and.returnValue(of(agentMock));
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
    expect(comp.screenType).toBe(comp.typesOfScreens.NEW_USER);
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
    view._model.userType.setValue(view.types_of_users.AIRLINE, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
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

    view._model.userType.setValue(view.types_of_users.AGENT,  GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
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
