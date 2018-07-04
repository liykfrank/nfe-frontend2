import { AgentModel } from './../../models/agent.model';
import { Injectable, Injector } from '@angular/core';
import { NwRepositoryAbstract } from '../../../../shared/base/nwe-repository.abstract';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../environments/environment';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs';
import { AlertsService } from '../../../../core/services/alerts.service';
import { AlertType } from '../../../../core/models/alert-type.enum';

@Injectable()
export class AgentService  extends NwRepositoryAbstract<AgentModel, Object> {

  private $agentFromCode = new BehaviorSubject<AgentModel>(null);
  private base = environment.basePath + environment.adm_acm.basePath + environment.adm_acm.api.agent;

  constructor(private http: HttpClient, injector: Injector, private _AlertsService: AlertsService) {
    super(
      http,
      environment.basePath +
      environment.adm_acm.basePath +
      environment.adm_acm.api.agent,
      injector
    );
  }

  public getAgentWithCode(code: string): void {
    this.configureUrl(this.getUrl([code]));

    this.get()
      .finally(() => this.configureUrl(this.base))
      .subscribe(
        (resp) => {
          this.$agentFromCode.next(resp);
        }, err => {
          this.$agentFromCode.next(new AgentModel());
          this._AlertsService.setAlertTranslate('ADM_ACM.SVCS.AGENT.title', 'ADM_ACM.SVCS.AGENT.desc', AlertType.ERROR);
        });
  }

  public getAgent(): Observable<AgentModel> {
    return this.$agentFromCode.asObservable();
  }

}
