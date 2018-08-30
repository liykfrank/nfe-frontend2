import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ROUTES } from '../../../shared/constants/routes';
import { MenuComponent } from './menu.component';

describe('MenuComponent', () => {
    let comp: MenuComponent;
    let fixture: ComponentFixture<MenuComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [ MenuComponent ],
            schemas: [ NO_ERRORS_SCHEMA ]
        });
        fixture = TestBed.createComponent(MenuComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('routes defaults to: ROUTES', () => {
        expect(comp.routes).toEqual(ROUTES);
    });

});
