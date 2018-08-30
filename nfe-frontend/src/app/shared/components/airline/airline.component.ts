import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { ReactiveFormHandler } from '../../base/reactive-form-handler';
import { AirlineFormModel } from './models/airline-form.model';
import { Airline } from './models/ariline.model';
import { AirlineService } from './services/airline.service';

@Component({
  selector: 'bspl-airline',
  templateUrl: './airline.component.html',
  styleUrls: ['./airline.component.scss']
})
export class AirlineComponent extends ReactiveFormHandler<AirlineFormModel> implements OnInit {

  @Input() role;
  @Input() isoCountryCode: string;
  @Input() agentCode: string;

  @Input() airlineVatNumberEnabled: boolean = true;
  @Input() companyRegistrationNumberEnabled: boolean = true;
  @Input() disabledContact: boolean;
  @Input() showContact: boolean = true;
  @Input() showAirlineName: boolean;
  @Input() showMoreDetails = true;
  @Output() changeAirlineFormModel: EventEmitter<FormGroup> = new EventEmitter();
  @Output() changeAirlineModel: EventEmitter<Airline> = new EventEmitter();

  disabledMoreDetails = true;
  airline: Airline = new Airline();

  display = false;

  constructor(private _airlineService: AirlineService) {
    super();
  }

  ngOnInit() {
    this.subscriptions.push(
      this.model.airlineCode
        .valueChanges.subscribe((airlineCode: string) => {
          if (this.model.airlineCode.valid) {
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

          if (this.airlineVatNumberEnabled) {
            this.model.airlineVatNumber.setValue(airline.taxNumber);
          }
        },
        error => {
          this._clean();
          this.model.airlineCode.setErrors({
            customError: {
              invalid: true,
              message: 'FORM_CONTROL_VALIDATORS.airline'
            }
          });
        }
      );
  }

  onClickMoreDetails() {
    this.display = true;
  }

  onChangeContact(value) {
    this.model.airlineFormModelGroup.setControl('airlineContact', value);
  }

  onClose() {
    this.display = false;
  }

  private _clean() {
    this.model.airlineVatNumber.reset();
    this.airline = new Airline();
    this.disabledMoreDetails = true;
  }
}
