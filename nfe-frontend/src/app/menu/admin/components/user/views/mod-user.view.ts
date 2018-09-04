import { TranslationService } from 'angular-l10n';
import { NewUserModel } from '../models/new-user.model';
import { FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { GLOBALS } from '../../../../../shared/constants/globals';
import { ArrayTemplateModel } from '../models/array-template.model';
import { Country } from '../../../../adm-acm/models/country.model';
import { TemplateService } from '../../../services/template.service';
import { ReactiveFormHandler } from '../../../../../shared/base/reactive-form-handler';
import { CountryTerritoryService } from '../services/country-territory.service';

export class ModUserView extends ReactiveFormHandler<NewUserModel> {

    _model = new NewUserModel();

    types_of_users;

    // TEMPLATES
    groupIsoCountry: FormGroup;
    isoCountry: FormControl;
    selectedTemplate;
    templates: ArrayTemplateModel[] = [];
    countries: Country[] = [];

    constructor(public _translationService: TranslationService,
        public templateService: TemplateService,
        private countryTerritoryService: CountryTerritoryService) {
        super();
        this.load();
    }

    load() {
        this.types_of_users = GLOBALS.TYPES_OF_USER;
        this.loadControlForCountryTerritory();
        this.disableControl(this._model.userType);
        this.disableControl(this._model.userCode);
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
        if (this._model.userType.value == this.types_of_users.AGENT && this.countries.length < 1) {
            const country = new Country();
            country.isoCountryCode = this.isoCountry.value;
            country.name = this.isoCountry.value;
            this.countries.push(country);
        }

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

    getTextButton() {
        return this._translationService.translate('USERS.btnUpdate');
    }

    callApi() {
        return true;
    }

    get model(): NewUserModel {
        return this._model;
    }

}
