import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { MenuTabsComponent } from './menu-tabs.component';

describe('MenuTabsComponent', () => {
    let comp: MenuTabsComponent;
    let fixture: ComponentFixture<MenuTabsComponent>;

    beforeEach(() => {
        const activatedRouteStub = {
            root: {},
            firstChild: {
                firstChild: {
                    firstChild: {
                        firstChild: {
                            snapshot: {
                                data: {
                                    tab: {}
                                }
                            }
                        },
                        snapshot: {
                            data: {
                                tab: {}
                            }
                        }
                    }
                }
            }
        };
        const routerStub = {
            routerState: {
                snapshot: {
                    url: {}
                }
            },
            events: {
                filter: () => ({
                    subscribe: () => ({})
                })
            },
            navigate: () => ({})
        };
        TestBed.configureTestingModule({
            declarations: [ MenuTabsComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
                { provide: ActivatedRoute, useValue: activatedRouteStub },
                { provide: Router, useValue: routerStub }
            ]
        });
        fixture = TestBed.createComponent(MenuTabsComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

    /*
    describe('ngOnInit', () => {
      it('makes expected calls', () => {
          spyOn(comp, '_getTabUrl');
          spyOn(comp, '_existTab');
          spyOn(comp, '_addTab');
          comp.ngOnInit();
          expect(comp._getTabUrl).toHaveBeenCalled();
          expect(comp._existTab).toHaveBeenCalled();
          expect(comp._addTab).toHaveBeenCalled();
      });
    });
    */

});
