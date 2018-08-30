import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ElementRef } from '@angular/core';
import { DropdownMultiComponent } from './dropdown-multi.component';

describe('DropdownMultiComponent', () => {
    let comp: DropdownMultiComponent;
    let fixture: ComponentFixture<DropdownMultiComponent>;

    const aux_country = [
        { 'isoCountryCode': 'AA', 'name': 'Country AA' },
        { 'isoCountryCode': 'BB', 'name': 'Country BB' },
        { 'isoCountryCode': 'CC', 'name': 'Country CC' }
    ];

    const simple_array = [
        'AA',
        'BB',
        'CC'
    ];

    beforeEach(() => {
        const elementRefStub = {};
        TestBed.configureTestingModule({
            declarations: [ DropdownMultiComponent ],
            schemas: [ NO_ERRORS_SCHEMA ],
            providers: [
                { provide: ElementRef, useValue: elementRefStub }
            ]
        });
        fixture = TestBed.createComponent(DropdownMultiComponent);
        comp = fixture.componentInstance;
    });

    it('COMPONENT can load instance', () => {
        expect(comp).toBeTruthy();
    });

    it('ATTRIBUTE [selectAllBar] defaults to: false', () => {
        expect(comp.selectAllBar).toEqual(false);
    });

    it('ATTRIBUTE [buttonsBar] defaults to: false', () => {
        expect(comp.buttonsBar).toEqual(false);
    });

    it('ATTRIBUTE optionSelected SET', () => {
        comp.optionSelected = null;
        expect(comp._optionSelected).toEqual([]);
        comp.optionSelected = aux_country;
        expect(comp._optionSelected).toEqual(aux_country);
    });

    it('ATTRIBUTE _optionSelected defaults to: []', () => {
        expect(comp._optionSelected).toEqual([]);
    });

    it('FUNCTION selectOption()', () => {
        comp.selectOption(aux_country[0]);
        expect(comp._optionSelected).toContain(aux_country[0]);

        comp.selectOption(aux_country[2]);
        expect(comp._optionSelected).toContain(aux_country[0]);
        expect(comp._optionSelected).toContain(aux_country[2]);

        comp.selectOption(aux_country[0]);
        expect(comp._optionSelected).toEqual([aux_country[2]]);
    });

    it('FUNCTION buildResponse()', () => {
        let val;
        comp._optionSelected = aux_country;
        comp.selectedItem.subscribe(data => val = data);
        comp.buildResponse();
        expect(val).toEqual(aux_country);

        comp.optionValue = 'isoCountryCode';
        comp.buildResponse();
        expect(val).toEqual(simple_array);
    });

    it('FUNCTION selectAll()', () => {
        comp._options = aux_country;
        comp.selectAll();
        expect(comp._optionSelected).toEqual(comp._options);

        comp.selectAll();
        expect(comp._optionSelected).toEqual([]);
    });

    it('FUNCTION checkAllSelected()', () => {
        expect(comp.checkAllSelected()).toBeFalsy();

        comp._options = aux_country;
        expect(comp.checkAllSelected()).toBeFalsy();

        comp._optionSelected = aux_country;
        expect(comp.checkAllSelected()).toBeTruthy();
    });

    it('FUNCTION checkClassAllSelected()', () => {
        expect(comp.checkClassAllSelected()).toEqual('');
        comp._options = aux_country;
        comp._optionSelected = aux_country;
        expect(comp.checkClassAllSelected()).toEqual('selected');
    });

    it('FUNCTION checkSelectAll()', () => {
        expect(comp.checkSelectAll()).toBeFalsy();
        comp.selectAllBar = true;
        expect(comp.checkSelectAll()).toBeTruthy();
    });

    it('FUNCTION checkButtonsBar()', () => {
        expect(comp.checkButtonsBar()).toBeFalsy();
        comp.buttonsBar = true;
        expect(comp.checkButtonsBar()).toBeTruthy();
    });

    it('FUNCTION checkClassSelected()', () => {
        expect(comp.checkClassSelected(aux_country[0])).toEqual('');
        comp._optionSelected = aux_country;
        expect(comp.checkClassSelected(aux_country[0])).toEqual('selected');
    });

    it('FUNCTION clickApply()', () => {
        let val;
        comp.buttonsBar = true;
        comp._optionSelected = aux_country;
        comp.selectedItem.subscribe(data => val = data);
        comp.clickApply();
        expect(val).toEqual(aux_country);
        expect(comp.open).toBeFalsy();
    });

    it('FUNCTION clickCancel()', () => {
        let aux = 0;
        comp.cancel.subscribe(data => aux++);
        comp.clickCancel();
        expect(aux).toEqual(1);
        expect(comp.open).toBeFalsy();
    });
});
