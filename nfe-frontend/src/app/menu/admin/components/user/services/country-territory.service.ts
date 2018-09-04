import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Country } from '../../../../adm-acm/models/country.model';
import { HttpServiceAbstract } from '../../../../../shared/base/http-service-abstract';
import { environment } from '../../../../../../environments/environment';

@Injectable()
export class CountryTerritoryService extends HttpServiceAbstract<Country[], any> {

    countryTerritory: Country[] = [];

    constructor(private http: HttpClient) {
        super(http, environment.api.user.countryTerritory);
    }

    getCountriesAndTerritory() {
        return this.get().subscribe(val => {
            this.countryTerritory = val;
            console.log(val);
        });
    }

}
