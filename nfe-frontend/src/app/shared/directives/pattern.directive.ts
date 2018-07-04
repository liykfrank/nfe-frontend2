import { Directive, ElementRef, HostListener, OnInit, Input, Output, EventEmitter } from '@angular/core';



@Directive({
  selector: '[ownPattern]'
})
export class PatternDirective implements OnInit {
  @Input() typingPattern: string;
  @Output() patternOnBlur: EventEmitter<any> = new EventEmitter();

  private el: HTMLInputElement;

  constructor (private elementRef: ElementRef) {
    this.el = this.elementRef.nativeElement;
  }

  ngOnInit(): void {
    this.convert();
  }

  @HostListener('blur', ['$event'])
  onBlur(event: any) {
    this.convert();
  }

  private convert() {
    let value = this.el.value;
    value = value.toUpperCase();
    this.el.value = value;
    const match = value.match(new RegExp(this.typingPattern));

    if (match && match.length > 0) {
      this.patternOnBlur.emit();
    } else {
      this.el.value = '';
    }
  }

}
