import { UserAddress } from './user-address.model';

export interface User {
  address: UserAddress;
  expiryDate: string;
  id: string;
  lastModifiedDate: string;
  name: string;
  organization: string;
  registerDate: string;
  telephone: string;
  userCode: string;
  userType: string;
  username: string;
}
