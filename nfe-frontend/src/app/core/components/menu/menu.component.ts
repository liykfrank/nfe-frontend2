import { Component } from '@angular/core';
import { ROUTES } from '../../../shared/constants/routes';

@Component({
    selector: 'bspl-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.scss']
})
export class MenuComponent {

  public routes = ROUTES;

  constructor() { }

}

