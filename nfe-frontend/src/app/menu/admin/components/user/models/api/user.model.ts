import { UserAddressInterface } from './user-address.model';

export interface UserInterface {

  address: UserAddressInterface;
  email: string;
  expiryDate: string;
  lastModifiedDate: string;
  lastName: string;
  name: string;
  organization: string;
  registerDate: string;
  telephone: string;
  // TODO: templates
  userCode: string;
  userType: string;
  username: string;
}

export class User implements UserInterface {

  constructor(
    public address: UserAddressInterface,
    public email: string,
    public expiryDate: string,
    public lastModifiedDate: string,
    public lastName: string,
    public name: string,
    public organization: string,
    public registerDate: string,
    public telephone: string,
    public userCode: string,
    public userType: string,
    public username: string,
  ) {}


}
