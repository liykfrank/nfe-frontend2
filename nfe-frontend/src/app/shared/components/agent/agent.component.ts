import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges, OnChanges } from '@angular/core';
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
export class AgentComponent extends ReactiveFormHandler implements OnInit, OnChanges {

  @Input() agentFormModelGroup: FormGroup;

  @Input() role;
  @Input() agentCode: string;

  private _agentVatNumberEnabled = true;
  @Input()
  set agentVatNumberEnabled(val: boolean) {
    this._agentVatNumberEnabled = val;
    if (val) {
      this.agentFormModelGroup.get('agentVatNumber').enable({emitEvent: false});
    } else {
      this.agentFormModelGroup.get('agentVatNumber').disable({emitEvent: false});
    }
  }
  get agentVatNumberEnabled(): boolean {
    return this._agentVatNumberEnabled;
  }

  private _companyRegistrationNumberEnabled = true;
  @Input()
  set companyRegistrationNumberEnabled(val: boolean) {
    this._companyRegistrationNumberEnabled = val;
    if (val) {
      this.agentFormModelGroup.get('agentRegistrationNumber').enable({emitEvent: false});
    } else {
      this.agentFormModelGroup.get('agentRegistrationNumber').disable({emitEvent: false});
    }
  }
  get companyRegistrationNumberEnabled(): boolean {
    return this._companyRegistrationNumberEnabled;
  }

  private _disabledContact: boolean;
  @Input()
  set disabledContact(val: boolean) {
    this._disabledContact = val;
    if (val) {
      this.agentFormModelGroup.get('agentContact').enable({emitEvent: false});
    } else {
      this.agentFormModelGroup.get('agentContact').disable({emitEvent: false});
    }
  }
  get disabledContact(): boolean {
    return this._disabledContact;
  }

  @Input() showAgentName: boolean;

  private _showContact: boolean = true;
  @Input()
  set showContact(val: boolean) {
    this._showContact = val;
    if (val) {
      this.agentFormModelGroup.get('agentContact').enable({emitEvent: false});
    } else {
      this.agentFormModelGroup.get('agentContact').disable({emitEvent: false});
    }
  }
  get showContact(): boolean {
    return this._showContact;
  }
  @Input() showMoreDetails = true;
  @Output() clickMoreDetails: EventEmitter<any> = new EventEmitter();
  @Output() changeAgent: EventEmitter<Agent> = new EventEmitter();
  @Output() changeAgentFormModel: EventEmitter<AgentFormModel> = new EventEmitter();
  disabledMoreDetails = true;
  agent: Agent = new Agent();

  display = false;

  constructor(private _agentService: AgentService) {
    super();
  }

  ngOnInit() {
    this.subscribe(this.agentFormModelGroup);
    this.subscriptions.push(this.agentFormModelGroup.get('agentCode').valueChanges.subscribe((agentCode: string) => {
      this.agentFormModelGroup.get('agentCode').valid ?  this._validateAgent() : this._clean();
     }));
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.agentCode != '' && changes.agentCode) {
      this.agentFormModelGroup.get('agentCode').setValue(this.agentCode.substring(0, 7));
      this.agentFormModelGroup.get('agentControlDigit').setValue(this.agentCode.substring(7, 8));
      this._validateAgent();
    }
  }

  onClickMoreDetails() {
    this.clickMoreDetails.emit(true);
    this.display = true;
  }

  onChangeContact(value) {
    this.agentFormModelGroup.setControl('agentContact', value);
  }

  onClose() {
    this.clickMoreDetails.emit(false);
    this.display = false;
  }

  private _validateAgent() {
    this._agentService.validateAgent(true, this.getIataCode())
    .subscribe((agent) => {

        this.changeAgent.emit(agent);
        this.agent = agent;

        if (this.agentVatNumberEnabled) {
          this.agentFormModelGroup.get('agentVatNumber').setValue(agent.vatNumber);
        }

        this.agentCode = agent.iataCode;
        this.disabledMoreDetails = false;
      }, error => {
        this._clean();
        this._setErros();
      });
  }

  private _clean() {
    this.agentFormModelGroup.get('agentVatNumber').reset();
    this.agent = new Agent();
    this.disabledMoreDetails = true;
  }

  private _setErros() {
    this.agentFormModelGroup.get('agentCode').setErrors({
      customError: {
        invalid: true,
        message: 'FORM_CONTROL_VALIDATORS.agent'
      }
    });

  }

 private getIataCode(): string {
    return this.agentFormModelGroup.get('agentCode').value + this.agentFormModelGroup.get('agentControlDigit').value;
  }

}
