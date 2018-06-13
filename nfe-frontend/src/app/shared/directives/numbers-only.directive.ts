import { Directive, HostListener, ElementRef } from '@angular/core';

@Directive({
  selector: '[appNumbersOnly]'
})
export class NumbersOnlyDirective {
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home'];
  private regex: RegExp = new RegExp(/^[0-9]*$/g);
  private currentValue = '';

  constructor(private el: ElementRef) {}

  @HostListener('paste', ['$event'])
  onPaste(event) {
    const pastedText = event.clipboardData.getData('text');

    if (!pastedText.match(this.regex)) {
      event.preventDefault();
    }
  }

  @HostListener('input', ['$event'])
  onInput(event) {
    if (!this.el.nativeElement.value.match(this.regex)) {
      event.srcElement.value = this.currentValue;
    } else {
      this.currentValue = this.el.nativeElement.value;
    }
  }

  // @HostListener('keydown', ['$event'])
  // onKeydown(event) {
  //   console.log(event);
  //   if (this.specialKeys.indexOf(event.key) !== -1) {
  //     return true;
  //   }

  //   let current: string = this.el.nativeElement.value;
  //   let next: string = current.concat(event.key);
  //   if (next && !String(next).match(this.regex)) {
  //     event.preventDefault();
  //   }
  // }
}
