import { CustomValidators } from './../../../../../shared/classes/CustomValidators';
import { ReactiveFormHandlerModel } from '../../../../../shared/base/reactive-form-handler-model';
import {
  FormGroup,
  Validators,
  FormControl
} from '../../../../../../../node_modules/@angular/forms';

export class NewUserModel extends ReactiveFormHandlerModel {
  // CONTROLS
  city: FormControl;
  country: FormControl;
  description: FormControl;
  locality: FormControl;
  zip: FormControl;
  expiryDate: FormControl;
  lastModifiedDate: FormControl;
  lastname: FormControl;
  name: FormControl;
  organization: FormControl;
  registerDate: FormControl;
  telephone: FormControl;
  userCode: FormControl;
  userType: FormControl;
  username: FormControl;
  email: FormControl;
  isoCountry: FormControl;

  // FORMSGROUPS
  address: FormGroup;
  groupForm: FormGroup;
  groupIsoCountry: FormGroup;

  constructor() {
    super();
  }

  createFormControls() {
    this.isoCountry = new FormControl('');
    this.city = new FormControl({ value: '', disabled: false }, []);
    this.country = new FormControl({ value: '', disabled: false }, []);
    this.description = new FormControl({ value: '', disabled: false }, []);
    this.locality = new FormControl({ value: '', disabled: false }, []);
    this.zip = new FormControl({ value: '', disabled: false }, []);
    this.expiryDate = new FormControl({ value: '', disabled: true }, []);
    this.lastModifiedDate = new FormControl({ value: '', disabled: false }, []);
    this.name = new FormControl({ value: '', disabled: false }, [
      Validators.required
    ]);
    this.lastname = new FormControl({ value: '', disabled: false }, [
      Validators.required
    ]);
    this.organization = new FormControl({ value: '', disabled: false }, [
      Validators.required
    ]);
    this.registerDate = new FormControl({ value: '', disabled: false }, []);
    this.telephone = new FormControl({ value: '', disabled: false }, [
      Validators.required
    ]);
    this.userCode = new FormControl('', {
      validators: [Validators.required],
      updateOn: 'blur'
    });
    this.userType = new FormControl({ value: '', disabled: false }, [
      Validators.required
    ]);
    this.email = new FormControl({ value: '', disabled: false }, [
      Validators.required,
      CustomValidators.email
    ]);
    this.username = new FormControl({ value: '', disabled: false }, [
      Validators.required,
      CustomValidators.email
    ]);
  }

  createFormGroups() {
    this.address = this.createGroupAddress();
    this.groupIsoCountry = new FormGroup({
      isoCountry: this.isoCountry
    });
  }

  createForm() {
    this.groupForm = new FormGroup({
      address: this.address,
      expiryDate: this.expiryDate,
      lastModifiedDate: this.lastModifiedDate,
      name: this.name,
      lastname: this.lastname,
      organization: this.organization,
      registerDate: this.registerDate,
      telephone: this.telephone,
      userCode: this.userCode,
      userType: this.userType,
      email: this.email,
      username: this.username
    });
  }

  createGroupAddress(): FormGroup {
    return new FormGroup({
      city: this.city,
      country: this.country,
      description: this.description,
      locality: this.locality,
      zip: this.zip
    });
  }
}
