import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SftpAccountMaintenanceComponent } from './sftp-account-maintenance.component';
import { SftpAccount } from '../models/sftp-account';
import { jqxGridComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxgrid';
import { SftpAccountsModule } from '../sftp-accounts.module';
import { HttpResponse } from '@angular/common/http';
import { SftpAccountsService } from '../services/sftp-accounts.service';
import { SftpAccountGridComponent } from '../sftp-account-grid/sftp-account-grid.component';
import { SharedModule } from '../../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { jqxTextAreaComponent } from 'jqwidgets-scripts/jqwidgets-ts/angular_jqxtextarea';
import { SftpAccountsQueryComponent } from '../sftp-accounts-query/sftp-accounts-query.component';
import { SftpAccountComponent } from '../sftp-account/sftp-account.component';
import { ViewChild } from '@angular/core';
import { of } from 'rxjs/observable/of';

describe('SftpAccountMaintenanceComponent', () => {

  function getExpectedSftpAccounts(): SftpAccount[] {
    return [
      { login: 'user1', mode: 'RO', status: 'ENABLED', publicKey: '1234' },
      { login: 'user2', mode: 'RW', status: 'DISABLED', publicKey: null },
      { login: 'user3', mode: 'RO', status: 'ENABLED', publicKey: '8888' }
    ];
  }

  const response = new HttpResponse({
    status: 200, statusText: 'OK'
  });

  let expectedSftpAccounts;

  const newAccount = { login: 'user4', mode: 'RO', status: 'ENABLED', publicKey: '467862348979874987134' };

  const delUser = 'user2';

  let sftpAccountsServiceSpy = jasmine.createSpyObj('SftpAccountsService',
    ['accounts', 'createAccount', 'deleteAccount', 'putAccount']);

  sftpAccountsServiceSpy.deleteAccount.and.returnValue(of(response));
  sftpAccountsServiceSpy.createAccount.and.returnValue(of(response));
  sftpAccountsServiceSpy.putAccount.and.returnValue(of(response));

  let component: SftpAccountMaintenanceComponent;
  let fixture: ComponentFixture<SftpAccountMaintenanceComponent>;

  beforeEach(async(() => {
    expectedSftpAccounts = getExpectedSftpAccounts();
    sftpAccountsServiceSpy.accounts.and.returnValue(of(expectedSftpAccounts));
    TestBed.configureTestingModule({
      imports: [SharedModule, ReactiveFormsModule],
      declarations: [jqxTextAreaComponent, SftpAccountsQueryComponent, SftpAccountComponent, SftpAccountMaintenanceComponent, SftpAccountGridComponent],
      providers: [
        { provide: SftpAccountsService, useValue: sftpAccountsServiceSpy }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', async(() => {
    expect(component).toBeTruthy();
  }));

  it('should contain expected accounts', async(() => {
    component.ngOnInit();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(component.accounts).toEqual(expectedSftpAccounts);
    });
  }));

  it('should create account', async(() => {
    let num = expectedSftpAccounts.length;
    component.saveClicked(Object.assign({}, newAccount));
    expect(component.accounts.length).toBe(num + 1);
    expect(component.accounts).toContain(newAccount);
  }));

  it('should delete account', async(() => {
    let num = expectedSftpAccounts.length;
    component.deleteClicked({ login: delUser, mode: '', status: '', publicKey: null });
    expect(component.accounts.length).toBe(num - 1);
    expect(component.accounts.findIndex(x => x.login == delUser)).toBe(-1);
  }));

  it('should update account', async(() => {
    let num = expectedSftpAccounts.length;
    let testKey = 'TEST-KEY';
    let acc = component.accounts[1];
    let acc2 = Object.assign({}, acc);
    acc2.publicKey = testKey;
    expect(acc).not.toEqual(acc2);
    component.onSelect(acc);
    expect(component.selAccount).toEqual(acc);
    component.saveClicked(acc2);
    expect(component.accounts.length).toBe(num);
    expect(component.accounts).toContain(acc);
    expect(acc).toEqual(acc2);
    expect(acc.publicKey).toEqual(testKey);
  }));

  it('should create new empty account', async(() => {
    component.newClicked();
    expect(component.sftpAccount).not.toBeNull();
    expect(component.sftpAccount).toEqual(new SftpAccount());
  }));

  it('should cancel create/ update new empty account', async(() => {
    component.cancelClicked({});
    expect(component.sftpAccount).toBeNull();
  }));
});
