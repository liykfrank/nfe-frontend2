import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ReactiveFormHandler } from '../../base/reactive-form-handler';
import { AirlineFormModel } from './models/airline-form.model';
import { Airline } from './models/ariline.model';
import { AbstractComponent } from '../../base/abstract-component';
import { AirlineService } from './services/airline.service';

@Component({
  selector: 'bspl-airline',
  templateUrl: './airline.component.html',
  styleUrls: ['./airline.component.scss']
})
export class AirlineComponent extends ReactiveFormHandler implements OnInit {

  airlineFormModelGroup: FormGroup = new AirlineFormModel().airlineFormModelGroup;

  @Input() role;
  @Input() isoCountryCode: string;
  @Input() agentCode: string;
  @Input() airlineVatNumberEnabled: boolean;
  @Input() companyRegistrationNumberEnabled: boolean;
  @Input() disabledContact: boolean;
  @Input() showContact: boolean;
  @Input() showAirlineName: boolean;
  @Input() showMoreDetails = true;
  @Output() changeAirlineFormModel: EventEmitter<FormGroup> = new EventEmitter();
  @Output() changeAirlineModel: EventEmitter<Airline> = new EventEmitter();
  @Output() clickMoreDetails: EventEmitter<any> = new EventEmitter();

  disabledMoreDetails = true;
  airline: Airline = new Airline();

  constructor(private _airlineService: AirlineService) {
    super();
  }

  ngOnInit() {
    this.subscribe(this.airlineFormModelGroup);
    this.subscriptions.push(
      this.airlineFormModelGroup
        .get('airlineCode')
        .valueChanges.subscribe((airlineCode: string) => {
          if (this.airlineFormModelGroup.get('airlineCode').valid) {
            this._validateAirline(airlineCode);
          } else {
            this._clean();
          }
        })
    );
  }

  private _validateAirline(airlineCode: string) {
    this._airlineService
      .validateAirlinet(true, this.isoCountryCode, airlineCode)
      .subscribe(
        airline => {
          this.airline = airline;
          this.changeAirlineModel.emit(airline);
          this.disabledMoreDetails = false;
        },
        error => {
          this._clean();
          this.airlineFormModelGroup.get('airlineCode').setErrors({
            customError: {
              invalid: true,
              message: 'FORM_CONTROL_VALIDATORS.airline'
            }
          });
        }
      );
  }

  onClickMoreDetails() {
    this.clickMoreDetails.emit();
  }

  onChangeContact(value) {
    this.airlineFormModelGroup.setControl('airlineContact', value);
  }

  private _clean() {
    this.airline = new Airline();
    this.disabledMoreDetails = true;
  }
}
