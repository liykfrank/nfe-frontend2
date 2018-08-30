import { ReactiveFormHandlerModel } from '../../../../../shared/base/reactive-form-handler-model';
import { FormGroup, Validators, FormControl } from '../../../../../../../node_modules/@angular/forms';

export class NewUserModel extends ReactiveFormHandlerModel {

    // CONTROLS
    city: FormControl;
    country: FormControl;
    description: FormControl;
    locality: FormControl;
    zip: FormControl;
    expiryDate: FormControl;
    id: FormControl;
    lastModifiedDate: FormControl;
    name: FormControl;
    organization: FormControl;
    registerDate: FormControl;
    telephone: FormControl;
    userCode: FormControl;
    userType: FormControl;
    username: FormControl;
    isoCountry: FormControl;

    // FORMSGROUPS
    address: FormGroup;
    newUserGroup: FormGroup;
    groupIsoCountry: FormGroup;

    constructor() {
        super();
    }

    createFormControls() {
        this.isoCountry = new FormControl('');
        this.city = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.country = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.description = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.locality = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.zip = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.expiryDate = new FormControl({value: '', disabled: true}, [Validators.required]);
        this.id = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.lastModifiedDate = new FormControl({value: '', disabled: false}, []);
        this.name = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.organization = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.registerDate = new FormControl({value: '', disabled: false}, []);
        this.telephone = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.userCode = new FormControl('', {validators: [Validators.required], updateOn: 'blur'});
        this.userType = new FormControl({value: '', disabled: false}, [Validators.required]);
        this.username = new FormControl({value: '', disabled: false}, [Validators.required, Validators.email]);
    }

    createFormGroups() {
        this.address = this.createGroupAddress();
        this.groupIsoCountry = new FormGroup({
            isoCountry: this.isoCountry
        });
    }

    createForm() {
        if (!this.newUserGroup) {
            this.newUserGroup = new FormGroup({
                address: this.address,
                expiryDate: this.expiryDate,
                id: this.id,
                lastModifiedDate: this.lastModifiedDate,
                name: this.name,
                organization: this.organization,
                registerDate: this.registerDate,
                telephone: this.telephone,
                userCode: this.userCode,
                userType: this.userType,
                username: this.username
            });
        }
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
