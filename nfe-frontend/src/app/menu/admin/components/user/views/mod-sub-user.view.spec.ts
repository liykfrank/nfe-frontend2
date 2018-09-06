import { HttpClientModule } from '@angular/common/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { TranslationModule, TranslationService } from 'angular-l10n';
import { of } from 'rxjs/observable/of';
import { AlertsService } from '../../../../../core/services/alerts.service';
import { l10nConfig } from '../../../../../shared/base/conf/l10n.config';
import { GLOBALS } from '../../../../../shared/constants/globals';
import { IssueSharedModule } from '../../../../../shared/issue-shared.module';
import { UtilsService } from '../../../../../shared/services/utils.service';
import { Country } from '../../../../adm-acm/models/country.model';
import { TemplateService } from '../../../services/template.service';
import { UserAddress } from '../models/api/user-address.model';
import { User, UserInterface } from '../models/api/user.model';
import { NewUserModel } from '../models/new-user.model';
import { CountryTerritoryService } from '../services/country-territory.service';
import { UserMaintenanceService } from '../services/user-maintenance.service';
import { UserComponent } from '../user.component';
import { ROUTES } from './../../../../../shared/constants/routes';
import { ModSubUserView } from './mod-sub-user.view';

/////////////////////////////////// ModSubUserViewview //////////////////////////////////////////
describe('ModSubUserView', () => {
  let comp: UserComponent;
  let view: ModSubUserView;
  let fixture: ComponentFixture<UserComponent>;

  const userMock: UserInterface = new User(
    new UserAddress('city', 'country', 'description', 'locality', 'zip'),
    'email',
    'expiryDate',
    'lastModificationDate',
    'lastName',
    'name',
    'organization',
    'registerDate',
    'telephone',
    'userCode',
    'userType',
    'username'
  );

  const userMaintenanceServiceStub = jasmine.createSpyObj<UserMaintenanceService>('UserMaintenanceService', [
    'getUser',
  ]);

  beforeEach(() => {
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

    userMaintenanceServiceStub.getUser.and.returnValue(of(userMock));

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

  it('onSelectedTemplate change value', () => {
    view.onSelectedTemplate('prueba');
    expect(view.selectedTemplate).toEqual('prueba');
  });

  describe('load', () => {

    it('makes the expected calls', () => {
      spyOn(view, 'loadControlForCountryTerritory');
      spyOn(view, 'loadSwitchActive');
      view.load();
      expect(view.loadControlForCountryTerritory).toHaveBeenCalled();
      expect(view.loadSwitchActive).toHaveBeenCalled();
    });

  });

  it('populateForm populates the form with a user', fakeAsync(() => {
    const model = new NewUserModel();
    model.name.setValue(userMock.name, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.lastname.setValue(userMock.lastName, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.username.setValue(userMock.username, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.email.setValue(userMock.email, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.organization.setValue(userMock.organization, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.locality.setValue(userMock.address.locality, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.city.setValue(userMock.address.city, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.zip.setValue(userMock.address.zip, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.country.setValue(userMock.address.country, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    model.telephone.setValue(userMock.telephone, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    spyOn(view, 'loadControlForCountryTerritory');

    view.load();
    tick();

    expect(view._model.groupForm.value).toEqual(model.groupForm.value);
  }));

  it('add template with arr countries', () => {
    view._model.userType.setValue(view.types_of_users.AIRLINE, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
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
    view._model.userType.setValue(view.types_of_users.AIRLINE, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
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
