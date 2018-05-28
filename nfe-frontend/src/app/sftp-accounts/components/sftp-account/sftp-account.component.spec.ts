import { HttpErrorResponse } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Observable } from 'rxjs/Observable';

import { asyncError } from '../../../../testing/async-observable-helpers';
import { SftpAccount } from '../../models/sftp-account.model';
import { SharedModule } from './../../../shared/shared.module';
import { SftpAccountPassword } from './../../models/sftp-account-password.model';
import { SftpAccountsService } from './../../services/sftp-accounts.service';
import { SftpAccountComponent } from './sftp-account.component';

describe('SftpAccountComponent new', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  const sftpAccountsMock: SftpAccount[] = [];
  const expectedSftpAccountCreated: SftpAccount = new SftpAccount(
    'test',
    'ENABLED',
    'test public key'
  );

  const sftpAccountServiceSpy = jasmine.createSpyObj('SftpAccountsService', [
    'accounts',
    'createAccount'
  ]);
  sftpAccountServiceSpy.accounts.and.returnValue(
    Observable.of(sftpAccountsMock)
  );
  sftpAccountServiceSpy.createAccount.and.returnValue(
    Observable.of(expectedSftpAccountCreated)
  );

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [
          { provide: SftpAccountsService, useValue: sftpAccountServiceSpy }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create a new account', () => {
    component.ngOnInit();
    Object.assign(component.sftpAccount, expectedSftpAccountCreated);
    component.save();

    expect(component.sftpAccount).toEqual(expectedSftpAccountCreated);
    expect(component.sftpAccountAlreadyExists).toBe(true);
    expect(component.msgs[0].summary).toBe('Sftp Account created correctly');
  });
});
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
describe('SftpAccountComponent update and delete', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  const sftpAccountsServiceMock: SftpAccount[] = [
    new SftpAccount('test', 'ENABLED', 'test public key')
  ];
  const expectedSftpAccountModified: SftpAccount = new SftpAccount(
    'test',
    'DISABLED',
    'test public key'
  );
  const expectedSftpAccountDeleted: SftpAccount = new SftpAccount('', '', '');

  const sftpAccountServiceSpy = jasmine.createSpyObj('SftpAccountsService', [
    'accounts',
    'modifyAccount',
    'deleteAccount'
  ]);
  sftpAccountServiceSpy.accounts.and.returnValue(
    Observable.of(sftpAccountsServiceMock)
  );
  sftpAccountServiceSpy.modifyAccount.and.returnValue(
    Observable.of(expectedSftpAccountModified)
  );
  sftpAccountServiceSpy.deleteAccount.and.returnValue(
    Observable.of(expectedSftpAccountDeleted)
  );

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [
          { provide: SftpAccountsService, useValue: sftpAccountServiceSpy }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should modify an account',
    async(() => {
      component.ngOnInit();
      Object.assign(component.sftpAccount, expectedSftpAccountModified);
      component.save();

      expect(component.sftpAccount).toEqual(expectedSftpAccountModified);
      expect(component.sftpAccountAlreadyExists).toBe(true);
      expect(component.msgs[0].summary).toBe('Sftp Account modified correctly');
    })
  );

  it(
    'should delete an account',
    async(() => {
      component.ngOnInit();

      component.delete();
      expect(component.sftpAccount).toEqual(expectedSftpAccountDeleted);
      expect(component.sftpAccountAlreadyExists).toBe(false);
      expect(component.msgs[0].summary).toBe('Sftp Account deleted correctly');
    })
  );
});
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
describe('SftpAccountComponent errors', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  const sftpAccountsMock: SftpAccount[] = [
    new SftpAccount('test', 'ENABLED', 'test public key')
  ];
  const responseErrorMock: HttpErrorResponse = new HttpErrorResponse({
    error: 404
  });

  const sftpAccountServiceSpy = jasmine.createSpyObj('SftpAccountsService', [
    'accounts',
    'createAccount',
    'modifyAccount',
    'deleteAccount'
  ]);
  sftpAccountServiceSpy.accounts.and.returnValue(
    Observable.of(sftpAccountsMock)
  );
  sftpAccountServiceSpy.createAccount.and.returnValue(
    asyncError(responseErrorMock)
  );
  sftpAccountServiceSpy.modifyAccount.and.returnValue(
    asyncError(responseErrorMock)
  );
  sftpAccountServiceSpy.deleteAccount.and.returnValue(
    asyncError(responseErrorMock)
  );

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [
          { provide: SftpAccountsService, useValue: sftpAccountServiceSpy }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should manage a creation account error', () => {
    component.sftpAccountAlreadyExists = false;
    component.save();
  });

  it('should manage a modification account error', () => {
    component.sftpAccountAlreadyExists = true;
    component.save();
  });

  it('should manage a deletion account error', () => {
    component.delete();
  });
});

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
describe('Password change', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  const sftpAccountsMock: SftpAccount[] = [
    new SftpAccount('test', 'ENABLED', 'test public key')
  ];
  const passwordMock: SftpAccountPassword = new SftpAccountPassword(
    'test123',
    'test1234',
    'test1234'
  );

  const sftpAccountServiceSpy = jasmine.createSpyObj('SftpAccountsService', [
    'accounts',
    'changePassword'
  ]);
  sftpAccountServiceSpy.accounts.and.returnValue(
    Observable.of(sftpAccountsMock)
  );
  sftpAccountServiceSpy.changePassword.and.returnValue(
    Observable.of(passwordMock)
  );

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [
          { provide: SftpAccountsService, useValue: sftpAccountServiceSpy }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should try to change the password', () => {
    component.changePassword();
    expect(component.changePwd).toBe(true);
  });

  it('should save the sftp account password change', () => {
    component.changePassword();
    component.savePassword();
    expect(component.changePwd).toBe(false);
    expect(component.sftpAccountPassword).toEqual(
      new SftpAccountPassword('', '', '')
    );
    expect(component.msgs[0].summary + ' ' + component.msgs[0].detail).toBe(
      'Password has been changed successfully'
    );
  });

  it('should cancel the password change', () => {
    Object.assign(component.sftpAccountPassword, passwordMock);
    component.cancelChangePassword();
    expect(component.changePwd).toBe(false);
    expect(component.sftpAccountPassword).toEqual(
      new SftpAccountPassword('', '', '')
    );
  });
});
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
describe('Password errors', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  const sftpAccountsMock: SftpAccount[] = [
    new SftpAccount('test', 'ENABLED', 'test public key')
  ];

  const responseErrorMock: HttpErrorResponse = new HttpErrorResponse({
    error: {
      error: 'Forbidden',
      message: 'Incorrect password',
      status: 403
    }
  });

  const sftpAccountServiceSpy = jasmine.createSpyObj('SftpAccountsService', [
    'accounts',
    'changePassword'
  ]);

  sftpAccountServiceSpy.accounts.and.returnValue(
    Observable.of(sftpAccountsMock)
  );
  sftpAccountServiceSpy.changePassword.and.returnValue(
    asyncError(responseErrorMock)
  );

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [
          { provide: SftpAccountsService, useValue: sftpAccountServiceSpy }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should show an incorrect old password error',
    async(() => {
      component.savePassword();
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        expect(component.showOldPasswordError).toBe(true);
      });
    })
  );

});
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
describe('Password backend error', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  const sftpAccountsMock: SftpAccount[] = [
    new SftpAccount('test', 'ENABLED', 'test public key')
  ];

  const sftpAccountServiceSpy = jasmine.createSpyObj('SftpAccountsService', [
    'accounts',
    'changePassword'
  ]);

  sftpAccountServiceSpy.accounts.and.returnValue(
    Observable.of(sftpAccountsMock)
  );
  sftpAccountServiceSpy.changePassword.and.returnValue(
    asyncError(new HttpErrorResponse({
      error: {
        status: 404
      }
    }))
  );

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [
          { provide: SftpAccountsService, useValue: sftpAccountServiceSpy }
        ]
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it(
    'should show an unexpected backend error',
    async(() => {

      component.savePassword();
      fixture.whenStable().then(() => {
        fixture.detectChanges();
        expect(component.showOldPasswordError).toBe(false);
      });
    })
  );

});
