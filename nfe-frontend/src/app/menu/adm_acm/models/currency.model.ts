import { KeyValue } from "../../../shared/models/key.value.model";

export class Currency extends KeyValue {
  name: string;
  numDecimals: number;
  expirationDate: Date;
}
