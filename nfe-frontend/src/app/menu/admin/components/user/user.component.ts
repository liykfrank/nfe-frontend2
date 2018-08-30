import { ArrayTemplateModel } from './models/array-template.model';
import { EnvironmentType } from './../../../../shared/enums/environment-type.enum';
import { ReactiveFormHandler } from './../../../../shared/base/reactive-form-handler';
import { CountryTerritoryService } from './services/country-territory.service';
import { Agent } from './../../../../shared/components/agent/models/agent.model';
import { AgentService } from './../../../../shared/components/agent/services/agent.service';
import { Country } from './../../../adm-acm/models/country.model';
import { Component, OnInit, OnDestroy, ViewEncapsulation } from '@angular/core';
import { NewUserModel } from './models/new-user.model';
import { FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { UserViews } from '../../../../shared/enums/users.enum';
import { GLOBALS } from '../../../../shared/constants/globals';
import { Router } from '@angular/router';
import { TranslationService } from 'angular-l10n';
import { TemplateService } from '../../services/template.service';


@Component({
  selector: 'bspl-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class UserComponent extends ReactiveFormHandler<NewUserModel> implements OnInit, OnDestroy {

  newUserModel = new NewUserModel();

  variablesControl: FormGroup;

  typesOfScreens = UserViews;

  screenType = this.typesOfScreens.NEW_USER;

  types_of_users;

  patterns = GLOBALS.PATTERNS;

  selectedTemplate;

  templates: ArrayTemplateModel[] = [];

  groupIsoCountry: FormGroup;
  isoCountry: FormControl;

  countries: Country[] = [];

  private subsActive;
  private subsType;

  constructor(
    public router: Router,
    private _translationService: TranslationService,
    public templateService: TemplateService,
    public countryTerritoryService: CountryTerritoryService,
    private _agentService: AgentService
  ) {
    super();
    this.getView(router.routerState.snapshot.url);
  }

  ngOnInit() {
    this.loadControlForCountryTerritory();
    if (this.isViewUser()) {

      this.types_of_users = GLOBALS.TYPES_OF_USER;

      if (this.isViewModUser()) {
        this.enableAll([this.newUserModel.userType, this.newUserModel.userCode]);

      } else {
        this.disableAll([this.newUserModel.userType]);

        this.subsType = this.newUserModel.userType.valueChanges.subscribe((userType) => {
          this.disableAll([this.newUserModel.userType, this.newUserModel.userCode]);
          this.resetControl(this.newUserModel.newUserGroup);
          this.newUserModel.userType.setValue(userType, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);

          if (userType == 'null') {
            this.disableControl(this.newUserModel.userCode);
          }
        });

        this.subscriptions.push(
          this.newUserModel.userCode.valueChanges.subscribe(() => {
            this.switchUserType();
          })
        );

      }
    } else {
      if (this.screenType == this.typesOfScreens.MOD_SUB_USER) {
        this.loadSwitchActive();
      }
    }
  }

  switchUserType() {
    const userCode = this.newUserModel.userCode.value;

    if (userCode && this.newUserModel.userCode.valid) {

      switch (this.newUserModel.userType.value) {
        case this.types_of_users.AGENT:
          this.caseUserTypeIsAgent(userCode);
          break;
        default:
          break;
      }

    } else {
      this.disableAll([this.newUserModel.userType, this.newUserModel.userCode]);
      this.resetControl(this.isoCountry);
    }

  }

  enableControl(formControl: AbstractControl): void {
    formControl.enable(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  disableControl(formControl: AbstractControl): void {
    formControl.disable(GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  resetControl(formControl: AbstractControl): void {
    formControl.reset('', GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
  }

  disableAll(exceptions?: AbstractControl[]): void {
    this.disableControl(this.newUserModel.newUserGroup);

    if (!exceptions) { return; }

    for (const control of exceptions) {
      this.enableControl(control);
    }
  }

  enableAll(exceptions?: AbstractControl[]): void {
    this.enableControl(this.newUserModel.newUserGroup);

    if (!exceptions) { return; }

    for (const control of exceptions) {
      this.disableControl(control);
    }
  }

  disableAllExceptUserTypeAndCode(disabled: boolean) {
    if (disabled) {
      this.disableControl(this.newUserModel.newUserGroup);
      this.enableControl(this.newUserModel.userType);
      this.enableControl(this.newUserModel.userCode);
     } else {
      this.enableControl(this.newUserModel.newUserGroup);
    }
  }

  caseUserTypeIsAgent(userCode: string): void {
    userCode = userCode + (+userCode % 7);
    this._agentService.validateAgent(EnvironmentType.MASTER_AGENT, userCode).subscribe(
      agent => {
        console.log(agent);
        this.enableAll();
        this.isoCountry.setValue(agent.isoCountryCode);
      }, error => {
        this.disableAll([this.newUserModel.userType, this.newUserModel.userCode]);
        this.resetControl(this.isoCountry);
      }
    );
  }

  caseUserTypeIsAirline(userCode): void {
    // TODO: Hacer la llamada al servicio validarUserCode
    // TODO: Hacer la llamada al servicio countries
  }

  caseUserTypeIsGds(userCode): void {
    // TODO: Hacer la llamada al servicio validarUserCode
    // TODO: Hacer la llamada al servicio countries

  }

  caseUserTypeIsOther(userCode): void {
    this.enableAll();
    // TODO: Hacer la llamada al servicio countries
  }

  isUserCodeValidate() {

    if (this.isViewUser()) {

      if (this.newUserModel.userType == this.types_of_users.AGENT) {
        return this.isoCountry.value != '';
      }
      return this.countryTerritoryService.countryTerritory.length > 0;
    }
    return this.countryTerritoryService.countryTerritory.length > 0 || this.isoCountry.value != '';
  }

  loadControlForCountryTerritory() {
    this.isoCountry = new FormControl({value: '', disabled: true});
    this.groupIsoCountry = new FormGroup({
      isoCountry: this.isoCountry
    });
  }

  loadSwitchActive() {
    this.variablesControl = new FormGroup({
      active: new FormControl(true)
    });

    this.subsActive = this.variablesControl.get('active').valueChanges.subscribe((val) => {

      if (val) {
        this.disableControl(this.newUserModel.expiryDate);
        // TODO: en caso de que sea disabled hay recuperar el valor antiguo.
      } else {
        this.enableControl(this.newUserModel.expiryDate);
      }
    });
  }

  checkView(view) {
    return this.newUserModel.userType.value == view;
  }

  ngOnDestroy(): void {
    if (this.screenType == this.typesOfScreens.MOD_SUB_USER) {
      this.subsActive.unsubscribe();
    }
  }

  isViewUser(): boolean {
    return this.screenType == this.typesOfScreens.NEW_USER || this.screenType == this.typesOfScreens.MOD_USER;
  }

  isViewModUser() {
    return this.screenType == this.typesOfScreens.MOD_USER;
  }

  isViewModSubUser(): boolean {
    return this.screenType == this.typesOfScreens.MOD_SUB_USER;
  }

  isAgent() {
    if (this.isViewUser()) {
      return this.newUserModel.userType.value === this.types_of_users.AGENT;
    }
    return false;
  }

  getView(url): string {
    const arr_url = url.split('/');
    if (arr_url.length > 0) {

      this.screenType = arr_url[2];

      return arr_url[2];
    }
    return url;
  }

  getTextButton() {
    if (this.screenType == this.typesOfScreens.NEW_USER || this.screenType == this.typesOfScreens.NEW_SUB_USER) {
      return this._translationService.translate('USERS.btnCreate');
    }
    return this._translationService.translate('USERS.btnUpdate');
  }

  onSelectedTemplate(event) {
    this.selectedTemplate = event;
    console.log(<any>this.selectedTemplate);
  }

  addTemplate() {
    if (this.selectedTemplate && this.countries.length > 0) {
      this.templates.push(new ArrayTemplateModel(this.selectedTemplate, this.countries));
      this.templateService.removeTemplate(this.selectedTemplate);
      this.selectedTemplate = undefined;
      this.countries = [];
    }
    console.log('size countries: ' + this.countries.length);
  }

  removeTemplate(position) {
    if (this.templates.length > 0) {
      this.templateService.restoreTemplate(this.templates[position].template);
      this.templates.splice(0, 1);
    }
  }

  onReturnCountryTerritory(event) {
    this.countries = event;
    console.log(this.countries);
  }

}
