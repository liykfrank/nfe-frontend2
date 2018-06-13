import { environment } from './../../../../../environments/environment';
import { Component, OnInit } from "@angular/core";

@Component({
  selector: 'app-sftp-account',
  templateUrl: './sftp-account.component.html',
  styleUrls: ['./sftp-account.component.scss']
})
export class SftpAccountComponent implements OnInit {
  url: string;

  constructor() { }

  ngOnInit(): void {
    let compose = environment.sftAccount.basePath + environment.sftAccount.api.create;

    compose = compose.replace('\{1\}', 'user');
    console.log(compose);
    compose = compose.replace('\{2\}', 'user@company.com');
    console.log(compose);
    this.url = compose;
  }

}

