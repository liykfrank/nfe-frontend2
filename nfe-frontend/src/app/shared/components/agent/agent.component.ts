import { EnvironmentType } from './../../enums/environment-type.enum';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import { FormGroup } from '@angular/forms';

import { ReactiveFormHandler } from '../../base/reactive-form-handler';
import { AgentFormModel } from './models/agent-form-model';
import { Agent } from './models/agent.model';
import { AgentService } from './services/agent.service';
import { GLOBALS } from '../../constants/globals';

@Component({
  selector: 'bspl-agent',
  templateUrl: './agent.component.html',
  styleUrls: ['./agent.component.scss']
})
export class AgentComponent extends ReactiveFormHandler<AgentFormModel> implements OnInit, OnChanges {
  agentFormModelGroup: FormGroup;

  PT_NUMERIC = GLOBALS.HTML_PATTERN.NUMERIC;
  PT_ALPHA = GLOBALS.HTML_PATTERN.ALPHANUMERIC_LOWERCASE;

  @Input() role;
  @Input() agentCode: string;

  @Input() agentVatNumberEnabled: boolean = true;
  @Input() companyRegistrationNumberEnabled: boolean = true;
  @Input() disabledContact: boolean = false;
  @Input() showAgentName: boolean = false;
  @Input() showContact: boolean = true;
  @Input() showMoreDetails: boolean = true;

  @Output() changeAgent: EventEmitter<Agent> = new EventEmitter();
  @Output() changeAgentFormModel: EventEmitter<AgentFormModel> = new EventEmitter();

  disabledMoreDetails = true;
  agent: Agent = new Agent();
  display = false;

  constructor(private _agentService: AgentService) {
    super();
  }

  ngOnInit() {
    this.agentFormModelGroup = this.model.agentFormModelGroup;

    this.subscriptions.push(
      this.model.agentCode.valueChanges.subscribe(() => {
          this.model.agentCode.valid
            ? this._validateAgent()
            : this._clean();
        })
    );

    this.subscriptions.push(
      this.model.agentControlDigit.valueChanges.subscribe(() => {
          this.model.agentControlDigit.valid
            ? this._validateAgent()
            : this._clean();
        })
    );
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.agentCode != '' && changes.agentCode) {
      this.model.agentCode
        .setValue(this.agentCode.substring(0, 7));
      this.model.agentControlDigit
        .setValue(this.agentCode.substring(7, 8));

      this._validateAgent();
    }
  }

  onClickMoreDetails() {
    this.display = true;
  }

  onClose() {
    this.display = false;
  }

  private _validateAgent() {
    const iataCode = this.getIataCode();

    if (iataCode != null) {
      const validMod = (Number(this.model.agentCode.value) % 7) == Number(this.model.agentControlDigit.value);
      if (!validMod) {
        this.model.agentControlDigit.setErrors({
          customError: {
            invalid: true,
            message: 'FORM_CONTROL_VALIDATORS.agent_checkDigit'
          }
        });
        return;
      }

      this._agentService.validateAgent(EnvironmentType.REFUND_INDIRECT, iataCode).subscribe(
        agent => {
          this.changeAgent.emit(agent);
          this.agent = agent;

          if (this.agentVatNumberEnabled) {
            this.model.agentVatNumber.setValue(agent.vatNumber);
          }

          this.agentCode = agent.iataCode;
          this.disabledMoreDetails = false;
        },
        () => {
          this._clean();
          this._setErros();
        }
      );
    }
  }

  private _clean() {
    this.model.agentVatNumber.reset();
    this.agent = new Agent();
    this.disabledMoreDetails = true;
  }

  private _setErros() {
    this.model.agentCode.setErrors({
      customError: {
        invalid: true,
        message: 'FORM_CONTROL_VALIDATORS.agent'
      }
    });
  }

  private getIataCode(): string {
    const agentCode = this.model.agentCode.value;
    const agentControlDigit = this.model.agentControlDigit.value;

    if (
      agentCode &&
      agentControlDigit &&
      agentCode.length == 7 &&
      agentControlDigit.length == 1
    ) {
      return agentCode + agentControlDigit;
    }

    return null;

  }
}
