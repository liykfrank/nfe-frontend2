import { Component, OnInit } from '@angular/core';

import { environment } from '../../../environments/environment';

@Component({
  selector: 'bspl-sftp-account',
  templateUrl: './sftp-account.component.html',
  styleUrls: ['./sftp-account.component.scss']
})
export class SftpAccountComponent implements OnInit {
  url: string;

  constructor() {}

  ngOnInit(): void {
    let compose =
      environment.sftAccount.basePath + environment.sftAccount.api.create;
    compose = compose.replace('{1}', 'user');

    compose = compose.replace('{2}', 'user@company.com');

    this.url = compose;
  }
}
