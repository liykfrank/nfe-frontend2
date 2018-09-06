import { Directive, ElementRef, Input, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { FormGroup, NgModelGroup, ControlContainer, FormGroupDirective, FormArray } from '@angular/forms';

@Directive({
  selector: '[appIdentifier]'
})
export class IdentifierDirective /* extends NgModelGroup */
  implements OnInit, OnDestroy /* , OnChanges */ {
  private changes: MutationObserver;

  @Input()
  base: string;

  constructor(
    private _renderer: Renderer2,
    private _el: ElementRef
  ) {
    // super(parent, validators, asyncValidators);
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
      throw new Error(
        "Directive appIdentifier doesn't work without a base name"
      );
    }

    this.findOnTree();
  }

  ngOnDestroy(): void {
    this.changes.disconnect();
  }

  private findOnTree(): void {
    // Set name on arrays
    this._el.nativeElement
      .querySelectorAll('[formarrayname]')
      .forEach(aux => {
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
    this.findByType(elems, 'p-calendar', isArray);
  }

  private findByType(elems, type: string, isArray?: boolean): void {
    let i = 0;

    elems.querySelectorAll(type).forEach(elem => {
      const id = elem.getAttribute('id');
      const formcontrolname = elem.getAttribute('formcontrolname')
        ? elem.getAttribute('formcontrolname')
        : elem.getAttribute('ng-reflect-name');

      if (!id && formcontrolname) {
        let name = `${(this.base ? this.base + '.' : '')}${formcontrolname}${isArray ? '.' + i.toString() : ''}`;

        const count = this._el.nativeElement.querySelectorAll('[id="' + name + '"]');
        if (count.length != 0) {
          name += '_' + count.length;
        }
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
    const count = this._el.nativeElement.querySelectorAll(type + '[id="' + name + '"]');
    return name + count.length;
  }

}
