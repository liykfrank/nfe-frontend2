import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { HttpServiceAbstract } from '../../../base/http-service-abstract';
import { Agent } from './../models/agent.model';
import { environment } from '../../../../../environments/environment';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AgentService {

  constructor(private http: HttpClient) {}

  public validateAgent(isRefund: boolean, agentCode: string): Observable<Agent> {
    const pathUrl = isRefund ? environment.api.refund.agent : environment.api.adm_acm.agent;
    return this.http.get<Agent>(environment.basePath + pathUrl + agentCode);
  }

}
