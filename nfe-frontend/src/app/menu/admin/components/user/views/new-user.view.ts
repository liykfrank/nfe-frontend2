import { UserMaintenanceService } from './../services/user-maintenance.service';
import { OnDestroy } from '@angular/core';
import { GLOBALS } from '../../../../../shared/constants/globals';
import { NewUserModel } from '../models/new-user.model';
import { FormControl, FormGroup, AbstractControl } from '@angular/forms';
import { TranslationService } from 'angular-l10n';
import { ArrayTemplateModel } from '../models/array-template.model';
import { Country } from '../../../../adm-acm/models/country.model';
import { TemplateService } from '../../../services/template.service';
import { CountryTerritoryService } from '../services/country-territory.service';
import { EnvironmentType } from '../../../../../shared/enums/environment-type.enum';
import { AgentService } from '../../../../../shared/components/agent/services/agent.service';
import { ReactiveFormHandler } from '../../../../../shared/base/reactive-form-handler';
import { UserInterface, User } from '../models/api/user.model';
import { UserAddress } from '../models/api/user-address.model';
import { UtilsService } from '../../../../../shared/services/utils.service';
import { AlertsService } from '../../../../../core/services/alerts.service';
import { AlertModel } from '../../../../../core/models/alert.model';
import { AlertType } from '../../../../../core/enums/alert-type.enum';

