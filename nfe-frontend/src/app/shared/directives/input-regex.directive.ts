import { Directive, HostListener, ElementRef, Input } from '@angular/core';
import { NgControl } from '@angular/forms';
import { GLOBALS } from '../constants/globals';

@Directive({
  selector: '[bsplInputRegex]'
})
export class InputRegexDirective {
  private el: HTMLInputElement;
  private specialKeys: Array<string> = ['Backspace', 'Tab', 'End', 'Home'];

  @Input('regexPattern')
  regexPattern;
  constructor(private elemntRef: ElementRef, private _control: NgControl) {
    this.el = this.elemntRef.nativeElement;
  }

  @HostListener('keypress', ['$event'])
  onKeypress(event: KeyboardEvent): boolean {

    if (this.specialKeys.indexOf(event.key) !== -1) {
      return;
    }

    const regex = new RegExp(this.regexPattern);

    if (!String(event.key).match(regex) && !String(event.key).toUpperCase().match(regex)) {
      event.preventDefault();
    }
  }


  @HostListener('keyup', ['$event'])
  onKeyup(event: KeyboardEvent) {

    if (this.regexPattern.indexOf('a-z') === -1) {
      this.el.value = this.el.value.toUpperCase();
      this._control.control.setValue(this.el.value, GLOBALS.REACTIVE_FORMS.EMIT_EVENT_FALSE);
    }
    event.preventDefault();

  }
}
