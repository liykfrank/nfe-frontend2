import { Component, OnInit, ViewChild } from '@angular/core';
import { SftpAccount } from '../models/sftp-account';
import { SftpAccountsService } from '../services/sftp-accounts.service';

@Component({
    selector: 'app-sftp-accounts-query',
    templateUrl: './sftp-accounts-query.component.html',
    styleUrls: ['./sftp-accounts-query.component.scss']
})
export class SftpAccountsQueryComponent implements OnInit {
    accounts: SftpAccount[];

    constructor(private accountService: SftpAccountsService) {
    }

    ngOnInit(): void {
        this.accountService.accounts().subscribe(accounts => {
            this.accounts = accounts;
        });
    }
}
