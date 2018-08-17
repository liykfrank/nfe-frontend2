export class Currency {
  name: string;
  numDecimals: number;
  expirationDate: Date;

  constructor() {
    this.name = '';
    this.numDecimals = 0;
    this.expirationDate = new Date();
  }
}
