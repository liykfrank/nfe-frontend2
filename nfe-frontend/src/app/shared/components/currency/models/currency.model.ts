export class Currency {
  name: string;
  numDecimals: number;
  expirationDate: Date;

  constructor() {
    this.name = 'EUR';
    this.numDecimals = 2;
    this.expirationDate = new Date();
  }
}
