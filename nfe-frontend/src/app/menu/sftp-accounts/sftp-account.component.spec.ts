import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { SafeUrlPipe } from '../../shared/pipes/safe-url.pipe';
import { SftpAccountComponent } from './sftp-account.component';

describe('SftpAccountComponent new', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [BrowserAnimationsModule],
      declarations: [SafeUrlPipe, SftpAccountComponent],
      providers: []
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SftpAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
