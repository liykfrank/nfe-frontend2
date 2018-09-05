import { CountryTerritoryService } from './country-territory.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { User } from '../models/api/user.model';
import { UserAddress } from '../models/api/user-address.model';
import { TestBed, inject  } from '@angular/core/testing';
import { of } from 'rxjs/observable/of';


describe('CountryTerritoryService', () => {
    const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', [
        'get'
      ]);

    beforeEach(() => {
        TestBed.configureTestingModule({
          imports: [
            HttpClientModule
          ],
          providers: [CountryTerritoryService]
        });
        _HttpClient.get.and.returnValue(
            of()
        );

      });


    it('should be created', inject(
        [CountryTerritoryService],
        (service: CountryTerritoryService) => {
          expect(service).toBeTruthy();
        }
    ));

    it('method getCountriesAndTerritory()', inject([CountryTerritoryService], (service: CountryTerritoryService) => {
        service.getCountriesAndTerritory();
        expect(service.getCountriesAndTerritory()).toBeTruthy();
      }));

});
