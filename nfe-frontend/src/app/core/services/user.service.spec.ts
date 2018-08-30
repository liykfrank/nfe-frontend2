import { User } from './../models/user.model';
import { HttpClientModule } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { TestBed, async, inject } from '@angular/core/testing';
import { TranslationModule, LocalizationModule } from 'angular-l10n';
import { l10nConfig } from '../../shared/base/conf/l10n.config';

import { UserService } from './user.service';
import { Observable } from '../../../../node_modules/rxjs/Observable';

describe('UserService', () => {
  const HTTP = jasmine.createSpyObj<HttpClient>('HttpClient', ['get']);
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        UserService,
        {provide: HttpClient, useValue: HTTP}]
    });
  }));

  it('user service should be created', async(inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  })));

  it('loadUser is called', inject([UserService], (service: UserService) => {
    const loadUser = spyOn(service, 'initializeUser');
    service.initializeUser();
    expect(loadUser).toHaveBeenCalled();
  }));

  it('loadUser is calling to http get', inject([UserService], (service: UserService) => {
    HTTP.get.calls.reset();
    HTTP.get.and.returnValue(Observable.of(200));
    service.initializeUser();
    expect(HTTP.get.calls.count()).toBe(1, 'expected only one call');
  }));
/*
  it('getter user is called', inject([UserService], (service: UserService) => {
    const getUser = spyOnProperty(HTTP, 'user', 'get');
    const user = service._user;
    expect(getUser).toHaveBeenCalled();
  }));


  it('getter user return an User', inject([UserService], (service: UserService) => {
    const objUser = {} as User;
    const spy = spyOnProperty(service, 'user', 'get').and.returnValue(objUser);
    expect(service.user).toBe(objUser);
  }));
 */

});
