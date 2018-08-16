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

  @Input() airlineFormModelGroup: FormGroup;
  @Input() role;
  @Input() isoCountryCode: string;
  @Input() agentCode: string;

  private _airlineVatNumberEnabled = true;
  @Input()
  set airlineVatNumberEnabled(val: boolean) {
    this._airlineVatNumberEnabled = val;
    if (val) {
      this.airlineFormModelGroup.get('airlineVatNumber').enable({emitEvent: false});
    } else {
      this.airlineFormModelGroup.get('airlineVatNumber').disable({emitEvent: false});
    }
  }
  get airlineVatNumberEnabled() {
    return this._airlineVatNumberEnabled;
  }

  private _companyRegistrationNumberEnabled = true;
  @Input()
  set companyRegistrationNumberEnabled(val: boolean) {
    this._companyRegistrationNumberEnabled = val;
    if (val) {
      this.airlineFormModelGroup.get('airlineRegistrationNumber').enable({emitEvent: false});
    } else {
      this.airlineFormModelGroup.get('airlineRegistrationNumber').disable({emitEvent: false});
    }
  }
  get companyRegistrationNumberEnabled() {
    return this._companyRegistrationNumberEnabled;
  }

  @Input() disabledContact: boolean;
  @Input() showContact: boolean = true;
  @Input() showAirlineName: boolean;
  @Input() showMoreDetails = true;
  @Output() changeAirlineFormModel: EventEmitter<FormGroup> = new EventEmitter();
  @Output() changeAirlineModel: EventEmitter<Airline> = new EventEmitter();
  @Output() clickMoreDetails: EventEmitter<boolean> = new EventEmitter();

  disabledMoreDetails = true;
  airline: Airline = new Airline();

  display = false;

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

          if (this.airlineVatNumberEnabled) {
            this.airlineFormModelGroup.get('airlineVatNumber').setValue(airline.taxNumber);
          }
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
    this.clickMoreDetails.emit(true);
    this.display = true;
  }

  onChangeContact(value) {
    this.airlineFormModelGroup.setControl('airlineContact', value);
  }

  onClose() {
    this.clickMoreDetails.emit(false);
    this.display = false;
  }

  private _clean() {
    this.airlineFormModelGroup.get('airlineVatNumber').reset();
    this.airline = new Airline();
    this.disabledMoreDetails = true;
  }
}
