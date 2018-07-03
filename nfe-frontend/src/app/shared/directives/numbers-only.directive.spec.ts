import { ElementRef } from '@angular/core';
import { NumbersOnlyDirective } from './numbers-only.directive';

describe('NumbersOnlyDirective', () => {
  it('should create an instance', () => {
    let el: ElementRef = new ElementRef('')
    const directive = new NumbersOnlyDirective(el);
    expect(directive).toBeTruthy();
  });
});
