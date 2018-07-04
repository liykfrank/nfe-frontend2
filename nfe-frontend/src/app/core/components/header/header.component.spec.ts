/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { HeaderComponent } from './header.component';
import { MenuComponent } from '../menu/menu.component';
import { TabsStateService } from '../../services/tabs-state.service';
import { SharedModule } from '../../../shared/shared.module';
import { MessageService } from 'primeng/components/common/messageservice';
import { SftpModifyPasswordService } from '../../services/sftp-modify-password.service';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule
      ],
      declarations: [
        HeaderComponent,
        MenuComponent
      ],
      providers: [
        TabsStateService,
        MessageService,
        SftpModifyPasswordService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
