import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SftpAccountComponent } from './sftp-account.component';
import { SftpAccountsModule } from '../sftp-accounts.module';
import { SftpAccount } from '../models/sftp-account';

describe('SftpAccountComponent new', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SftpAccountsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    component.isNew = true;
    component.sftpAccount = new SftpAccount();
    fixture.detectChanges();
    component.ngAfterViewInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register', () => {
    component.onRegister.subscribe(x => {
      expect(x).toEqual({ login: '', mode: '', status: '', publicKey: '' });
    });
    component.register();
  });

  it('should cancel', () => {
    component.onCancel.subscribe(x => {
      expect(x).toBeDefined();
    });
    component.cancel();
  });

  it('should delete', () => {
    component.onDelete.subscribe(x => {
      expect(x).toBeDefined();
    });
    component.delete();
  });

});


describe('SftpAccountComponent update', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SftpAccountsModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    component.isNew = false;
    component.sftpAccount = { login: 'A', mode: 'RO', status: 'ENABLED', publicKey: 'A KEY' };
    fixture.detectChanges();
    component.ngAfterViewInit();
    fixture.detectChanges();
    component.ngOnChanges({});
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register', () => {
    component.onRegister.subscribe(x => {
      expect(x).toEqual({ login: 'A', mode: 'RO', status: 'ENABLED', publicKey: 'A KEY' });
    });
    component.register();
  });

  it('should cancel', () => {
    component.onCancel.subscribe(x => {
      expect(x).toBeDefined();
    });
    component.cancel();
  });

  it('should delete', () => {
    component.onDelete.subscribe(x => {
      expect(x).toBeDefined();
      expect(x.login).toEqual('A');
    });
    component.delete();
  });
});