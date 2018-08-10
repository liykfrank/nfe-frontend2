import { Directive, HostListener, ElementRef } from '@angular/core';

@Directive({
  selector: '[appInputInteger]'
})
export class InputIntegerDirective {

  private el: HTMLInputElement;
  constructor(private elemntRef: ElementRef) {
    this.el = this.elemntRef.nativeElement;
  }

  @HostListener('keydown', ['$event.key'])
  onKeydown(value) {

    if (value == 'Tab' ||
        value == 'Backspace' ||
        value == 'ArrowUp' ||
        value == 'ArrowDown' ||
        value == 'ArrowLeft' ||
        value == 'ArrowRight' ||
        value == 'Delete') {
      return true;
    }

    const reg = new RegExp('[0-9]');

    if (value.match(reg)) {
      return true;
    }

    return false;

  }

}
