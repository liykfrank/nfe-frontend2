import { Directive, HostListener, ElementRef, OnInit, Input, OnChanges, SimpleChanges } from '@angular/core';
import { DecimalsFormatterPipe } from './decimals-formatter.pipe';

@Directive({
  selector: '[decimalsFormatterDirective]'
})
export class DecimalsFormatterDirectiveDirective implements OnInit, OnChanges {

  private el: HTMLInputElement;

  @Input() decimals: number;

  constructor(
    private elemntRef: ElementRef,
    private _DecimalsFormatterPipe: DecimalsFormatterPipe
  ) {
    this.el = this.elemntRef.nativeElement;
  }

  ngOnInit() {
    this.setValueWithDecimals();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.decimals) {
      this.setValueWithDecimals();
    }
  }

  setValueWithDecimals() {
    let transformed = this._DecimalsFormatterPipe.transform(this.el.value, this.decimals);
    setTimeout(() => this.el.value = transformed, 0);
  }

  @HostListener('focus', ['$event.target.value'])
  onFocus(value) {
    this.el.value = this._DecimalsFormatterPipe.parse(value, this.decimals);
  }

  @HostListener('blur', ['$event.target.value'])
  onBlur(value) {
    let transformed = this._DecimalsFormatterPipe.transform(value, this.decimals);
    setTimeout(() => this.el.value = transformed, 0);
  }
}
