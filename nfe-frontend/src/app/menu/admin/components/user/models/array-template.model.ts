import { TemplateModel } from './template.model';
import { Country } from './../../../../adm-acm/models/country.model';
export class ArrayTemplateModel {

    constructor(public template: TemplateModel, public arrCountries: Country[]) {}


    getCountriesHtml() {
        let countries = '';
        let span = '';
        let size = this.arrCountries.length;
        if (this.arrCountries.length >= 10) {
            size = 10;
        }
        let separator = ', ';
        for (let i = 0; i < size; i++) {
            if (i == (size - 1)) {
                separator = '';
            }
            countries += this.arrCountries[i].name + separator;
        }

        if (this.arrCountries.length > 10) {
            span = '... <span class="view-more-btn">View More</span>';
        }
        const response = `<p class="countries">${countries} ${span}</p>`;
        return response;
    }



}
