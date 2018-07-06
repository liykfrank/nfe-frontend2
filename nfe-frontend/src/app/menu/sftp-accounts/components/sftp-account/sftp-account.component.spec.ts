import { HttpErrorResponse } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Observable } from 'rxjs/Observable';

import { asyncError } from '../../../../../testing/async-observable-helpers';
import { SharedModule } from '../../../../shared/shared.module';
import { SftpAccountComponent } from './sftp-account.component';


describe('SftpAccountComponent new', () => {
  let component: SftpAccountComponent;
  let fixture: ComponentFixture<SftpAccountComponent>;

  beforeEach(
    async(() => {
      TestBed.configureTestingModule({
        imports: [SharedModule, BrowserAnimationsModule],
        declarations: [SftpAccountComponent],
        providers: [ ]
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

});

