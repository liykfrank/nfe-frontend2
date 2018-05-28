import { HttpClient } from '@angular/common/http';
import { NwRepositoryAbstract } from '../../../shared/base/nwe-repository.abstract';
import { Injectable, Injector } from '@angular/core';
import { environment } from './../../../../environments/environment';

export class SftpAccountsResourceGen<T> extends NwRepositoryAbstract<T, Object> {
  constructor(http: HttpClient, injector: Injector) {
    super(
      http,
      environment.accounts.basePath + environment.accounts.api.accounts,
      injector
    );
  }
}
