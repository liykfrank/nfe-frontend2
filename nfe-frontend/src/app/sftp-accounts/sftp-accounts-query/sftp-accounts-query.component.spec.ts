import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SftpAccountsQueryComponent } from './sftp-accounts-query.component';
import { SftpAccountsService } from '../services/sftp-accounts.service';
import { SftpAccount } from '../models/sftp-account';
import { asyncData } from '../../../testing/async-observable-helpers';
import { SftpAccountsModule } from '../sftp-accounts.module';

describe('SftpAccountsQueryComponent', () => {

  const expectedSftpAccounts: SftpAccount[] = [
    { login: 'user1', mode: 'RO', status: 'ENABLED', publicKey: '1234' },
    { login: 'user2', mode: 'RW', status: 'DISABLED', publicKey: null },
    { login: 'user3', mode: 'RO', status: 'ENABLED', publicKey: '8888' }
  ];

  let sftpAccountsServiceSpy = jasmine.createSpyObj('SftpAccountsService', ['accounts']);

  sftpAccountsServiceSpy.accounts.and.returnValue(
    asyncData(expectedSftpAccounts.map(x => Object.assign({}, x))));

  let component: SftpAccountsQueryComponent;
  let fixture: ComponentFixture<SftpAccountsQueryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SftpAccountsModule],
      providers: [
        { provide: SftpAccountsService, useValue: sftpAccountsServiceSpy }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountsQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', async(() => {
    expect(component).toBeTruthy();
  }));

  it('should contain expected accounts', async () => {
    component.ngOnInit();

    fixture.whenRenderingDone().then(() => {
      expect(component.accounts).toEqual(expectedSftpAccounts);
    });
  });
});