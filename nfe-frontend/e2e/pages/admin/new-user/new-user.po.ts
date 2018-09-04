import { browser, by, element } from 'protractor';

import { ROUTES } from './../../../../src/app/shared/constants/routes';

export class NewUser {
  private baseID = 'USER.CREATE.USER.';

  navigateTo() {
    browser.get('#' + ROUTES.NEW_USER.URL);
  }

  selectAgentType() {
    const select = element.all(by.css('div.form-control')).first();
    select.click();

    const optionAgent = element.all(by.css('div.single-option')).first();
    optionAgent.click();
  }

  getInputCode() {
    return element(by.id(this.baseID + 'userCode'));
  }

  getInputName() {
    return element(by.id(this.baseID + 'name'));
  }

  getInputLastname() {
    return element(by.id(this.baseID + 'lastname'));
  }

  getAddress() {
    return element(by.id(this.baseID + 'address'));
  }

  getExpiryDate() {
    return element(by.id(this.baseID + 'expiryDate'));
  }

  getLastModifiedDate() {
    return element(by.id(this.baseID + 'lastModifiedDate'));
  }

  getOrganization() {
    return element(by.id(this.baseID + 'organization'));
  }

  getRegisterDate() {
    return element(by.id(this.baseID + 'registerDate'));
  }

  getTelephone() {
    return element(by.id(this.baseID + 'telephone'));
  }

  getUserType() {
    return element(by.id(this.baseID + 'userType'));
  }

  getEmail() {
    return element(by.id(this.baseID + 'email'));
  }

  getUsername() {
    return element(by.id(this.baseID + 'username'));
  }

  getCity() {
    return element(by.id(this.baseID + 'city'));
  }
  getCountry() {
    return element(by.id(this.baseID + 'country'));
  }
  getDescription() {
    return element(by.id(this.baseID + 'description'));
  }
  getLocality() {
    return element(by.id(this.baseID + 'locality'));
  }
  getZip() {
    return element(by.id(this.baseID + 'zip'));
  }

  // Buttons
  getCreateButton() {
    return element(by.id(this.baseID + 'button.0'));
  }

  // Message OK
  getOK() {
    return element(by.css('div.alert-info'));
  }
}