export class NewUserView extends ReactiveFormHandler<NewUserModel>
  implements OnDestroy {
  _model = new NewUserModel();

  _types_of_users;

  // TEMPLATES
  groupIsoCountry: FormGroup;
  isoCountry: FormControl;
  selectedTemplate;
  templates: ArrayTemplateModel[] = [];
  countries: Country[] = [];

  userValid = false;

  private subsType;

  constructor(
    private _translationService: TranslationService,
    private templateService: TemplateService,
    private countryTerritoryService: CountryTerritoryService,
    private _agentService: AgentService,
    private _userMaintenanceService: UserMaintenanceService,
    private _utilsService: UtilsService,
    private _alertService: AlertsService
  ) {
    super();
    this.load();
  }

  ngOnDestroy(): void {
    this.subsType.unsubscribe();
  }

  load() {
    this._types_of_users = GLOBALS.TYPES_OF_USER;
    this.loadControlForCountryTerritory();

    this.disableFormGroup(this._model.groupForm, [this._model.userType]);

    this.subsType = this._model.userType.valueChanges.subscribe(userType => {
      this.disableFormGroup(this._model.groupForm, [
        this._model.userType,
        this._model.userCode
      ]);
      this.resetControl(this._model.groupForm);
      this.userValid = false;
      this.templates = [];
      this.isoCountry.setValue('');
      this.countries = [];
      this.templateService.reset();
      this._model.userType.setValue(
        userType,
        GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE
      );

      if (userType == 'null') {
        this.disableControl(this._model.userCode);
        this.countries = [];
      }
    });

    this.subscriptions.push(
      this._model.userCode.valueChanges.subscribe(() => {
        this.switchUserType();
      })
    );
  }

  switchUserType() {
    const userCode = this._model.userCode.value;
    console.log(userCode);

    if (userCode && this._model.userCode.valid) {
      switch (this._model.userType.value) {
        case this.types_of_users.AGENT:
          this.caseUserTypeIsAgent(userCode);
          break;
        case this.types_of_users.AIRLINE:
          this.caseUserTypeIsAirline(userCode);
          break;
        case this.types_of_users.GDS:
          this.caseUserTypeIsGds(userCode);
          break;
        default:
          this.caseUserTypeIsOther(userCode);
          break;
      }
    } else {
      this.disableFormGroup(this._model.groupForm, [
        this._model.userType,
        this._model.userCode
      ]);
      this.resetControl(this.isoCountry);
      this.templates = [];
      this.isoCountry.setValue('');
      this.countries = [];
      this.templateService.reset();
      this.userValid = false;
    }
  }

  // TYPES
  disableAllExceptUserTypeAndCode(disabled: boolean) {
    if (disabled) {
      this.disableControl(this._model.groupForm);
      this.enableControl(this._model.userType);
      this.enableControl(this._model.userCode);
    } else {
      this.enableControl(this._model.groupForm);
    }
  }

  caseUserTypeIsAgent(userCode: string): void {
    userCode = this.agentCodeCd(userCode);
    this._agentService
      .validateAgent(EnvironmentType.MASTER_AGENT, userCode)
      .subscribe(
        agent => {
          this.enableFormGroup(this._model.groupForm);
          this.userValid = true;
          this.isoCountry.setValue(agent.isoCountryCode);
        },
        error => {
          this.disableFormGroup(this._model.groupForm, [
            this._model.userType,
            this._model.userCode
          ]);
          this.resetControl(this.isoCountry);
          this.userValid = false;
        }
      );
  }

  caseUserTypeIsAirline(userCode): void {
    // TODO: Hacer la llamada al servicio validarUserCode
    // TODO: Hacer la llamada al servicio countries
    this.userValid = userCode != '' ? true : false;
    this.enableFormGroup(this._model.groupForm);
    this.countryTerritoryService.getCountriesAndTerritory();
  }

  caseUserTypeIsGds(userCode): void {
    // TODO: Hacer la llamada al servicio validarUserCode
    // TODO: Hacer la llamada al servicio countries
    this.userValid = userCode != '' ? true : false;
    this.enableFormGroup(this._model.groupForm);
    this.countryTerritoryService.getCountriesAndTerritory();
  }

  caseUserTypeIsOther(userCode): void {
    // TODO: Hacer la llamada al servicio countries
    this.userValid = userCode != '' ? true : false;
    this.enableFormGroup(this._model.groupForm);
    this.countryTerritoryService.getCountriesAndTerritory();
  }

  isUserCodeValidate() {
    return this.userValid;
  }

  isAgent() {
    return this._model.userType.value === this._types_of_users.AGENT;
  }

  getTypeOfUsersArray() {
    return Object.values(this._types_of_users);
  }

  checkType(type) {
    return this._model.userType.value == type;
  }

  showTemplate() {
    return this.userValid;
  }
  // END TYPES

  // TEMPLATES
  loadControlForCountryTerritory() {
    this.isoCountry = new FormControl({ value: '', disabled: true });
    this.groupIsoCountry = new FormGroup({
      isoCountry: this.isoCountry
    });
  }

  onSelectedTemplate(event) {
    this.selectedTemplate = event;
  }

  addTemplate() {
    if (
      this._model.userType.value == this.types_of_users.AGENT &&
      this.countries.length < 1
    ) {
      const country = new Country();
      country.isoCountryCode = this.isoCountry.value;
      country.name = this.isoCountry.value;
      this.countries.push(country);
    }

    if (this.selectedTemplate && this.countries.length > 0) {
      this.templates.push(
        new ArrayTemplateModel(
          this.selectedTemplate,
          this.countries,
          this._model.userType.value == this.types_of_users.AGENT
        )
      );
      this.templateService.removeTemplate(this.selectedTemplate);
      this.selectedTemplate = undefined;
      this.countries = [];
    }
  }

  removeTemplate(position) {
    if (this.templates.length > 0) {
      this.templateService.restoreTemplate(this.templates[position].template);
      this.templates.splice(position, 1);
    }
  }

  onReturnCountryTerritory(event) {
    this.countries = event;
    console.log(this.countries);
  }

  toogleEdit(position) {
    this.templates[position].modeEdit = !this.templates[position].modeEdit;
  }
  // END TEMPLATES

  getTextButton() {
    return this._translationService.translate('USERS.btnCreate');
  }

  callApi() {

    // Llamada a la api;
    const user: UserInterface = this.buildRequestUser();
    this._userMaintenanceService.createUser(user).subscribe(
      data => {

        const alert = new AlertModel(
          this._translationService.translate('USERS.views.newUser.title'),
          this._translationService.translate('USERS.views.newUser.message'),
          AlertType.INFO
        );

        this._alertService.setAlert(alert);

        this.resetControl(this.model.groupForm);
        this.disableAllExceptUserTypeAndCode(true);
        this.isoCountry.setValue('');
        this.countries = [];
        this.templates = [];
        this.templateService.reset();
      }, err => {
        this.setErrors(err.error);
      }
    );

  }

  private buildRequestUser(): UserInterface {
    const userCode = this._model.userType.value == this.types_of_users.AGENT ? this.agentCodeCd(this._model.userCode.value) : this._model.userCode.value;
    return new User(
      new UserAddress(
        this._model.city.value,
        this._model.country.value,
        this._model.description.value,
        this._model.locality.value,
        this._model.zip.value
      ),
      this._model.email.value,
      this._model.expiryDate.value,
      this._model.lastModifiedDate.value,
      this._model.lastname.value,
      this._model.name.value,
      this._model.organization.value,
      this._model.registerDate.value,
      this._model.telephone.value,
      userCode,
      this._model.userType.value.toUpperCase(),
      this._model.username.value
    );
  }

  setErrors(error) {
    if (error.validationErrors) {
      const forms = [
        this.model.groupForm
      ];


      const errors = this._utilsService.setBackErrorsOnForms(
        forms.filter(x => x != null),
        error.validationErrors
      );

      console.log(errors);

      let msg = '';
      for (const aux of errors) {
        msg += '\n' + aux.message;
      }

      const alert = new AlertModel(
        this._translationService.translate('error'),
        msg == ''
          ? this._translationService.translate('REFUNDS.error')
          : msg,
        AlertType.ERROR
      );
      this._alertService.setAlert(alert);
    }
  }

  get model(): NewUserModel {
    return this._model;
  }

  get types_of_users() {
    if (!this._types_of_users) {
      this._types_of_users = GLOBALS.TYPES_OF_USER;
    }
    return this._types_of_users;
  }

  // TODO: Meterla en un Utils
  private agentCodeCd(s: string): string {
    return s + (+s % 7);
  }
}
