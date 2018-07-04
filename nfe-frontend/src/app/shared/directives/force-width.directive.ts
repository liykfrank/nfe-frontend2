
import { Directive, Input, ElementRef, Renderer2, HostBinding, OnInit, RendererStyleFlags2 } from '@angular/core';

@Directive({
  selector: '[forceWidth]'
})
export class ForceWidthDirective implements OnInit {

  @Input() setWidth: string;

  constructor (public _renderer: Renderer2, public _el: ElementRef) { }

  ngOnInit(): void {
    this._renderer.setStyle(this._el.nativeElement, 'width', this.setWidth, RendererStyleFlags2.Important);
  }

}
