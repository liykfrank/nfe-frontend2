import { EnvironmentType } from './../../../enums/environment-type.enum';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Agent } from './../models/agent.model';
import { environment } from '../../../../../environments/environment';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AgentService {

  constructor(private http: HttpClient) {}

  public validateAgent(type: EnvironmentType, agentCode: string): Observable<Agent> {
    let pathUrl;

    switch (type) {
      case EnvironmentType.REFUND_INDIRECT:
        pathUrl = environment.api.refund.agent;
        break;
      case EnvironmentType.ACDM:
        pathUrl = environment.api.adm_acm.agent;
        break;
      case EnvironmentType.MASTER_AGENT:
        pathUrl = environment.api.masterData.agent;
        break;
    }
    return this.http.get<Agent>(environment.basePath + pathUrl + agentCode);
  }

}
