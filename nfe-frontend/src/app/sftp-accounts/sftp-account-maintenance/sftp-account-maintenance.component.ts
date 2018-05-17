import { Component, OnInit, ViewChild } from '@angular/core';
import { SftpAccount } from '../models/sftp-account';
import { SftpAccountsService } from '../services/sftp-accounts.service';
import { SftpAccountGridComponent } from '../sftp-account-grid/sftp-account-grid.component';

@Component({
  selector: 'app-sftp-account-maintenance',
  templateUrl: './sftp-account-maintenance.component.html',
  styleUrls: ['./sftp-account-maintenance.component.scss']
})
export class SftpAccountMaintenanceComponent implements OnInit {
  @ViewChild("sftpGrid") sftpGrid: SftpAccountGridComponent;
  sftpAccount: SftpAccount;
  selAccount: SftpAccount;
  accounts: SftpAccount[];

  constructor(private accountService: SftpAccountsService) {
    this.selAccount = null;
  }

  ngOnInit() {
    this.accountService.accounts().subscribe(
      accounts => this.accounts = accounts
    );
  }

  onSelect(acc) {
    this.selAccount = acc;
    this.sftpAccount = new SftpAccount();
    for (let k in this.sftpAccount) this.sftpAccount[k] = acc[k];
  }

  newClicked() {
    this.sftpAccount = new SftpAccount();
  }

  cancelClicked(acc) {
    this.cancel();
  }

  private cancel() {
    this.sftpAccount = null;
    if (this.selAccount != null) {
      this.sftpGrid.unselectRow();
      this.selAccount = null;
    }
  }

  deleteClicked(accToDelete: SftpAccount) {
    let { login } = accToDelete;
    this.accountService.deleteAccount(login).subscribe(() => {
      let index = this.accounts.findIndex(a => a.login == login);
      this.accounts.splice(index, 1);
      console.log("deleted");
      console.log("grid " + this.sftpGrid);
      this.sftpGrid.grid.updatebounddata();
    })
    this.cancel();
  }

  saveClicked(accToSave: SftpAccount) {
    let acc = this.sftpAccount;
    if (this.selAccount != null) {
      this.accountService.putAccount(accToSave).subscribe(() => {
        Object.assign(this.selAccount, accToSave);
        this.cancel();
      });
    } else {
      this.accountService.createAccount(accToSave).subscribe(() => {
        this.accounts.push(accToSave);
        console.log("saved");
        console.log("grid " + this.sftpGrid);
        this.sftpGrid.grid.updatebounddata();
        this.cancel();
      })
    }
  }
}
