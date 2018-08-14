import { Directive, HostListener, ElementRef, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { DecimalsFormatterPipe } from '../pipes/decimals-formatter.pipe';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[decimalsFormatterDirective]'
})
export class DecimalsFormatterDirective implements OnInit, OnChanges {


  @Input('maxLength') maxLength: number = 11;
  private el: HTMLInputElement;
  private value_aux;

  @Input() decimals: number = 0;

  constructor(
    private elemntRef: ElementRef,
    private _DecimalsFormatterPipe: DecimalsFormatterPipe,
    private _control: NgControl
  ) {
    this.el = this.elemntRef.nativeElement;
  }

  ngOnInit() {
    this.value_aux = this.el.value;
    this.setValueWithDecimals();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.decimals) {
      this.onBlur(null);
    }
  }

  setValueWithDecimals() {
    if (!this.el.disabled) {
      const transformed = this._DecimalsFormatterPipe.transform(this.el.value, this.decimals);

      setTimeout(() => this.el.value = transformed, 0);
    }
  }

  @HostListener('focus', ['$event.target.value'])
  onFocus(value) {
    this.el.value = this._DecimalsFormatterPipe.parse(value, this.decimals);
  }

  @HostListener('blur', ['$event.target.value'])
  onBlur(value) {
    const transformed = this._DecimalsFormatterPipe.transform(this.value_aux, this.decimals);
    this._control.control.setValue(this._DecimalsFormatterPipe.parse(transformed, this.decimals));
    setTimeout(() => this.el.value = transformed, 0);
  }

  @HostListener('keyup', ['$event.target.value'])
  onKeyup(value) {
    if (this.checkLength(this.el.value)) {
      this.value_aux = this.el.value;
    }
    this.el.value = this.value_aux;
  }

  @HostListener('keydown', ['$event.key'])
  onKeydown(value) {
    if (this.checkKeys(value)) {
      return true;
    }

    const reg = new RegExp('[0-9.]');
    const list = this.el.value.split('.');

    if ((value.match(reg) || []).length == 0 || (value == '.' && list.length == 2)) {
      return false;
    }
  }

  checkKeys(value) {
    return value == 'Tab'
      || value == 'Backspace'
      || value == 'Delete'
      || value == 'ArrowUp'
      || value == 'ArrowLeft'
      || value == 'ArrowDown'
      || value == 'ArrowRight';
  }

  checkLength(value) {
    const maxLength = this.maxLength - (this.decimals && !value.toString().includes('.') ? this.decimals : 0);
    const value_replaced = value.toString().replace('.', '');
    return value_replaced.length <= maxLength;
  }

}
