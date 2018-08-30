import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ContactComponent } from './contact.component';

describe('ContactComponent', () => {
    let comp: ContactComponent;
    let fixture: ComponentFixture<ContactComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [ ContactComponent ],
            schemas: [ NO_ERRORS_SCHEMA ]
        });
        fixture = TestBed.createComponent(ContactComponent);
        comp = fixture.componentInstance;
    });

    it('can load instance', () => {
        expect(comp).toBeTruthy();
    });

});
