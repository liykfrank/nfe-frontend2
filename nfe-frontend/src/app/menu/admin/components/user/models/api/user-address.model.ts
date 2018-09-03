export interface UserAddressInterface {

  city: string;
  country: string;
  description: string;
  locality: string;
  zip: string;
}

export class UserAddress {

  constructor(
    public city: string,
    public country: string,
    public description: string,
    public locality: string,
    public zip: string,
  ) {}
}
