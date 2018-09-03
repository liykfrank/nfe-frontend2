import { TemplateModel } from './template.model';
import { Country } from './../../../../adm-acm/models/country.model';
export class ArrayTemplateModel {

    modeEdit = false;

    constructor(public template: TemplateModel, public arrCountries: Country[], public agent = false) {}


    getCountriesHtml() {
        let span = '';
        let size = this.arrCountries.length;
        if (this.arrCountries.length >= 10) {
            size = 10;
        }
        if (this.arrCountries.length > 10) {
            span = `... <span class="tooltip-countries">View More
                            <div class="top">
                                ${this.getCountriesInText(this.arrCountries.length)}
                                <i></i>
                            </div>
                        </span>`;
        }
        const response = `<div class="countries">${this.getCountriesInText(size)} ${span}</div>`;
        return response;
    }

    setArrCountries(newArray) {
        this.arrCountries = newArray;
    }

    getCountriesInText(size) {
        let separator = ', ';
        let countries = '';
        for (let i = 0; i < size; i++) {
            if (i == (size - 1)) {
                separator = '';
            }
            countries += this.arrCountries[i].name + separator;
        }
        return countries;
    }



}
