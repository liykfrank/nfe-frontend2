import { Directive, ElementRef, Input, OnDestroy, OnInit, Renderer2, OnChanges, SimpleChanges } from '@angular/core';

@Directive({
  selector: '[appIdentifier]'
})
export class IdentifierDirective implements OnInit, OnDestroy/* , OnChanges */ {

  private changes: MutationObserver;

  @Input() base: string;

  constructor (
    private _renderer: Renderer2,
    private _el: ElementRef
  ) {

    this.changes = new MutationObserver(() => this.findOnTree());

    const config: MutationObserverInit = {
      /* attributes: true, */
      childList: true,
      subtree: true
      /* ,
      characterData: true */
    };

    this.changes.observe(this._el.nativeElement, config);
  }

  ngOnInit(): void {
    if (!this.base || this.base.length == 0) {
      throw new Error('Directive appIdentifier doesn\'t work without a base name');
    }

    this.findOnTree();
  }

  /* ngOnChanges(changes: SimpleChanges): void {
    if (changes._el) {
      this.findOnTree();
    }
  } */

  ngOnDestroy(): void {
    this.changes.disconnect();
  }

  private findOnTree(): void {
    // Set name on arrays
    this._el.nativeElement.querySelectorAll('div[formarrayname]').forEach(aux => {
      const baseAux = this.base;
      this.base = this.base + '.' + aux.getAttribute('formarrayname');
      this.setID(aux, true);
      this.base = baseAux;
    });

    // Set name on the other elements
    this.setID(this._el.nativeElement);
  }

  private setID(elems, isArray?: boolean): void {
    this.findByType(elems, 'input', isArray);
    this.findByType(elems, 'textarea', isArray);
    this.findByType(elems, 'select', isArray);
    this.findByType(elems, 'i', isArray);
    this.findByType(elems, 'button', isArray);
  }

  private findByType(elems, type: string, isArray?: boolean): void {
    let i = 0;

    elems.querySelectorAll(type).forEach(elem => {
      const id = elem.getAttribute('id');
      const formcontrolname = elem.getAttribute('formcontrolname') ? elem.getAttribute('formcontrolname') : elem.getAttribute('ng-reflect-name');

      if (!id && formcontrolname) {
        const name = (this.base ? this.base + '.' : '') + formcontrolname + (isArray && isArray == true ? '.' + i.toString() : '');
        this._renderer.setProperty(elem, 'id', name);
      }

      if (!id && (type == 'i' || type == 'button')) {
        this._renderer.setProperty(elem, 'id', this.findValidName(type));
      }

      i++;
    });
  }

  private findValidName(type: string): string {
    const name = (this.base ? this.base + '.' : '') + type + '.';
    let i = 0;

    while (this._el.nativeElement.querySelector(type + '[id="' + name + i.toString() + '"]')) {
      i++;
    }

    return name + i.toString();
  }

}
