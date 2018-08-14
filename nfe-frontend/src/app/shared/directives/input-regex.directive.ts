import { Directive, HostListener, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[bsplInputRegex]'
})
export class InputRegexDirective {
  private el: HTMLInputElement;
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home'];

  @Input('regexPattern')
  regexPattern;
  constructor(private elemntRef: ElementRef) {
    this.el = this.elemntRef.nativeElement;
  }

  @HostListener('keypress', ['$event'])
  onKeypress(event: KeyboardEvent): boolean {

    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }

    const regex = new RegExp(this.regexPattern);
    const current: string = this.el.value;
    const next: string = current.concat(event.key);

    if (next && !String(next).match(regex)) {
      event.preventDefault();
    }
  }
}
