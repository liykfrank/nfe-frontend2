import { Directive, HostListener, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[bsplInputRegex]'
})
export class InputRegexDirective {

  private el: HTMLInputElement;

  private lastText = '';

  @Input('regexPattern') regexPattern;
  constructor(private elemntRef: ElementRef) {
    this.el = this.elemntRef.nativeElement;
  }

  @HostListener('keyup', ['$event'])
  onKeyup(value) {

    let text = value.srcElement.value;

    if (text == '') {
      this.lastText = '';
      this.el.value = this.lastText;
      return false;
    }

    const reg = new RegExp(this.regexPattern);
    text = this.isLowerCase(text);




    if (reg.test(text)) {
      this.lastText = text;
      this.el.value = text;
      return true;
    } else {
      this.el.value = this.lastText;
      return false;
    }
  }

  isLowerCase(value: string): string {

    const reg = new RegExp('[a-z]');

    if (!this.regexPattern.match(reg)) {
      value = value.toUpperCase();
    }
    return value;
  }

}
