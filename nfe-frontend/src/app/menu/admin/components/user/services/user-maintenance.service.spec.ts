import { UserMaintenanceService } from './user-maintenance.service';
import { inject, TestBed } from '@angular/core/testing';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../../../../shared/base/conf/l10n.config';
import { AlertsService } from '../../../../../core/services/alerts.service';
import { User } from '../models/api/user.model';
import { UserAddress } from '../models/api/user-address.model';
import { of } from 'rxjs/observable/of';

describe('UserMaintenanceService', () => {
    const _HttpClient = jasmine.createSpyObj<HttpClient>('HttpClient', [
        'post'
      ]);
      const user = new User(new UserAddress('', '', '', '', ''), 'jjj@jjj.com', '', '', 'lastname',
         'name', 'accelya', '', '000', '123', 'AIRLINE', 'jjj@jjj.com');

    beforeEach(() => {
        TestBed.configureTestingModule({
          imports: [
            HttpClientModule
          ],
          providers: [UserMaintenanceService]
        });
        _HttpClient.post.and.returnValue(
            of()
        );

      });


    it('should be created', inject(
        [UserMaintenanceService],
        (service: UserMaintenanceService) => {
          expect(service).toBeTruthy();
        }
    ));

    it('method create user', inject([UserMaintenanceService], (service: UserMaintenanceService) => {
        service.createUser(user);
        expect(service.createUser(user)).toBeTruthy();
      }));

});
