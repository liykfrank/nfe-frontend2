import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { AirlineService } from './services/airline.service';
import { AirlineComponent } from './airline.component';

describe('AirlineComponent', () => {
    let comp: AirlineComponent;
    let fixture: ComponentFixture<AirlineComponent>;

    beforeEach(() => {
        const airlineServiceStub = {
            validateAirlinet: () => ({
                subscribe: () => ({})
            })
        };
        TestBed.configureTestingModule({
            declarations: [ AirlineComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
                { provide: AirlineService, useValue: airlineServiceStub }
            ]
        });
        fixture = TestBed.createComponent(AirlineComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('showContact defaults to: true', () => {
        expect(comp.showContact).toEqual(true);
    });

    it('showMoreDetails defaults to: true', () => {
        expect(comp.showMoreDetails).toEqual(true);
    });

    it('disabledMoreDetails defaults to: true', () => {
        expect(comp.disabledMoreDetails).toEqual(true);
    });

    it('display defaults to: false', () => {
        expect(comp.display).toEqual(false);
    });

});
