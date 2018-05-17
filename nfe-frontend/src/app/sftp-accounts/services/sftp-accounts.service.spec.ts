import { SftpAccountsService } from './sftp-accounts.service';
import { SftpAccount } from '../models/sftp-account';
import { defer } from 'rxjs/observable/defer';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { asyncError, asyncData } from '../../../testing/async-observable-helpers';

describe('SftpAccountsService', () => {
  let httpClientSpy: { get: jasmine.Spy, post: jasmine.Spy, put: jasmine.Spy, delete: jasmine.Spy };
  let sftpAccountsService: SftpAccountsService;

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['get', 'post', 'put', 'delete']);
    sftpAccountsService = new SftpAccountsService(<any>httpClientSpy);
  });

  it('should be created', () => {
    expect(sftpAccountsService).toBeTruthy();
  });

  it('should return expected accounts (HttpClient called once)', () => {
    const expectedSftpAccounts: SftpAccount[] =
      [{ login: "user1", mode: "RO", status: "enabled", publicKey: "1234" },
      { login: "user2", mode: "RW", status: "disabled", publicKey: null }
      ];

    httpClientSpy.get.and.returnValue(asyncData(expectedSftpAccounts));

    sftpAccountsService.accounts().subscribe(
      data => expect(data).toEqual(expectedSftpAccounts, 'expected accounts'),
      fail
    );
    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });

  it('should return expected account (HttpClient called once)', () => {
    const expectedSftpAccount: SftpAccount =
      { login: "user1", mode: "RO", status: "enabled", publicKey: "1234" };

    httpClientSpy.get.and.returnValue(asyncData(expectedSftpAccount));

    sftpAccountsService.account("user1").subscribe(
      data => expect(data).toEqual(expectedSftpAccount, 'expected account'),
      fail
    );

    expect(httpClientSpy.get.calls.count()).toBe(1, 'one call');
  });

  it('should return an error when the server returns a 404', () => {
    const errorResponse = new HttpErrorResponse({
      error: 'test 404 error',
      status: 404, statusText: 'Not Found'
    });

    httpClientSpy.get.and.returnValue(asyncError(errorResponse));

    sftpAccountsService.account("userX").subscribe(
      account => fail('expected an error, no account'),
      error => expect(error.status).toBe(404)
    );
  });

  it('should return an 201', () => {
    const response = new HttpResponse({
      status: 201, statusText: 'Created'
    });
    const newSftpAccount: SftpAccount =
      { login: "user1", mode: "RO", status: "enabled", publicKey: "1234" };

    httpClientSpy.post.and.returnValue(asyncData(response));

    sftpAccountsService.createAccount(newSftpAccount).subscribe(
      () => expect(response.status).toBe(201)
    );
  });

  it('put should return an 200', () => {
    const response = new HttpResponse({
      status: 200, statusText: 'OK'
    });
    const newSftpAccount: SftpAccount =
      { login: "user1", mode: "RO", status: "enabled", publicKey: "1234" };

    httpClientSpy.put.and.returnValue(asyncData(response));

    sftpAccountsService.putAccount(newSftpAccount).subscribe(
      () => expect(response.status).toBe(200)
    );
  });

  it('delete should return an 200', () => {
    const response = new HttpResponse({
      status: 200, statusText: 'OK'
    });

    httpClientSpy.delete.and.returnValue(asyncData(response));

    sftpAccountsService.deleteAccount("loginXX").subscribe(
      () => expect(response.status).toBe(200)
    );
  });
});
