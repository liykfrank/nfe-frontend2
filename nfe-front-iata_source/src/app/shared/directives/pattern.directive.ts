import {
  Directive,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output,
  Renderer2
} from '@angular/core';

@Directive({
  selector: '[ownPattern]'
})
export class PatternDirective implements OnInit {
  @Input() typingPattern: string;
  @Input() withError = false;
  @Output() patternOnBlur: EventEmitter<any> = new EventEmitter();
  @Output() notifyError: EventEmitter<boolean> = new EventEmitter();

  private el: HTMLInputElement;

  constructor(private renderer: Renderer2, private elementRef: ElementRef) {
    this.el = this.elementRef.nativeElement;
  }

  ngOnInit(): void {
    if (!this.withError) {
      this.convertAndMatch();
    }
  }

  @HostListener('keyup', ['$event'])
  onkeyup(event: any) {
    const ret = this.convertAndMatch();

    if (this.withError) {
      if (ret) {
        this.notifyError.emit(false);
      } else {
        this.renderer.addClass(this.el, 'error');
        this.notifyError.emit(true);
      }
    }
  }

  @HostListener('focus', ['$event'])
  onFocus(event: any) {
    this.renderer.removeClass(this.el, 'error');
  }

  @HostListener('blur', ['$event'])
  onBlur(event: any) {
    const ret = this.convertAndMatch();

    if (!this.withError){
      if (ret) {
        this.patternOnBlur.emit();
      } else if (!ret) {
        this.renderer.addClass(this.el, 'error');
      }
    }
  }

  private convertAndMatch(): boolean {
    let value = this.el.value;
    value = value.toUpperCase();
    this.el.value = value;

    return value.match(new RegExp(this.typingPattern)) != null;
  }

}
