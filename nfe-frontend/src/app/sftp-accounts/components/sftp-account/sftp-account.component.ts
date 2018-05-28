
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm, NgModel } from "@angular/forms";
import { Message } from 'primeng/components/common/api';

import { SftpAccount } from '../../models/sftp-account.model';
import { SftpAccountsService } from '../../services/sftp-accounts.service';
import { SftpAccountPassword } from './../../models/sftp-account-password.model';

@Component({
  selector: 'app-sftp-account',
  templateUrl: './sftp-account.component.html',
  styleUrls: ['./sftp-account.component.scss']
})
export class SftpAccountComponent implements OnInit {
  @ViewChild('sftpAccountForm') form: NgForm;
  @ViewChild('changePasswordForm') form2: NgForm;
  @ViewChild('oldPassword') oldPasswordInput: NgModel;
  @ViewChild('password') passwordInput: NgModel;


  sftpAccount: SftpAccount;
  sftpAccountPassword: SftpAccountPassword;
  accountStatus: any[];
  sftpAccountAlreadyExists: boolean;
  changePwd: boolean;
  showOldPasswordError: boolean;
  msgs: Message[];

  constructor(private accountService: SftpAccountsService) {
    this.accountStatus = [
      { label: 'ENABLED', value: 'ENABLED' },
      { label: 'DISABLED', value: 'DISABLED' }/*,
      { label: 'EXPIRED', value: 'EXPIRED' }*/
    ];
    this.msgs = [];
    this.changePwd = false;
    this.sftpAccountPassword = new SftpAccountPassword('', '', '');
    this.showOldPasswordError = false;
  }

  ngOnInit(): void {
    this.accountService
      .accounts()
      .subscribe(accounts => this.setSftpAccount(accounts));
  }

  save() {
    if (this.sftpAccountAlreadyExists) {
      this.accountService
        .modifyAccount(this.sftpAccount)
        .subscribe(
          response => this.savedSuccessfully('modified'),
          error => this.showBackEndError(error)
        );
    } else {
      this.accountService
        .createAccount(this.sftpAccount)
        .subscribe(
          response => this.savedSuccessfully('created'),
          error => this.showBackEndError(error)
        );
    }
  }

  delete() {
    this.accountService
      .deleteAccount(this.form.value.login)
      .subscribe(
        response => this.deletedSuccessfully(),
        error => this.showBackEndError(error)
      );


  }

  changePassword() {
    this.changePwd = true;
  }

  savePassword() {
    if (this.passwordsMatch()) {
      this.accountService
        .changePassword(this.sftpAccount, this.sftpAccountPassword)
        .subscribe(response => this.passwordChangedSuccessfully(),
      error => this.passwordError(error) );
    }
  }

  cancelChangePassword() {
    this.changePwd = false;
    this.resetPasswordForm();
  }

  passwordsMatch() {
    return this.sftpAccountPassword.password == this.sftpAccountPassword.confirmPassword;
  }

  private setSftpAccount(sftpAccounts: SftpAccount[]) {
    if (sftpAccounts.length > 0) {
      this.sftpAccount = sftpAccounts[0];
      this.sftpAccount.mode = 'RO';
      this.sftpAccountAlreadyExists = true;
    } else {
      this.sftpAccount = new SftpAccount('', '', '');
      this.sftpAccountAlreadyExists = false;
      this.sftpAccount.status = 'ENABLED';
    }
  }

  private savedSuccessfully(type) {
    let msg: string;

    if (type == 'modified') {
      msg = 'Sftp Account modified correctly';
    } else if (type == 'created') {
      msg = 'Sftp Account created correctly';
    }

    this.msgs = [];
    this.msgs.push({
      severity: 'success',
      summary: msg
    });
    this.sftpAccountAlreadyExists = true;
  }

  private deletedSuccessfully() {
    this.resetSftpAccountsForm();

    this.accountService
    .accounts()
    .subscribe(accounts => this.setSftpAccount(accounts));

    this.msgs.push({
      severity: 'success',
      summary: 'Sftp Account deleted correctly'
    });

    this.sftpAccountAlreadyExists = false;
  }

  private passwordChangedSuccessfully(){
    this.changePwd = false;
    this.resetPasswordForm();
    this.msgs.push({severity: 'success', summary: 'Password', detail: 'has been changed successfully'});
  }

  private resetSftpAccountsForm() {
    this.sftpAccount.login = '';
    this.sftpAccount.status = '';
    this.sftpAccount.publicKey = '';
    this.msgs = [];

  }

  private resetPasswordForm() {
    this.sftpAccountPassword = new SftpAccountPassword('', '', '');
    this.showOldPasswordError = false;
    this.msgs = [];
  }

  private passwordError(error: any) {
    if(error.error.message == 'Incorrect password') {
      this.showOldPasswordError = true;
    } else {
      this.showBackEndError(error);
    }
  }

  private showBackEndError(error: any) {
    alert(JSON.stringify(error));
  }

}

