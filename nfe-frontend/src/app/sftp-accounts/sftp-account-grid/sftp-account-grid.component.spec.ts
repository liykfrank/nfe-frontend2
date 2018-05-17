import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SftpAccountGridComponent } from './sftp-account-grid.component';
import { SftpAccountsModule } from '../sftp-accounts.module';
import { SftpAccount } from '../models/sftp-account';

describe('SftpAccountGridComponent', () => {

  function getTestAccounts(): SftpAccount[] {
    return [
      { login: 'user1', mode: 'RO', status: 'ENABLED', publicKey: '1234' },
      { login: 'user2', mode: 'RW', status: 'DISABLED', publicKey: null },
      { login: 'user3', mode: 'RO', status: 'ENABLED', publicKey: '8888' }
    ]
  }

  let component: SftpAccountGridComponent;
  let fixture: ComponentFixture<SftpAccountGridComponent>;

  let sftpAccounts: any[] = getTestAccounts();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SftpAccountsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountGridComponent);
    component = fixture.componentInstance;
    component.accounts = getTestAccounts();
    fixture.detectChanges();
    component.ngOnInit();
    fixture.detectChanges();
    component.ngAfterViewInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have grid loaded', () => {
    expect(component.grid).toBeDefined();
    expect(component.grid.getrows().length).toBe(sftpAccounts.length);
  });

  it('should have accounts in grid', () => {
    for (let i = 0; i < sftpAccounts.length; i++) {
      let data = component.grid.getrowdata(i);
      let { login, mode, status, publicKey } = data;
      expect(sftpAccounts).toContain({ login, mode, status, publicKey });
    }
  });

  it('should capture row-selection', () => {
    component.select.subscribe(x => {
      expect(x).toBeDefined();
      let { login, mode, status, publicKey } = x;
      expect({ login, mode, status, publicKey }).toEqual(sftpAccounts[1]);
    });
    component.grid.selectrow(1);
  });

  it('should unselect row', () => {
    component.grid.selectrow(1);
    component.unselectRow();
    expect(component.grid.selectedrowindex()).toBe(-1);
  });

  it('should not contain date columns', () => {
    let { creationTime, updatedTime } = component.grid.getrowdata(1);
    expect(creationTime).toBeUndefined();
    expect(updatedTime).toBeUndefined();
  });
});


describe('SftpAccountGridComponent', () => {

  function getTestAccounts(): SftpAccount[] {
    return [
      { login: 'user1', mode: 'RO', status: 'ENABLED', publicKey: '1234' },
      { login: 'user2', mode: 'RW', status: 'DISABLED', publicKey: null },
      { login: 'user3', mode: 'RO', status: 'ENABLED', publicKey: '8888' }
    ]
  }

  let component: SftpAccountGridComponent;
  let fixture: ComponentFixture<SftpAccountGridComponent>;

  let sftpAccounts: any[] = getTestAccounts();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SftpAccountsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountGridComponent);
    component = fixture.componentInstance;
    component.accounts = getTestAccounts();
    component.includeDates = true;
    fixture.detectChanges();
    component.ngOnInit();
    fixture.detectChanges();
    component.ngAfterViewInit();
    fixture.detectChanges();
  });

  it('should contain date columns', () => {
    let { creationTime, updatedTime } = component.grid.getrowdata(1);
    expect(creationTime).not.toBeDefined();
    expect(updatedTime).not.toBeDefined();
  });
});
