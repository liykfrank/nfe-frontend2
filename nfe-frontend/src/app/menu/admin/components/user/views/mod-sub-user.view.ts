import { OnDestroy } from '@angular/core';
import { TranslationService } from 'angular-l10n';
import { NewUserModel } from '../models/new-user.model';
import { FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { GLOBALS } from '../../../../../shared/constants/globals';
import { ArrayTemplateModel } from '../models/array-template.model';
import { Country } from '../../../../adm-acm/models/country.model';
import { TemplateService } from '../../../services/template.service';
import { ReactiveFormHandler } from '../../../../../shared/base/reactive-form-handler';
import { CountryTerritoryService } from '../services/country-territory.service';
import { UserMaintenanceService } from '../services/user-maintenance.service';
import { UserInterface } from '../models/api/user.model';

export class ModSubUserView extends ReactiveFormHandler<NewUserModel> implements OnDestroy {

    _model = new NewUserModel();

    types_of_users = GLOBALS.TYPES_OF_USER;

    // TEMPLATES
    groupIsoCountry: FormGroup;
    isoCountry: FormControl;
    selectedTemplate;
    templates: ArrayTemplateModel[] = [];
    countries: Country[] = [];

    activeControl: FormGroup;

    subsActive;

    userType;

    constructor(private _translationService: TranslationService,
        public templateService: TemplateService,
        private countryTerritoryService: CountryTerritoryService,
        private _userMaintenanceService: UserMaintenanceService) {
        super()/* istanbul ignore next */;
        this.load();
    }

    ngOnDestroy(): void {
        this.subsActive.unsubscribe();
        this.subsActive = undefined;
    }

    load() {
        this.loadControlForCountryTerritory();
        this.loadSwitchActive();

        this._userMaintenanceService.getUser('c0658a16-d36c-4337-8ed7-2471bc188f34').subscribe(
          user => {
            this.populateForm(user);
          }
        );
    }

    // TEMPLATES
    loadControlForCountryTerritory() {
        this.isoCountry = new FormControl({ value: '', disabled: true });
        this.groupIsoCountry = new FormGroup({
            isoCountry: this.isoCountry
        });
        this.countryTerritoryService.getCountriesAndTerritory();
    }

    onSelectedTemplate(event) {
        this.selectedTemplate = event;
    }

    addTemplate() {


        if (this.selectedTemplate && this.countries.length > 0) {
            this.templates.push(new ArrayTemplateModel(this.selectedTemplate, this.countries, (this._model.userType.value == this.types_of_users.AGENT)));
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
    }

    toogleEdit(position) {
        this.templates[position].modeEdit = !this.templates[position].modeEdit;
    }

    showTemplate() {
        return true;
    }
    // END TEMPLATES

    loadSwitchActive() {
        this.activeControl = new FormGroup({
            active: new FormControl(true)
        });

        this.subsActive = this.activeControl.get('active').valueChanges.subscribe((val) => {

            if (val) {
                this.disableControl(this._model.expiryDate);
                // TODO: en caso de que sea disabled hay recuperar el valor antiguo.
            } else {
                this.enableControl(this._model.expiryDate);
            }
        });
    }

    getTextButton() {
        return this._translationService.translate('USERS.btnUpdate');
    }

    callApi() {
        return true;
    }

    private populateForm(user: UserInterface): void {
      this._model.name.setValue(user.name);
      this._model.lastname.setValue(user.lastName);
      this._model.username.setValue(user.username);
      this._model.email.setValue(user.email);
      this._model.organization.setValue(user.organization);
      this._model.locality.setValue(user.address.locality);
      this._model.city.setValue(user.address.city);
      this._model.zip.setValue(user.address.zip);
      this._model.country.setValue(user.address.country);
      this._model.telephone.setValue(user.telephone);
    }

    get model(): NewUserModel {
        return this._model;
    }
  }
