import { Subscription } from 'rxjs/Subscription';
import { OnDestroy } from '@angular/core';


export abstract class AbstractComponent implements OnDestroy {

  protected subscriptions: Subscription[] = [];

  constructor() {}

  /**
   * Removes all current subscriptions for this component.
   */
  protected removeSubscriptions() {
    for (const subscription of this.subscriptions) {
      subscription.unsubscribe();
    }
    this.subscriptions = [];
  }

  ngOnDestroy(): void {
    this.removeSubscriptions();
  }

  setFocus() {
    const errores = document.getElementsByClassName('form-control error');
    const aux = errores.length > 0 ? <HTMLElement>errores[0] : null;
    if (aux) {
      aux.focus();
    }
  }
}
