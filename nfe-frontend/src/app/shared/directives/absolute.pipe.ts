import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'abs'
})
export class AbsolutePipe implements PipeTransform {
  transform(value: number): number {
     return Math.abs(value);
  }
}
