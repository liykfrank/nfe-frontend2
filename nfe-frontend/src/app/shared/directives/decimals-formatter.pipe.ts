import { Pipe, PipeTransform } from '@angular/core';


@Pipe({
  name: 'decimalsFormatter'
})
export class DecimalsFormatterPipe implements PipeTransform {

  private DECIMAL_SEPARATOR: string;
  private THOUSANDS_SEPARATOR: string;
  private PADDING = '00000000';

  constructor() {
    // TODO comes from configuration settings
    this.DECIMAL_SEPARATOR = '.';
    this.THOUSANDS_SEPARATOR = ',';
  }

  transform(value: number | string, fractionSize: number = 2): string {
    value = value ? value : '0';

    let [integer, fraction = ''] = value.toString()
      .split(this.DECIMAL_SEPARATOR);

    integer = integer.replace(/\B(?=(\d{3})+(?!\d))/g, this.THOUSANDS_SEPARATOR);

    fraction = fractionSize > 0
      ? this.DECIMAL_SEPARATOR + (fraction + this.PADDING).substring(0, fractionSize)
      : '';

    return integer + fraction;
  }

  parse(value: string, fractionSize: number = 2): string {
    value = value ? value : '';
    value = Number(value) === 0 ? '' : value;

    let [integer, fraction = ''] = value.split(this.DECIMAL_SEPARATOR);

    integer = integer.replace(new RegExp (this.THOUSANDS_SEPARATOR, 'g'), '');

    fraction = parseInt(fraction, 10) > 0 && fractionSize > 0
      ? this.DECIMAL_SEPARATOR + (fraction + this.PADDING).substring(0, fractionSize)
      : '';

    return integer + fraction;
  }

}
