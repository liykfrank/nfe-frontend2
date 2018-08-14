import { InputIntegerDirective } from './input-integer.directive';
import { ElementRef } from '@angular/core';

xdescribe('InputIntegerDirective', () => {
  let _el: ElementRef;
  it('should create an instance', () => {
    const directive = new InputIntegerDirective(_el);
    expect(directive).toBeTruthy();
  });
});
