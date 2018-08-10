import { FormGroup, FormControl, Validators } from '@angular/forms';

export class AcdmBasicInfoFormModel {
  private _basicInfoModelGroup: FormGroup;

  get basicInfoFormModelGroup(): FormGroup {
    if (!this._basicInfoModelGroup) {
      this._initialize();
    }
    return this._basicInfoModelGroup;
  }

  private _initialize() {
    this._basicInfoModelGroup = this._newBasicInfoForm();
  }

  private _newBasicInfoForm() {
    return new FormGroup({
      isoCountryCode: new FormControl('', [Validators.required]),
      billingPeriod: new FormControl('', [Validators.required]),
      concernsIndicator: new FormControl('', [Validators.required]),
      currency: new FormGroup({
        code: new FormControl('', [Validators.required]),
        decimals: new FormControl('', [Validators.required])
      }),
      taxOnCommissionType: new FormControl('', [Validators.required]),
      statisticalCode: new FormControl('', [Validators.required])
    });
  }
}
