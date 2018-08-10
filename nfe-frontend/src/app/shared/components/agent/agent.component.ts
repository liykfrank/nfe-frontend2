import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { ReactiveFormHandler } from '../../base/reactive-form-handler';
import { AgentFormModel } from './models/agent-form-model';
import { Agent } from './models/agent.model';
import { AgentService } from './services/agent.service';

@Component({
  selector: 'bspl-agent',
  templateUrl: './agent.component.html',
  styleUrls: ['./agent.component.scss']
})
export class AgentComponent extends ReactiveFormHandler implements OnInit {

  @Input() agentFormModelGroup: FormGroup;

  @Input() role;
  @Input() agentCode: string;
  @Input() disabledAgentCode: boolean;
  @Input() agentVatNumberEnabled: boolean;
  @Input() companyRegistrationNumberEnabled: boolean;
  @Input() disabledContact: boolean;
  @Input() showAgentName: boolean;
  @Input() showContact: boolean;
  @Input() showMoreDetails = true;
  @Output() clickMoreDetails: EventEmitter<any> = new EventEmitter();
  @Output() changeAgent: EventEmitter<Agent> = new EventEmitter();
  @Output() changeAgentFormModel: EventEmitter<AgentFormModel> = new EventEmitter();
  disabledMoreDetails = true;
  agent: Agent = new Agent();

  constructor(private _agentService: AgentService) {
    super();
  }

  ngOnInit() {
    this.subscribe(this.agentFormModelGroup);
    if (this.agentCode && this.agentCode.length == 8) {
       this._validateAgent();
    }
  }

  onClickMoreDetails() {
    this.clickMoreDetails.emit();
  }

  onChangeContact(value) { // TODO setear contacto y ¿emitir?
    this.agentFormModelGroup.setControl('agentContact', value);
  }

  private _validateAgent() {
    this._agentService.validateAgent(true, this.agentCode)
    .subscribe((agent) => {
        this.changeAgent.emit(agent);
        this.agent = agent;
        this.agentFormModelGroup.get('agentCode').setValue(agent.iataCode.substring(0, 7));
        this.agentFormModelGroup.get('agentControlDigit').setValue(agent.iataCode.substring(7, 8));
        this.agentCode = agent.iataCode;
        this.disabledMoreDetails = false;
      }, error => {
        this._clean();
      });
  }

  private _clean() {
    this.agent = new Agent();
    this.disabledMoreDetails = true;
  }

}
