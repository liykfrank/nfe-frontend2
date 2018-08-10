import { ElementRef, Renderer2 } from '@angular/core';
import { IdentifierDirective } from './identifier.directive';

xdescribe('IdentifierDirective', () => {
  let _renderer: Renderer2;
  let _el: ElementRef;

  it('should create an instance', () => {
    const directive = new IdentifierDirective(_renderer, _el);
    expect(directive).toBeTruthy();
  });
});
