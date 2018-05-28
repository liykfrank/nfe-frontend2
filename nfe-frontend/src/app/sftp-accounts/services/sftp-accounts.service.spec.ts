import { HttpClientModule, HttpResponse } from '@angular/common/http';
import { inject, TestBed } from '@angular/core/testing';
import { LocalizationModule, TranslationModule } from 'angular-l10n';
import { of } from 'rxjs/observable/of';

import { l10nConfig } from '../../shared/base/conf/l10n.config';
import { SftpAccountPassword } from '../models/sftp-account-password.model';
import { SftpAccount } from './../models/sftp-account.model';
import { SftpAccountResource } from './resources/sftp-account-resource';
import { SftpAccountsPasswordResource } from './resources/sftp-accounts-password-resource';
import { SftpAccountsResource } from './resources/sftp-accounts-resource';
import { SftpAccountsService } from './sftp-accounts.service';

describe('SftpAccountsService', () => {
  const STATUS_201 = new HttpResponse({ status: 201 });
  const STATUS_200 = new HttpResponse({ status: 200 });
  const ACCOUNTS = [
    new SftpAccount('user1', 'enabled', '1234'),
    new SftpAccount('user2', 'disabled', null)
  ];

  const SPY_SVC = jasmine.createSpyObj('SftpAccountResource', [
    'deleteById',
    'putById',
    'post'
  ]);
  SPY_SVC.deleteById.and.returnValue(of(STATUS_200));
  SPY_SVC.putById.and.returnValue(of(STATUS_200));
  SPY_SVC.post.and.returnValue(of(STATUS_201));

  const SPY_SVCLIST = jasmine.createSpyObj<SftpAccountsResource>(
    'SftpAccountsResource',
    ['get']
  );
  SPY_SVCLIST.get.and.returnValue(of(ACCOUNTS));

  const SPY_SVCPASS = jasmine.createSpyObj<SftpAccountsPasswordResource>(
    'SftpAccountsResSftpAccountsPasswordResourceource',
    ['putPassword']
  );
  SPY_SVCPASS.putPassword.and.returnValue(of(STATUS_200));

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        TranslationModule.forRoot(l10nConfig),
        LocalizationModule
      ],
      providers: [
        { provide: SftpAccountResource, useValue: SPY_SVC },
        { provide: SftpAccountsResource, useValue: SPY_SVCLIST },
        { provide: SftpAccountsPasswordResource, useValue: SPY_SVCPASS },
        SftpAccountsService
      ]
    });
  });

  it(
    'Service should be created',
    inject([SftpAccountsService], (svc: SftpAccountsService) => {
      expect(svc).toBeTruthy();
    })
  );

  it(
    'Should return expected accounts (HttpClient called once)',
    inject([SftpAccountsService], (svc: SftpAccountsService) => {
      svc.accounts().subscribe(data => {
        expect(data).toEqual(ACCOUNTS, 'expected accounts');
        expect(SPY_SVCLIST.get).toHaveBeenCalled();
        expect(SPY_SVCLIST.get).toHaveBeenCalledTimes(1);
      }, fail);
    })
  );

  it(
    'should return an 201',
    inject([SftpAccountsService], (svc: SftpAccountsService) => {
      const newSftpAccount: SftpAccount = new SftpAccount(
        'user1',
        'enabled',
        '1234'
      );

      let method = svc.createAccount(newSftpAccount);
      method.subscribe(ret => {
        expect(ret).toBeDefined();
        expect(ret.status).toBe(201);
      }, fail);
    })
  );

  it(
    'put should return an 200',
    inject([SftpAccountsService], (svc: SftpAccountsService) => {
      const newSftpAccount: SftpAccount = new SftpAccount(
        'user1',
        'enabled',
        '1234'
      );

      let method = svc.modifyAccount(newSftpAccount);
      method.subscribe(ret => {
        expect(ret).toBeDefined();
        expect(ret.status).toBe(200);
      }, fail);
    })
  );

  it(
    'delete should return an 200',
    inject([SftpAccountsService], (svc: SftpAccountsService) => {
      let method = svc.deleteAccount('loginXX');
      method.subscribe(ret => {
        expect(ret).toBeDefined();
        expect(ret.status).toBe(200);
      }, fail);
    })
  );

  it(
    'Change Password should return an 200',
    inject([SftpAccountsService], (svc: SftpAccountsService) => {
      let pass: SftpAccountPassword = new SftpAccountPassword('Password1', 'Password2', 'Password2');
      let method = svc.changePassword(ACCOUNTS[0], pass);
      method.subscribe(ret => {
        expect(ret).toBeDefined();
        expect(ret.status).toBe(200);
      }, fail);
    })
  );

});
