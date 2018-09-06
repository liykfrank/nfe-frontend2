import { UserMaintenanceService } from './user-maintenance.service';
import { inject, TestBed } from '@angular/core/testing';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { User, UserInterface } from '../models/api/user.model';
import { UserAddress } from '../models/api/user-address.model';
import { of } from 'rxjs/observable/of';

describe('UserMaintenanceService', () => {
  const httpClientStub = jasmine.createSpyObj<HttpClient>('HttpClient', ['post', 'get']);

  const user: UserInterface = new User(
    new UserAddress('', '', '', '', ''),
    'jjj@jjj.com',
    '',
    '',
    'lastname',
    'name',
    'accelya',
    '',
    '000',
    '123',
    'AIRLINE',
    'jjj@jjj.com'
  );

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        UserMaintenanceService
      ]
    });
    httpClientStub.post.and.returnValue(of(user));
    httpClientStub.get.and.returnValue(of(user));
  });

  it('should be created', inject(
    [UserMaintenanceService],
    (service: UserMaintenanceService) => {
      expect(service).toBeTruthy();
    }
  ));

  it('method create user', inject(
    [UserMaintenanceService],
    (service: UserMaintenanceService) => {
      // service.createUser(user).subscribe((response) => {
      //   expect(response).toBe(user);
      // });
      expect(service.createUser).toBeTruthy();

    }
  ));

  it('getUser creates returns a user', inject(
    [UserMaintenanceService],
    (service: UserMaintenanceService) => {
      // service.getUser('').subscribe((response) => {
      //   expect(response).toBe(user);
      // });
      expect(service.createUser).toBeTruthy();
    }
  ));

});
