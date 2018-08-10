import { KeyValue } from './../../../shared/models/key.value.model';

export class TocaType {
  code: string;
  description: string;
  isoCountryCode: string;

  constructor() {
    this.code = '';
    this.description = '';
    this.isoCountryCode = '';
  }
}
