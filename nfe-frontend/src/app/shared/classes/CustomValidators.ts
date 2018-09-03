import { FormControl } from '@angular/forms';
import { GLOBALS } from '../constants/globals';

export class CustomValidators {

    static email(control: FormControl) {
        const email = control.value;
        const regex = new RegExp(GLOBALS.PATTERNS.EMAIL);
        if (email && !email.match(regex)) {
            return {
                emailWithDomain: {
                  parsedEmail: email
                }
              };
        }
        return null;
    }
}
